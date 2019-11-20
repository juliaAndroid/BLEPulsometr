package com.morozova.blepulsometr.di.components;


import com.morozova.blepulsometr.di.annotations.ActivityScope;
import com.morozova.blepulsometr.di.modules.ActivityModule;

import dagger.Component;

@ActivityScope
@Component(modules = {ActivityModule.class}, dependencies =  com.morozova.blepulsometr.di.components.AppComponent.class)
public interface ActivityComponent {
}
