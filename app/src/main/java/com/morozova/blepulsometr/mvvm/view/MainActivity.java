package com.morozova.blepulsometr.mvvm.view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.morozova.blepulsometr.R;
import com.morozova.blepulsometr.constants.DiConstants;
import com.morozova.blepulsometr.mvvm.viewmodel.implementations.BLEViewModel;
import com.morozova.blepulsometr.mvvm.viewmodel.interfaces.IBLEViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {


    @Inject
    @Named(DiConstants.VIEWMODELFACTORY_VOICE_BOT)
    ViewModelProvider.AndroidViewModelFactory mBLEViewModelFactory;
    private BluetoothAdapter mBluetoothAdapter;
    private IBLEViewModel iBLEViewModel;
    private List<BluetoothDevice> listDevices = new ArrayList<>();

    private static final int RQS_ENABLE_BLUETOOTH = 1;

    private ImageView ivBluetoothState;
    private TextView tvPulseInfo;
    private ListView listBondedDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        setContentView(R.layout.activity_main);

       initView();

        ViewModelProvider provider = new ViewModelProvider(this, mBLEViewModelFactory);
        iBLEViewModel = provider.get(BLEViewModel.class);
        iBLEViewModel.init(MainActivity.this);


        iBLEViewModel.getUiEventsLiveData()
                .observe(this, this::onUiEventReceived);
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            showInfoMsg(getString(R.string.ble_not_supported));
            finish();
        }
        getBluetoothAdapterAndLeScanner();
    }

    private void initView() {
        ivBluetoothState = findViewById(R.id.iv_bluetooth_state);
        tvPulseInfo = findViewById(R.id.tv_pulse_info);
        listBondedDevices = findViewById(R.id.list_bonded_devices);

        listBondedDevices.setOnItemClickListener((adapterView, view, i, l) -> {
            getPulse(listDevices.get(i));
        });
    }

    private void onUiEventReceived(String s) {
        switch (s){
            case IBLEViewModel.BLUETOOTH_ON:{
                ivBluetoothOnOff(R.drawable.bluetooth_connected);
            } break;
            case IBLEViewModel.BLUETOOTH_OFF: {
                ivBluetoothOnOff(R.drawable.bluetooth_disabled);
                bluetoothDialogOn();
            } break;
            default: showInfoMsg(s);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       bluetoothDialogOn();
    }

    private void bluetoothDialogOn() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, RQS_ENABLE_BLUETOOTH);
        } else {
            ivBluetoothOnOff(R.drawable.bluetooth_connected);
            showListDevices();
        }
    }

    private void ivBluetoothOnOff(int drawable) {
        ivBluetoothState.setImageDrawable(getDrawable(drawable));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RQS_ENABLE_BLUETOOTH && resultCode == Activity.RESULT_CANCELED) {
            showInfoMsg(getString(R.string.ble_is_off));
            return;
        } else if (requestCode == RQS_ENABLE_BLUETOOTH && resultCode == Activity.RESULT_OK) {
            ivBluetoothOnOff(R.drawable.bluetooth_connected);
            showListDevices();
        }
        getBluetoothAdapterAndLeScanner();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showListDevices() {
        listDevices = iBLEViewModel.getBondDevices(mBluetoothAdapter);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listDevices);
        listBondedDevices.setAdapter(adapter);
    }

    private void getPulse(BluetoothDevice device) {
        iBLEViewModel.getPulse(device);
        if (!iBLEViewModel.getLiveDataPulse().hasObservers()) {
            iBLEViewModel.getLiveDataPulse().observe(this, this::onPulseReceived);
        }
    }

    private void onPulseReceived(String pulse) {
        tvPulseInfo.setText(getString(R.string.pulse_info, pulse));
    }

    private void getBluetoothAdapterAndLeScanner() {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    private void showInfoMsg(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iBLEViewModel.uninit();
    }
}
