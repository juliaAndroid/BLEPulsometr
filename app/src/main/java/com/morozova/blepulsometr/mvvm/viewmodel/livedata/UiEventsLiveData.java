package com.morozova.blepulsometr.mvvm.viewmodel.livedata;

import com.morozova.blepulsometr.mvvm.viewmodel.interfaces.IBLEViewModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.Nullable;
import androidx.annotation.StringDef;
import androidx.lifecycle.MutableLiveData;

public class UiEventsLiveData extends MutableLiveData<String> {

    @Override
    public void postValue(@UiEvents String value) {
        super.postValue(value);
    }

    @Override
    public void setValue(@UiEvents String value) {
        super.setValue(value);
    }

    @Nullable
    @Override
    @UiEvents
    public String getValue() {
        return super.getValue();
    }

    @StringDef({IBLEViewModel.BLUETOOTH_OFF,
            IBLEViewModel.BLUETOOTH_ON,
            IBLEViewModel.CONNECTED_TO_GATT_SERVER,
            IBLEViewModel.GETTING_SERVICES,
            IBLEViewModel.DISCONNECTED_FROM_GATT_SERVER,
            IBLEViewModel.SUCCESSFULY_READ_CHARACTERISTIC,
            IBLEViewModel.CHARACTERISTIC_READ_NOT_SUCCESSFUL,
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface UiEvents {
    }
}