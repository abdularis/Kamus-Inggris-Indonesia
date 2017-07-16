package com.paperplanes.mykamus;

import android.app.Application;

import com.paperplanes.mykamus.di.components.AppComponent;
import com.paperplanes.mykamus.di.components.DaggerAppComponent;
import com.paperplanes.mykamus.di.modules.AppModule;

/**
 * Created by abdularis on 15/07/17.
 */

public class DictionaryApp extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
