package com.morozova.blepulsometr.mvvm.viewmodel.implementations.factories;

import android.app.Application;

import com.morozova.blepulsometr.App;
import com.morozova.blepulsometr.constants.DiConstants;
import com.morozova.blepulsometr.mvvm.model.interactors.interfaces.IBLEInteractor;
import com.morozova.blepulsometr.mvvm.viewmodel.implementations.BLEViewModel;
import com.polidea.rxandroidble2.RxBleClient;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import io.reactivex.Scheduler;

public class BLEViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Scheduler mUiScheduler;

    private final IBLEInteractor iBLEInteractor;

    private final Application mApplicaiton;

    private final RxBleClient mRxBleClient;

    @Inject
    public BLEViewModelFactory(App application, @Named(DiConstants.SCHEDULER_UI) Scheduler uiScheduler, IBLEInteractor interactor, RxBleClient rxBleClient) {
        super(application);
        this.mApplicaiton = application;
        this.mUiScheduler = uiScheduler;
        this.iBLEInteractor = interactor;
        this.mRxBleClient = rxBleClient;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BLEViewModel(mApplicaiton, mUiScheduler, iBLEInteractor, mRxBleClient);
    }
}
