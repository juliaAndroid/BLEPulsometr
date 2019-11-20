package com.morozova.blepulsometr.mvvm.model.interactors.implementations;

import com.morozova.blepulsometr.mvvm.model.interactors.interfaces.IBLEInteractor;
import com.morozova.blepulsometr.mvvm.model.repositories.interfaces.IBLERepository;

import javax.inject.Inject;

public class BLEInteractorImpl implements IBLEInteractor {

    private IBLERepository bleRepository;

    @Inject
    BLEInteractorImpl(IBLERepository repository) {
        this.bleRepository = repository;
    }

}
