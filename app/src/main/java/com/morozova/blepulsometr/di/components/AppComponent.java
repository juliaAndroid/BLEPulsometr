package com.morozova.blepulsometr.di.components;


import com.morozova.blepulsometr.App;
import com.morozova.blepulsometr.di.modules.ActivityModule;
import com.morozova.blepulsometr.di.modules.AppActivitiesModule;
import com.morozova.blepulsometr.di.modules.ApplicationModule;
import com.morozova.blepulsometr.di.modules.InteractorsModule;
import com.morozova.blepulsometr.di.modules.RepositoriesModule;
import com.morozova.blepulsometr.di.modules.RxBleModule;
import com.morozova.blepulsometr.di.modules.SchedulersModule;
import com.morozova.blepulsometr.di.modules.ViewModelFactoriesModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
        ActivityModule.class,
        AppActivitiesModule.class,
        ApplicationModule.class,
        InteractorsModule.class,
        RepositoriesModule.class,
        SchedulersModule.class,
        RxBleModule.class,
        ViewModelFactoriesModule.class})
public interface AppComponent {

    void inject(App app);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder appContext(App app);

        AppComponent build();
    }
}