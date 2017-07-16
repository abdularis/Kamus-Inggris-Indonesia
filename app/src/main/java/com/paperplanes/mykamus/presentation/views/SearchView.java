package com.paperplanes.mykamus.presentation.views;

import com.paperplanes.mykamus.domain.model.Word;

import java.util.List;

/**
 * Created by abdularis on 15/07/17.
 */

public interface SearchView {

    void showResult(List<Word> words);

    void showDataNotFound();

    void showErrorMessage(String msg);

    void clearResult();

}
