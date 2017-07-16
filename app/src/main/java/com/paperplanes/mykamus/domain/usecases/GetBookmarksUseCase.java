package com.paperplanes.mykamus.domain.usecases;

import com.paperplanes.mykamus.domain.data.DictionaryDataSource;
import com.paperplanes.mykamus.domain.model.TranslationMode;
import com.paperplanes.mykamus.domain.model.Word;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by abdularis on 16/07/17.
 */

public class GetBookmarksUseCase extends UseCase<GetBookmarksUseCase.Params, GetBookmarksUseCase.Result> {

    private DictionaryDataSource mDataSource;

    @Inject
    public GetBookmarksUseCase(DictionaryDataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    protected void execute(Params params) {
        mDataSource.getBookmark(params.mode, new DictionaryDataSource.GetCallback() {
            @Override
            public void onWordLoaded(List<Word> words) {
                getCallback().onSuccess(new Result(words));
            }

            @Override
            public void onWordNotAvailable() {
                getCallback().onSuccess(new Result(null));
            }

            @Override
            public void onError(Throwable err) {
                getCallback().onFailed(err);
            }
        });
    }

    public static class Params implements UseCase.Params {
        public TranslationMode mode;

        public Params(TranslationMode mode) {
            this.mode = mode;
        }
    }

    public static class Result implements UseCase.Result {
        public List<Word> words;

        public Result(List<Word> words) {
            this.words = words;
        }
    }
}
