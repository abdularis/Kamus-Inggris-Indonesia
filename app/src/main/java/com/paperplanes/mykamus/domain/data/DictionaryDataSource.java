package com.paperplanes.mykamus.domain.data;

import com.paperplanes.mykamus.domain.model.TranslationMode;
import com.paperplanes.mykamus.domain.model.Word;

import java.util.List;

/**
 * Created by abdularis on 15/07/17.
 */

public interface DictionaryDataSource {

    interface ErrCallback {

        void onError(Throwable err);

    }

    interface GetCallback extends ErrCallback {

        void onWordLoaded(List<Word> words);

        void onWordNotAvailable();

    }

    void searchWord(TranslationMode tMode, String keyword, int limit, GetCallback cb);

    void addBookmark(TranslationMode tMode, Word word);

    void removeBookmark(TranslationMode tMode, Word word);

    void getBookmark(TranslationMode tMode, GetCallback cb);

    void clearBookmark(TranslationMode tMode);

}
