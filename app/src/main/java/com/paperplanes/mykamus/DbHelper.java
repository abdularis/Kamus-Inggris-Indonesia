package com.paperplanes.mykamus;

/**
 * Created by abdularis on 26/03/17.
 */


import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DbHelper extends SQLiteAssetHelper {

    public static final String DB_NAME = "kamus.db";
    public static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
}
