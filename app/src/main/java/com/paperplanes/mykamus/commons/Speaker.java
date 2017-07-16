package com.paperplanes.mykamus.commons;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by abdularis on 28/03/17.
 */

public class Speaker implements TextToSpeech.OnInitListener {

    private TextToSpeech mTTS = null;
    private boolean mReady = false;

    public Speaker(Context context) {
        mTTS = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            mTTS.setLanguage(Locale.US);
            mReady = true;
        }
    }

    public void speak(String text) {
        if (mReady) {
            mTTS.speak(text, TextToSpeech.QUEUE_ADD, null);
        }
    }

    public void pause(int duration) {
        if (mReady) {
            mTTS.playSilence(duration, TextToSpeech.QUEUE_ADD, null);
        }
    }

    public void close() {
        if (mReady) {
            mTTS.shutdown();
        }
    }
}
