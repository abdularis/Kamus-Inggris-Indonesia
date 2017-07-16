package com.paperplanes.mykamus.domain.usecases;

import com.paperplanes.mykamus.domain.data.DictionaryDataSource;
import com.paperplanes.mykamus.domain.model.TranslationMode;
import com.paperplanes.mykamus.domain.model.Word;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by abdularis on 16/07/17.
 */

public class AddBookmarkUseCase extends UseCase<AddBookmarkUseCase.Params, AddBookmarkUseCase.Result> {

    private DictionaryDataSource mDataSource;

    @Inject
    public AddBookmarkUseCase(DictionaryDataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    protected void execute(Params params) {
        params.word.setBookmarked(true);
        mDataSource.addBookmark(params.mode, params.word);
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
