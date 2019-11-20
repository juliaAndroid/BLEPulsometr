package com.morozova.blepulsometr;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import com.morozova.blepulsometr.di.components.AppComponent;
import com.morozova.blepulsometr.di.components.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;


public class App extends Application implements HasActivityInjector, HasServiceInjector {

    private static AppComponent sAppComponent;

    @Inject
    DispatchingAndroidInjector<Activity> mDispatchingAndroidInjectorActivity;

    @Inject
    DispatchingAndroidInjector<Service> mDispatchingAndroidInjectorService;

    @Override
    public void onCreate() {
        super.onCreate();

        initDagger2();
    }

    private void initDagger2() {
        sAppComponent = DaggerAppComponent
                .builder()
                .appContext(this)
                .build();
        sAppComponent.inject(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return mDispatchingAndroidInjectorActivity;
    }

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

    @Override
    public DispatchingAndroidInjector<Service> serviceInjector() {
        return mDispatchingAndroidInjectorService;
    }

}
