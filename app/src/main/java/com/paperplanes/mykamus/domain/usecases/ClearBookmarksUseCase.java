package com.paperplanes.mykamus.domain.usecases;

import com.paperplanes.mykamus.domain.data.DictionaryDataSource;
import com.paperplanes.mykamus.domain.model.TranslationMode;
import com.paperplanes.mykamus.domain.model.Word;

import javax.inject.Inject;

/**
 * Created by abdularis on 16/07/17.
 */

public class ClearBookmarksUseCase extends UseCase<ClearBookmarksUseCase.Params, ClearBookmarksUseCase.Result> {

    private DictionaryDataSource mDataSource;

    @Inject
    public ClearBookmarksUseCase(DictionaryDataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    protected void execute(Params params) {
        mDataSource.clearBookmark(params.mode);
        getCallback().onSuccess(new Result());
    }

    public static class Params implements UseCase.Params {
        TranslationMode mode;

        public Params(TranslationMode mode) {
            this.mode = mode;
        }
    }

    public static class Result implements UseCase.Result {}
}
