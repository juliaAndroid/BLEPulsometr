package com.morozova.blepulsometr.mvvm.viewmodel.interfaces;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.morozova.blepulsometr.mvvm.viewmodel.livedata.UiEventsLiveData;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public interface IBLEViewModel {

    String BLUETOOTH_OFF = "OFF";
    String BLUETOOTH_ON = "ON";
    String CONNECTED_TO_GATT_SERVER = "Connected to GATT server.";
    String GETTING_SERVICES = "Getting services....";
    String DISCONNECTED_FROM_GATT_SERVER = "Disconnected from GATT server.";
    String SUCCESSFULY_READ_CHARACTERISTIC = "Successfully read characteristic";
    String CHARACTERISTIC_READ_NOT_SUCCESSFUL = "Characteristic read not successful";

    void init(@NonNull Activity activity);

    void uninit();

    MutableLiveData<String> getLiveDataPulse();

    void getPulse(BluetoothDevice bluetoothDevice);

    UiEventsLiveData getUiEventsLiveData();

    List<BluetoothDevice> getBondDevices(BluetoothAdapter bluetoothAdapter);

}
