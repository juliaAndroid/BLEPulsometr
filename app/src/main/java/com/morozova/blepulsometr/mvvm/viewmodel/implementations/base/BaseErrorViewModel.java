package com.morozova.blepulsometr.mvvm.viewmodel.implementations.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import io.reactivex.functions.Consumer;

public abstract class BaseErrorViewModel extends AndroidViewModel implements Consumer<Throwable> {


    public BaseErrorViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public final void accept(@Nullable Throwable throwable) {
        if (throwable != null) {
            throwable.printStackTrace();
        }
        defaultErrorHandling(throwable);
    }

    protected abstract void defaultErrorHandling(@Nullable Throwable throwable);

}
