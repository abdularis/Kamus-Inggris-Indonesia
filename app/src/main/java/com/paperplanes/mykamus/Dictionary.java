package com.paperplanes.mykamus;

import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by abdularis on 26/03/17.
 */

public class Dictionary {
    public static final int MAX_RESULT_LIMIT = 30;

    private SQLiteOpenHelper mOpenHelper;
    private SQLiteDatabase mDb = null;

    private List<SearchResultListener> mSearchListener;

    private static Dictionary instance = null;

    private String mLastKeyword = "";
    private TranslationType mTType = TranslationType.EN_TO_ID;

    enum TranslationType {
        ID_TO_EN, EN_TO_ID
    }

    public static Dictionary getInstance(Context context) {
        if (instance == null)
            instance = new Dictionary(context);
        return instance;
    }

    public Dictionary(Context context) {
        mOpenHelper = new DbHelper(context);
        mSearchListener = new ArrayList<SearchResultListener>();
    }

    public void open() {
        mDb = mOpenHelper.getReadableDatabase();
    }

    public void close() {
        if (mDb != null) {
            mDb.close();
            mOpenHelper.close();
        }
    }

    private void sortResult(List<Word> result) {
        Word tmp, tmp2;

        char pref = mLastKeyword.charAt(0);
        int resLen = result.size();
        for (int i = 0; i < resLen; i++) {
            tmp = result.get(i);
            for (int j = i+1; j < resLen; j++) {
                tmp2 = result.get(j);
                if (tmp2.getWord().charAt(0) == pref) {
                    if (tmp2.getWord().length() < tmp.getWord().length()) {
                        result.set(i, tmp2);
                        result.set(j, tmp);

                        tmp = tmp2;
                    }
                }
            }
        }
    }

    private HashMap<String, List<Word>> mLastResults = new HashMap<String, List<Word>>();
    public List<Word> search(String keyword) {
        mLastKeyword = keyword;
        List<Word> result = mLastResults.get(mLastKeyword);

        if (result != null) {
            callListener(keyword, result);
            return result;
        }

        if (mDb != null) {
            result = new ArrayList<Word>();

            if (keyword.length() > 0) {
                String tableName = "data_id_en";
                if (mTType == TranslationType.EN_TO_ID)
                    tableName = "data_en_id";

                Cursor cursor = mDb.rawQuery(
                        "SELECT * FROM " + tableName + " WHERE word MATCH '" + keyword +
                                "*' LIMIT " + String.valueOf(MAX_RESULT_LIMIT) + ";", null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Word w = new Word(cursor.getString(0), cursor.getString(1));
                    result.add(w);
                    cursor.moveToNext();
                }
                cursor.close();

                if (result.size() > 0)
                    sortResult(result);
                mLastResults.put(keyword, result);
            }

            callListener(keyword, result);

        }

        return result;
    }

    private void callListener(String keyword, List<Word> result) {
        if (mSearchListener.size() <= 0)
            return;

        if (keyword.length() > 0 && result.isEmpty()) {
            for (SearchResultListener l : mSearchListener)
                l.onNotFound(keyword);
        }
        else {
            for (SearchResultListener l : mSearchListener)
                l.onFound(result);
        }
    }

    public void addSearchResultListener(SearchResultListener listener) {
        if (listener != null)
            mSearchListener.add(listener);
    }

    public void setTranslationType(TranslationType type) {
        if (mTType != type) {
            mTType = type;
            mLastResults.clear();
            search(mLastKeyword);
        }
    }

    public TranslationType getTranslationType() {
        return mTType;
    }

    public String getLastKeyword() {
        return mLastKeyword;
    }

    public interface SearchResultListener {
        void onFound(List<Word> results);
        void onNotFound(String keyword);
    }

}
