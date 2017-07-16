package com.paperplanes.mykamus.di.modules;

import android.content.Context;

import com.paperplanes.mykamus.data.local.DbHelper;
import com.paperplanes.mykamus.data.local.DictionaryLocalDataSource;
import com.paperplanes.mykamus.domain.data.DictionaryDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by abdularis on 15/07/17.
 */

@Module
public class DataSourceModule {

    @Provides
    @Singleton
    DbHelper provideDbHelper(Context context) {
        return new DbHelper(context);
    }

    @Provides
    @Singleton
    DictionaryDataSource provideDictionaryDataSource(DictionaryLocalDataSource dataSource) {
        return dataSource;
    }
}
