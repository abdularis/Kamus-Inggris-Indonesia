package com.paperplanes.mykamus.domain.model;

/**
 * Created by abdularis on 26/03/17.
 */

public class Word {

    private int mId;
    private String mWord;
    private String mTrans;
    private boolean mBookmarked;

    public Word(int id, String word, String trans, boolean bookmarked) {
        mId = id;
        mWord = word;
        mTrans = trans;
        mBookmarked = bookmarked;
    }

    public int getId() {
        return mId;
    }

    public String getWord() {
        return mWord;
    }

    public String getTrans() {
        return mTrans;
    }

    public boolean isBookmarked() {
        return mBookmarked;
    }


    public void setId(int id) {
        mId = id;
    }

    public void setWord(String word) {
        mWord = word;
    }

    public void setTrans(String trans) {
        mTrans = trans;
    }

    public void setBookmarked(boolean bookmarked) {
        mBookmarked = bookmarked;
    }
}
