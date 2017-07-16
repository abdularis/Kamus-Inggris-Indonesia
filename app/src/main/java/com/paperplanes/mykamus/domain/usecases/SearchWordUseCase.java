package com.paperplanes.mykamus.domain.usecases;

import com.paperplanes.mykamus.domain.data.DictionaryDataSource;
import com.paperplanes.mykamus.domain.model.TranslationMode;
import com.paperplanes.mykamus.domain.model.Word;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by abdularis on 15/07/17.
 */

public class SearchWordUseCase extends UseCase<SearchWordUseCase.Params, SearchWordUseCase.Result> {

    private DictionaryDataSource mDataSource;
    private boolean mQuerying;

    @Inject
    public SearchWordUseCase(DictionaryDataSource dataSource) {
        mDataSource = dataSource;
        mQuerying = false;
    }

    @Override
    protected void execute(final Params params) {
        if (mQuerying) return;

        mQuerying = true;
        mDataSource.searchWord(params.mode, params.keyword, params.limit, new DictionaryDataSource.GetCallback() {
            @Override
            public void onWordLoaded(List<Word> words) {
                mQuerying = false;
                sortResult(params.keyword, words);
                getCallback().onSuccess(new Result(words));
            }

            @Override
            public void onWordNotAvailable() {
                mQuerying = false;
                getCallback().onSuccess(new Result(null));
            }

            @Override
            public void onError(Throwable err) {
                mQuerying = false;
                getCallback().onFailed(err);
            }
        });
    }

    private void sortResult(String keyword, List<Word> result) {
        Word tmp, tmp2;

        char pref = keyword.charAt(0);
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

    public static class Params implements UseCase.Params {
        public TranslationMode mode;
        public String keyword;
        public int limit;

        public Params(TranslationMode tMode, String keyword, int limit) {
            this.mode = tMode;
            this.keyword = keyword;
            this.limit = limit;
        }
    }

    public static class Result implements UseCase.Result {
        public List<Word> words;

        public Result(List<Word> words) {
            this.words = words;
        }
    }
}
