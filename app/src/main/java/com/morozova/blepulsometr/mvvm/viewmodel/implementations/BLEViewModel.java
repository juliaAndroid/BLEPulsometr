package com.morozova.blepulsometr.mvvm.viewmodel.implementations;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.morozova.blepulsometr.BuildConfig;
import com.morozova.blepulsometr.constants.DiConstants;
import com.morozova.blepulsometr.mvvm.model.interactors.interfaces.IBLEInteractor;
import com.morozova.blepulsometr.mvvm.viewmodel.implementations.base.BaseErrorViewModel;
import com.morozova.blepulsometr.mvvm.viewmodel.interfaces.IBLEViewModel;
import com.morozova.blepulsometr.mvvm.viewmodel.livedata.UiEventsLiveData;
import com.polidea.rxandroidble2.RxBleClient;

import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.inject.Named;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

public class BLEViewModel extends BaseErrorViewModel implements IBLEViewModel {

    private static final String TAG = "test";
    private MutableLiveData<String> mPulseLiveData;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final IBLEInteractor mInteractor;
    private final Scheduler mUiScheduler;
    private final RxBleClient mRxBleClient;
    private UiEventsLiveData mUiEventsLiveData;
    private WeakReference<Activity> mActivity;
    private final static UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    public BLEViewModel(Application app, @Named(DiConstants.SCHEDULER_UI) Scheduler uiScheduler, IBLEInteractor interactor, RxBleClient rxBleClient) {
        super(app);
        this.mUiScheduler = uiScheduler;
        this.mInteractor = interactor;
        this.mRxBleClient = rxBleClient;
    }


    @Override
    public void init(@NonNull Activity activity) {
        this.mActivity = new WeakReference<>(activity);
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        activity.registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    public MutableLiveData<String> getLiveDataPulse() {
        if (mPulseLiveData == null) {
            mPulseLiveData = new MutableLiveData<>();
        }
        return mPulseLiveData;
    }

    @Override
    public void getPulse(BluetoothDevice device) {
        device.connectGatt(mActivity.get(), false, gattCallback);
    }

    @Override
    protected void defaultErrorHandling(@Nullable Throwable throwable) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "Exception in " + BLEViewModel.class.getSimpleName(), throwable);
        }
    }

    @Override
    public UiEventsLiveData getUiEventsLiveData() {
        if (mUiEventsLiveData == null) {
            mUiEventsLiveData = new UiEventsLiveData();
        }
        return mUiEventsLiveData;
    }

    @Override
    public List<BluetoothDevice> getBondDevices(BluetoothAdapter bluetoothAdapter) {
        List<BluetoothDevice> list = new ArrayList<>();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            list.addAll(pairedDevices);
        }
        return list;
    }

    private final BluetoothGattCallback gattCallback =
            new BluetoothGattCallback() {
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                                    int newState) {
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            getUiEventsLiveData().setValue(IBLEViewModel.CONNECTED_TO_GATT_SERVER);
                            getUiEventsLiveData().setValue(IBLEViewModel.GETTING_SERVICES);
                            boolean ans = gatt.discoverServices();
                            Log.d(TAG, "Discover Services started: " + ans);
                        });
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        getUiEventsLiveData().setValue(IBLEViewModel.DISCONNECTED_FROM_GATT_SERVER);
                    }
                }

                @Override
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        BluetoothGattService serv = gatt.getService(CLIENT_CHARACTERISTIC_CONFIG);
                        if (serv != null) {
                            BluetoothGattCharacteristic characteristic = serv.getCharacteristic(CLIENT_CHARACTERISTIC_CONFIG);
                            if (characteristic.getUuid() == CLIENT_CHARACTERISTIC_CONFIG) {
                                gatt.readCharacteristic(characteristic);
                            } else {
                                Log.d(TAG, "res was false");
                            }
                        }
                    } else {
                        Log.i(TAG, "onServicesDiscovered received: " + status);
                    }
                }

                @Override
                public void onCharacteristicRead(BluetoothGatt gatt,
                                                 BluetoothGattCharacteristic characteristic,
                                                 int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        String value = new String(characteristic.getValue(), StandardCharsets.UTF_8);
                        getUiEventsLiveData().setValue(IBLEViewModel.SUCCESSFULY_READ_CHARACTERISTIC);
                        getLiveDataPulse().setValue(value);
                    } else {
                        getUiEventsLiveData().setValue(IBLEViewModel.CHARACTERISTIC_READ_NOT_SUCCESSFUL);
                    }
                }
            };


    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        getUiEventsLiveData().setValue(IBLEViewModel.BLUETOOTH_OFF);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        getUiEventsLiveData().setValue(IBLEViewModel.BLUETOOTH_ON);
                        break;
                }

            }
        }
    };

    @Override
    public void uninit(){
        mActivity.get().unregisterReceiver(mBroadcastReceiver);
    }
}
