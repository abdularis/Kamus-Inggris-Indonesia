package com.paperplanes.mykamus.data.local;

import android.provider.BaseColumns;

/**
 * Created by abdularis on 16/07/17.
 */

public class DbContract {

    public static class DATA_DICT implements BaseColumns {
        public static final String COL_WORD = "word";
        public static final String COL_TRANS = "trans";
        public static final String COL_BOOKMARKED = "bookmarked";
    }

    public static class DATA_EN_ID extends DATA_DICT {
        public static final String TABLE_NAME = "data_en_id";
    }

    public static class DATA_ID_EN extends DATA_DICT {
        public static final String TABLE_NAME = "data_id_en";
    }
}
