package com.paperplanes.mykamus.config;

import android.content.SharedPreferences;

import com.paperplanes.mykamus.domain.model.TranslationMode;

/**
 * Created by abdularis on 16/07/17.
 */

public class Config {
    private static final String KEY_TRANS_MODE = "translation_mode";
    private static final String KEY_MAX_RESULT = "max_result";

    private static final int DEF_MAX_RESULTS = 65;

    private TranslationMode mTranslationMode;
    private int mMaxResultLimit;
    private SharedPreferences mPreferences;

    private static Config Instance = null;

    public static Config getInstance(SharedPreferences preferences) {
        if (Instance == null) {
            Instance = new Config(preferences);
        }

        return Instance;
    }

    private Config(SharedPreferences preferences) {
        mPreferences = preferences;

        String str = mPreferences.getString(KEY_TRANS_MODE, TranslationMode.values()[0].toString());
        mTranslationMode = TranslationMode.valueOf(str);
        mMaxResultLimit = mPreferences.getInt(KEY_MAX_RESULT, DEF_MAX_RESULTS);
    }

    public TranslationMode getTranslationMode() {
        return mTranslationMode;
    }

    public void setTranslationMode(TranslationMode translationMode) {
        mTranslationMode = translationMode;
        mPreferences.edit()
                .putString(KEY_TRANS_MODE, translationMode.toString())
                .apply();
    }

    public int getMaxResultLimit() {
        return mMaxResultLimit;
    }

    public void setMaxResultLimit(int maxResultLimit) {
        mMaxResultLimit = maxResultLimit;
        mPreferences.edit()
                .putInt(KEY_MAX_RESULT, mMaxResultLimit)
                .apply();
    }
}
