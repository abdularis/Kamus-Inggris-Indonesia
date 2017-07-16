package com.paperplanes.mykamus.di.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.paperplanes.mykamus.presentation.AndroidUseCaseExecutor;
import com.paperplanes.mykamus.commons.Speaker;
import com.paperplanes.mykamus.config.Config;
import com.paperplanes.mykamus.domain.usecases.UseCaseExecutor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by abdularis on 15/07/17.
 */

@Module
public class AppModule {

    Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    Speaker provideSpeaker() {
        return new Speaker(mApplication);
    }

    @Provides
    @Singleton
    UseCaseExecutor provideUseCaseExecutor() {
        return new AndroidUseCaseExecutor();
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences("en_id_dict", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    Config provideConfig(SharedPreferences preferences) {
        return Config.getInstance(preferences);
    }
}
