package com.morozova.blepulsometr.di.modules;

import com.morozova.blepulsometr.mvvm.model.interactors.implementations.BLEInteractorImpl;
import com.morozova.blepulsometr.mvvm.model.interactors.interfaces.IBLEInteractor;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public interface InteractorsModule {

    @Binds
    @Singleton
    IBLEInteractor provideInteractor(BLEInteractorImpl interactor);
}
