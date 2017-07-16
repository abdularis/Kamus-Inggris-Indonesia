package com.paperplanes.mykamus.domain.usecases;

import com.paperplanes.mykamus.domain.data.DictionaryDataSource;
import com.paperplanes.mykamus.domain.model.TranslationMode;
import com.paperplanes.mykamus.domain.model.Word;

import javax.inject.Inject;

/**
 * Created by abdularis on 16/07/17.
 */

public class RemoveBookmarkUseCase extends UseCase<RemoveBookmarkUseCase.Params, RemoveBookmarkUseCase.Result> {

    private DictionaryDataSource mDataSource;

    @Inject
    public RemoveBookmarkUseCase(DictionaryDataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    protected void execute(Params params) {
        params.word.setBookmarked(false);
        mDataSource.removeBookmark(params.mode, params.word);
        getCallback().onSuccess(new Result());
    }


    public static class Params implements UseCase.Params {
        TranslationMode mode;
        Word word;

        public Params(Word word, TranslationMode mode) {
            this.word = word;
            this.mode = mode;
        }
    }

    public static class Result implements UseCase.Result {}
}
