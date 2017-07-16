package com.paperplanes.mykamus.presentation.presenters;

import android.util.Log;

import com.paperplanes.mykamus.commons.Speaker;
import com.paperplanes.mykamus.config.Config;
import com.paperplanes.mykamus.domain.model.TranslationMode;
import com.paperplanes.mykamus.domain.model.Word;
import com.paperplanes.mykamus.domain.usecases.AddBookmarkUseCase;
import com.paperplanes.mykamus.domain.usecases.GetBookmarksUseCase;
import com.paperplanes.mykamus.domain.usecases.RemoveBookmarkUseCase;
import com.paperplanes.mykamus.domain.usecases.SearchWordUseCase;
import com.paperplanes.mykamus.domain.usecases.UseCase;
import com.paperplanes.mykamus.domain.usecases.UseCaseExecutor;
import com.paperplanes.mykamus.presentation.views.SearchView;

import javax.inject.Inject;

/**
 * Created by abdularis on 15/07/17.
 */

public class SearchPresenter {

    private SearchView mView;
    private UseCaseExecutor mCaseExecutor;
    private SearchWordUseCase mSearchWordUseCase;
    private GetBookmarksUseCase mGetBookmarksUseCase;
    private AddBookmarkUseCase mAddBookmarkUseCase;
    private RemoveBookmarkUseCase mRemoveBookmarkUseCase;
    private Speaker mSpeaker;
    private TranslationMode mCurrTransMode;
    private String mLastKeyword;
    private Config mConfig;

    @Inject
    public SearchPresenter(UseCaseExecutor caseExecutor,
                           SearchWordUseCase searchWordUseCase,
                           GetBookmarksUseCase getBookmarksUseCase,
                           AddBookmarkUseCase addBookmarkUseCase,
                           RemoveBookmarkUseCase removeBookmarkUseCase,
                           Speaker speaker,
                           Config config) {
        mCaseExecutor = caseExecutor;
        mSearchWordUseCase = searchWordUseCase;
        mGetBookmarksUseCase = getBookmarksUseCase;
        mAddBookmarkUseCase = addBookmarkUseCase;
        mRemoveBookmarkUseCase = removeBookmarkUseCase;
        mSpeaker = speaker;
        mCurrTransMode = config.getTranslationMode();
        mConfig = config;
        mLastKeyword = "";
        mView = null;
    }

    public void setView(SearchView view) {
        mView = view;
    }

    public void search(String keyword) {
        if (keyword.isEmpty()) {
            mView.clearResult();
            mLastKeyword = "";
            return;
        }
        mLastKeyword = keyword;
        mSearchWordUseCase.setParams(new SearchWordUseCase.Params(mCurrTransMode, keyword, 60));
        mCaseExecutor.execute(mSearchWordUseCase, new UseCase.Callback<SearchWordUseCase.Result>() {
            @Override
            public void onSuccess(SearchWordUseCase.Result result) {
                if (result.words != null && result.words.size() > 0) {
                    mView.showResult(result.words);
                }
                else {
                    mView.showDataNotFound();
                }
            }

            @Override
            public void onFailed(Throwable err) {
                mView.showErrorMessage(err.getMessage());
            }
        });
    }

    public void switchLang() {
        if (mCurrTransMode == TranslationMode.EN_TO_ID) {
            mCurrTransMode = TranslationMode.ID_TO_EN;
        } else {
            mCurrTransMode = TranslationMode.EN_TO_ID;
        }

        mConfig.setTranslationMode(mCurrTransMode);
        search(mLastKeyword);
    }

    public void clear() {
        mView.clearResult();
        mLastKeyword = "";
    }

    public void onWordSelected(Word word) {
        if (word != null && !word.getWord().isEmpty()) {
            mSpeaker.speak(word.getWord());
        }
    }

    public void toggleBookmark(Word word) {
        if (word.isBookmarked()) {
            mRemoveBookmarkUseCase.setParams(new RemoveBookmarkUseCase.Params(word, mCurrTransMode));
            mCaseExecutor.execute(mRemoveBookmarkUseCase, new UseCase.Callback<RemoveBookmarkUseCase.Result>() {
                @Override
                public void onSuccess(RemoveBookmarkUseCase.Result result) {
                }

                @Override
                public void onFailed(Throwable err) {
                }
            });
        }
        else {
            mAddBookmarkUseCase.setParams(new AddBookmarkUseCase.Params(word, mCurrTransMode));
            mCaseExecutor.execute(mAddBookmarkUseCase, new UseCase.Callback<AddBookmarkUseCase.Result>() {
                @Override
                public void onSuccess(AddBookmarkUseCase.Result result) {
                }

                @Override
                public void onFailed(Throwable err) {
                }
            });
        }
    }

    public TranslationMode getCurrTransMode() {
        return mCurrTransMode;
    }
}
