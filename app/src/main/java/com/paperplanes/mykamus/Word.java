package com.paperplanes.mykamus;

/**
 * Created by abdularis on 26/03/17.
 */

public class Word {

    private String mWord;
    private String mTrans;

    public Word(String word, String trans) {
        mWord = word;
        mTrans = trans;
    }

    public String getWord() {
        return mWord;
    }

    public String getTrans() {
        return mTrans;
    }

}
