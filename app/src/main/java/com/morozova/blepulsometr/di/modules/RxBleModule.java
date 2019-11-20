package com.morozova.blepulsometr.di.modules;

import com.morozova.blepulsometr.App;
import com.polidea.rxandroidble2.RxBleClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RxBleModule {

    @Provides
    @Singleton
    RxBleClient provideContextRx(App app) {
        return RxBleClient.create(app);
    }
}
