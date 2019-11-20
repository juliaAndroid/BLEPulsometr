package com.morozova.blepulsometr.di.modules;

import com.morozova.blepulsometr.constants.DiConstants;
import com.morozova.blepulsometr.mvvm.viewmodel.implementations.factories.BLEViewModelFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;

@Module
public interface ViewModelFactoriesModule {

    @Binds
    @Singleton
    @Named(DiConstants.VIEWMODELFACTORY_VOICE_BOT)
    ViewModelProvider.AndroidViewModelFactory provideViewModelFactory(BLEViewModelFactory factory);

}