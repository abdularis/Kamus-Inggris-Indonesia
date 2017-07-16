package com.paperplanes.mykamus.presentation.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.paperplanes.mykamus.DictionaryApp;
import com.paperplanes.mykamus.R;
import com.paperplanes.mykamus.domain.model.TranslationMode;
import com.paperplanes.mykamus.presentation.ui.adapter.WordAdapter;
import com.paperplanes.mykamus.domain.model.Word;
import com.paperplanes.mykamus.presentation.presenters.SearchPresenter;
import com.paperplanes.mykamus.presentation.views.SearchView;

import java.util.List;

import javax.inject.Inject;


public class DictionaryFragment extends Fragment
        implements TextWatcher, AdapterView.OnItemClickListener,
        SearchView, WordAdapter.OnBookmarkItemClickListener {

    @Inject SearchPresenter mPresenter;

    private WordAdapter mWAdapter;
    private TextView mMsgText;
    private ListView mListView;
    private EditText mKeyword;

    public DictionaryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);

        ((DictionaryApp) getActivity().getApplication()).getAppComponent().inject(this);

        mPresenter.setView(this);
        mWAdapter = new WordAdapter(getActivity(), R.layout.item_word);
        mWAdapter.setBookmarkClickedListener(this);

        mMsgText = (TextView) view.findViewById(R.id.text_msg);
        mListView = (ListView) view.findViewById(R.id.list_result);
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(mWAdapter);

        mKeyword = (EditText) view.findViewById(R.id.edit_search_keyword);
        mKeyword.addTextChangedListener(this);

        View clearKeywordBtn = view.findViewById(R.id.clearKeywordButton);
        clearKeywordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.clear();
            }
        });

        return view;
    }

    public TranslationMode getTranslationMode() {
        return mPresenter.getCurrTransMode();
    }

    public void switchLang() {
        mPresenter.switchLang();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mPresenter.onWordSelected(mWAdapter.getItem(position));
    }

    @Override
    public void onBookmarkItemClick(Word clickedWord) {
        mPresenter.toggleBookmark(clickedWord);
    }

    @Override
    public void showResult(List<Word> words) {
        showListView();
        mWAdapter.clear();
        mWAdapter.addAll(words);
    }

    @Override
    public void showDataNotFound() {
        showTextMessage();
        mMsgText.setText(R.string.not_found);
    }

    @Override
    public void showErrorMessage(String msg) {
        showTextMessage();
        mMsgText.setText(msg);
    }

    @Override
    public void clearResult() {
        mKeyword.removeTextChangedListener(this);
        mKeyword.setText("");
        mKeyword.addTextChangedListener(this);
        mWAdapter.clear();
        hideContentView();
    }

    //
    private void showTextMessage() {
        mListView.setVisibility(View.INVISIBLE);
        mMsgText.setVisibility(View.VISIBLE);
    }

    private void showListView() {
        mListView.setVisibility(View.VISIBLE);
        mMsgText.setVisibility(View.INVISIBLE);
    }

    private void hideContentView() {
        mListView.setVisibility(View.INVISIBLE);
        mMsgText.setVisibility(View.INVISIBLE);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mPresenter.search(s.toString().trim());
    }

    @Override
    public void afterTextChanged(Editable s) {}
}
