package com.paperplanes.mykamus.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.paperplanes.mykamus.domain.data.DictionaryDataSource;
import com.paperplanes.mykamus.domain.model.TranslationMode;
import com.paperplanes.mykamus.domain.model.Word;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by abdularis on 15/07/17.
 */

public class DictionaryLocalDataSource implements DictionaryDataSource {

    private DbHelper mHelper;

    @Inject
    public DictionaryLocalDataSource(DbHelper dbHelper) {
        mHelper = dbHelper;
    }

    @Override
    public void searchWord(TranslationMode tMode, String keyword, int limit, GetCallback cb) {
        String tableName = "data_id_en";
        if (tMode == TranslationMode.EN_TO_ID) {
            tableName = "data_en_id";
        }

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * FROM " + tableName + " WHERE word LIKE '" + keyword +
                        "%' LIMIT " + String.valueOf(limit), null);

        List<Word> wordList = new ArrayList<>();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                int id = c.getInt(0);
                String wordStr = c.getString(1);
                String transl = c.getString(2);
                boolean bookmarked = c.getInt(3) == 1;

                wordList.add(new Word(id, wordStr, transl, bookmarked));

                c.moveToNext();
            }
        }
        c.close();

        if (wordList.size() > 0) {
            cb.onWordLoaded(wordList);
        }
        else {
            cb.onWordNotAvailable();
        }
    }

    @Override
    public void addBookmark(TranslationMode tMode, Word word) {
        String table = tMode == TranslationMode.EN_TO_ID ? DbContract.DATA_EN_ID.TABLE_NAME : DbContract.DATA_ID_EN.TABLE_NAME;
        String where = DbContract.DATA_DICT._ID + "=?";
        String whereArgs[] = {String.valueOf(word.getId())};
        ContentValues values = new ContentValues();
        values.put(DbContract.DATA_DICT.COL_BOOKMARKED, 1);

        SQLiteDatabase db = mHelper.getWritableDatabase();

        db.update(table, values, where, whereArgs);
    }

    @Override
    public void removeBookmark(TranslationMode tMode, Word word) {
        String table = tMode == TranslationMode.EN_TO_ID ? DbContract.DATA_EN_ID.TABLE_NAME : DbContract.DATA_ID_EN.TABLE_NAME;
        String where = DbContract.DATA_DICT._ID + "=?";
        String whereArgs[] = {String.valueOf(word.getId())};
        ContentValues values = new ContentValues();
        values.put(DbContract.DATA_DICT.COL_BOOKMARKED, 0);

        SQLiteDatabase db = mHelper.getWritableDatabase();

        db.update(table, values, where, whereArgs);
    }

    @Override
    public void getBookmark(TranslationMode tMode, GetCallback cb) {
        String cols[] = {
                DbContract.DATA_DICT._ID,
                DbContract.DATA_DICT.COL_WORD,
                DbContract.DATA_DICT.COL_TRANS,
                DbContract.DATA_DICT.COL_BOOKMARKED
        };

        String table = tMode == TranslationMode.EN_TO_ID ? DbContract.DATA_EN_ID.TABLE_NAME : DbContract.DATA_ID_EN.TABLE_NAME;
        String sel = DbContract.DATA_DICT.COL_BOOKMARKED + "=?";
        String selArgs[] = {"1"};

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = db.query(table, cols, sel, selArgs, null, null, null);

        List<Word> wordList = new ArrayList<>();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                int id = c.getInt(0);
                String wordStr = c.getString(1);
                String transl = c.getString(2);
                boolean bookmarked = c.getInt(3) == 1;

                wordList.add(new Word(id, wordStr, transl, bookmarked));

                c.moveToNext();
            }
        }
        c.close();

        if (wordList.size() > 0) {
            cb.onWordLoaded(wordList);
        }
        else {
            cb.onWordNotAvailable();
        }
    }

    @Override
    public void clearBookmark(TranslationMode tMode) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        String where = DbContract.DATA_DICT.COL_BOOKMARKED + "=?";
        String whereArgs[] = {"1"};
        String table =
                tMode == TranslationMode.EN_TO_ID ? DbContract.DATA_EN_ID.TABLE_NAME : DbContract.DATA_ID_EN.TABLE_NAME;
        ContentValues values = new ContentValues();
        values.put(DbContract.DATA_DICT.COL_BOOKMARKED, 0);

        db.update(table, values, where, whereArgs);
    }
}
