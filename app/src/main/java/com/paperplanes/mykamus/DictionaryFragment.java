package com.paperplanes.mykamus;


import android.content.Context;
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

import java.util.List;


public class DictionaryFragment extends Fragment
        implements AdapterView.OnItemClickListener, Dictionary.SearchResultListener {

    private Dictionary mDictionary;
    private WordAdapter mWAdapter;
    private Speaker mSpeaker;
    private TextView mMsgText;

    public DictionaryFragment() {
    }

    public Dictionary.TranslationType getTranslationType() {
        return mDictionary.getTranslationType();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSpeaker.close();
        mDictionary.close();
    }

    public void switchDictionaryLang() {
        if (mDictionary.getTranslationType() == Dictionary.TranslationType.EN_TO_ID) {
            mDictionary.setTranslationType(Dictionary.TranslationType.ID_TO_EN);
        } else {
            mDictionary.setTranslationType(Dictionary.TranslationType.EN_TO_ID);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);

        mSpeaker = new Speaker(getActivity());
        mDictionary = Dictionary.getInstance(getActivity());
        mDictionary.open();
        mWAdapter = new WordAdapter(getActivity(), R.layout.item_word);
        mDictionary.addSearchResultListener(mWAdapter);
        mDictionary.addSearchResultListener(this);

        mMsgText = (TextView) view.findViewById(R.id.text_msg);
        ListView listView = (ListView) view.findViewById(R.id.list_result);
        listView.setOnItemClickListener(this);
        listView.setAdapter(mWAdapter);

        final EditText keywordText = (EditText) view.findViewById(R.id.edit_search_keyword);
        keywordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDictionary.search(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        View clearKeywordBtn = view.findViewById(R.id.clearKeywordButton);
        clearKeywordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keywordText.setText("");
            }
        });

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Word w = mWAdapter.getWord(position);
        if (w != null) {
            mSpeaker.speak(w.getWord());
            mSpeaker.pause(1000);
        }
    }

    @Override
    public void onFound(List<Word> results) {
        mMsgText.setVisibility(View.GONE);
    }

    @Override
    public void onNotFound(String keyword) {
        String msg = "\"" + keyword + "\" " + getResources().getText(R.string.not_found);
        mMsgText.setText(msg);
        mMsgText.setVisibility(View.VISIBLE);
    }
}
