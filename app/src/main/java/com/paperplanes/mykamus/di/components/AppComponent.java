package com.paperplanes.mykamus.di.components;

import com.paperplanes.mykamus.di.modules.AppModule;
import com.paperplanes.mykamus.di.modules.DataSourceModule;
import com.paperplanes.mykamus.presentation.ui.fragment.BookmarkFragment;
import com.paperplanes.mykamus.presentation.ui.fragment.DictionaryFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by abdularis on 15/07/17.
 */

@Singleton
@Component(modules = {AppModule.class, DataSourceModule.class})
public interface AppComponent {

    void inject(DictionaryFragment fragment);

    void inject(BookmarkFragment fragment);

}
