package com.morozova.blepulsometr.di.modules;

import com.morozova.blepulsometr.constants.DiConstants;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class SchedulersModule {

    @Provides
    @Singleton
    @Named(DiConstants.SCHEDULER_COMPUTATION)
    Scheduler provideComputationScheduler() {
        return Schedulers.computation();
    }

    @Provides
    @Singleton
    @Named(DiConstants.SCHEDULER_IO)
    Scheduler provideIoScheduler() {
        return Schedulers.io();
    }

    @Provides
    @Singleton
    @Named(DiConstants.SCHEDULER_UI)
    Scheduler provideUiScheduler() {
        return AndroidSchedulers.mainThread();
    }

}
