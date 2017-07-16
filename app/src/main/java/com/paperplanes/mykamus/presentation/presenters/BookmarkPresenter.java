package com.paperplanes.mykamus.presentation.presenters;

import com.paperplanes.mykamus.commons.Speaker;
import com.paperplanes.mykamus.domain.model.TranslationMode;
import com.paperplanes.mykamus.domain.model.Word;
import com.paperplanes.mykamus.domain.usecases.ClearBookmarksUseCase;
import com.paperplanes.mykamus.domain.usecases.GetBookmarksUseCase;
import com.paperplanes.mykamus.domain.usecases.RemoveBookmarkUseCase;
import com.paperplanes.mykamus.domain.usecases.UseCase;
import com.paperplanes.mykamus.domain.usecases.UseCaseExecutor;
import com.paperplanes.mykamus.presentation.views.BookmarksView;

import javax.inject.Inject;

/**
 * Created by abdularis on 16/07/17.
 */

public class BookmarkPresenter {

    private BookmarksView mView;
    private UseCaseExecutor mCaseExecutor;
    private GetBookmarksUseCase mGetBookmarksUseCase;
    private RemoveBookmarkUseCase mRemoveBookmarkUseCase;
    private ClearBookmarksUseCase mClearBookmarksUseCase;
    private Speaker mSpeaker;

    @Inject
    public BookmarkPresenter(UseCaseExecutor caseExecutor,
                             GetBookmarksUseCase getBookmarksUseCase,
                             RemoveBookmarkUseCase removeBookmarkUseCase,
                             ClearBookmarksUseCase clearBookmarksUseCase,
                             Speaker speaker) {
        mCaseExecutor = caseExecutor;
        mGetBookmarksUseCase = getBookmarksUseCase;
        mRemoveBookmarkUseCase = removeBookmarkUseCase;
        mClearBookmarksUseCase = clearBookmarksUseCase;
        mSpeaker = speaker;
    }

    public void setView(BookmarksView view) {
        mView = view;
    }

    public void loadData(TranslationMode mode) {
        mGetBookmarksUseCase.setParams(new GetBookmarksUseCase.Params(mode));
        mCaseExecutor.execute(mGetBookmarksUseCase, new UseCase.Callback<GetBookmarksUseCase.Result>() {
            @Override
            public void onSuccess(GetBookmarksUseCase.Result result) {
                if (result.words != null && result.words.size() > 0) {
                    mView.showWords(result.words);
                }
                else {
                    mView.showNoData();
                }
            }

            @Override
            public void onFailed(Throwable err) {}
        });
    }

    public void removeBookmark(final Word word, TranslationMode translationMode) {
        mRemoveBookmarkUseCase.setParams(new RemoveBookmarkUseCase.Params(word, translationMode));
        mCaseExecutor.execute(mRemoveBookmarkUseCase, new UseCase.Callback<RemoveBookmarkUseCase.Result>() {
            @Override
            public void onSuccess(RemoveBookmarkUseCase.Result result) {
                mView.removeWord(word);
            }

            @Override
            public void onFailed(Throwable err) {}
        });
    }

    public void clearBookmarks(TranslationMode translationMode) {
        mClearBookmarksUseCase.setParams(new ClearBookmarksUseCase.Params(translationMode));
        mCaseExecutor.execute(mClearBookmarksUseCase, new UseCase.Callback<ClearBookmarksUseCase.Result>() {
            @Override
            public void onSuccess(ClearBookmarksUseCase.Result result) {
                mView.clearWords();
            }

            @Override
            public void onFailed(Throwable err) {}
        });
    }

    public void onWordSelected(Word word) {
        if (word != null && !word.getWord().isEmpty()) {
            mSpeaker.speak(word.getWord());
        }
    }
}
