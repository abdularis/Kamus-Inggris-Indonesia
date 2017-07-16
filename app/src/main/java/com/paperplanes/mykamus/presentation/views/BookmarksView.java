package com.paperplanes.mykamus.presentation.views;

import com.paperplanes.mykamus.domain.model.Word;

import java.util.List;

/**
 * Created by abdularis on 16/07/17.
 */

public interface BookmarksView {

    void showWords(List<Word> words);

    void showNoData();

    void clearWords();

    void removeWord(Word word);

}
