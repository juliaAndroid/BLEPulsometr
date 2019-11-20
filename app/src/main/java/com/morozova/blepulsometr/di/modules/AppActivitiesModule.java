package com.morozova.blepulsometr.di.modules;

import com.morozova.blepulsometr.di.annotations.ActivityScope;
import com.morozova.blepulsometr.mvvm.view.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Module(includes = {AndroidSupportInjectionModule.class})
public interface AppActivitiesModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = {ActivityModule.class})
    MainActivity mainActivityInjector();
}
