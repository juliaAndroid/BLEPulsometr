package com.morozova.blepulsometr.di.modules;

import com.morozova.blepulsometr.mvvm.model.repositories.implementation.BLERepository;
import com.morozova.blepulsometr.mvvm.model.repositories.interfaces.IBLERepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public interface RepositoriesModule {

    @Binds
    @Singleton
    IBLERepository provideRepository(BLERepository repository);
}
