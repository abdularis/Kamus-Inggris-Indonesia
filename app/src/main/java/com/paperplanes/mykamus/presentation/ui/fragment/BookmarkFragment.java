package com.paperplanes.mykamus.presentation.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.paperplanes.mykamus.DictionaryApp;
import com.paperplanes.mykamus.R;
import com.paperplanes.mykamus.domain.model.TranslationMode;
import com.paperplanes.mykamus.domain.model.Word;
import com.paperplanes.mykamus.presentation.presenters.BookmarkPresenter;
import com.paperplanes.mykamus.presentation.ui.adapter.WordAdapter;
import com.paperplanes.mykamus.presentation.views.BookmarksView;

import java.util.List;

import javax.inject.Inject;


public class BookmarkFragment extends Fragment implements BookmarksView, WordAdapter.OnBookmarkItemClickListener {
    private static final String KEY_TRANS_MODE = "TranslationMode";

    @Inject
    BookmarkPresenter mPresenter;

    private ListView mListView;
    private TextView mTextMsg;
    private WordAdapter mWordAdapter;
    private TranslationMode mCurrTransMode;

    public BookmarkFragment() {
    }

    public static BookmarkFragment newInstance(TranslationMode tMode) {
        BookmarkFragment frag = new BookmarkFragment();

        Bundle args = new Bundle();
        args.putString(KEY_TRANS_MODE, tMode.toString());
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        ((DictionaryApp) getActivity().getApplication()).getAppComponent().inject(this);
        mPresenter.setView(this);

        mWordAdapter = new WordAdapter(getActivity(), R.layout.item_word);
        mWordAdapter.setBookmarkClickedListener(this);
        mListView = (ListView) view.findViewById(R.id.bookmark_list);
        mListView.setAdapter(mWordAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.onWordSelected(mWordAdapter.getItem(position));
            }
        });

        mTextMsg = (TextView) view.findViewById(R.id.text_msg);

        if (getArguments() != null) {
            String strTransMode =
                    getArguments().getString(KEY_TRANS_MODE, TranslationMode.values()[0].toString());
            mCurrTransMode = TranslationMode.valueOf(strTransMode);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.loadData(mCurrTransMode);
    }

    @Override
    public void showWords(List<Word> words) {
        showListView();
        mWordAdapter.clear();
        mWordAdapter.addAll(words);
    }

    @Override
    public void showNoData() {
        showTextMessage();
        mTextMsg.setText(R.string.no_data);
    }

    @Override
    public void clearWords() {
        mWordAdapter.clear();
        showNoData();
    }

    @Override
    public void removeWord(Word word) {
        mWordAdapter.remove(word);
        if (mWordAdapter.getCount() <= 0)
            showNoData();
    }

    @Override
    public void onBookmarkItemClick(Word clickedWord) {
        mPresenter.removeBookmark(clickedWord, mCurrTransMode);
    }

    private void showTextMessage() {
        mListView.setVisibility(View.GONE);
        mTextMsg.setVisibility(View.VISIBLE);
    }

    private void showListView() {
        mListView.setVisibility(View.VISIBLE);
        mTextMsg.setVisibility(View.GONE);
    }
}
