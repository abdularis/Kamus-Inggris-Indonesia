package com.paperplanes.mykamus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class DictionaryActivity extends AppCompatActivity implements
        TextWatcher, AdapterView.OnItemClickListener, Dictionary.SearchResultListener {

    private Dictionary mDictionary;
    private WordAdapter mWAdapter;
    private Speaker mSpeaker;

    private TextView mMsgText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        View switchBtn = findViewById(R.id.switchBtn);
        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLanguage();
            }
        });

        mSpeaker = new Speaker(this);

        mMsgText = (TextView) findViewById(R.id.msgTextView);

        mDictionary = Dictionary.getInstance(this);
        mDictionary.open();

        mWAdapter = new WordAdapter(this, R.layout.word_row_item);
        mDictionary.addSearchResultListener(mWAdapter);
        mDictionary.addSearchResultListener(this);

        ListView lv = (ListView) findViewById(R.id.resultListView);
        lv.setAdapter(mWAdapter);
        lv.setOnItemClickListener(this);

        final EditText keywordText = (EditText) findViewById(R.id.keywordEditText);
        keywordText.addTextChangedListener(this);

        View clearKeywordBtn = findViewById(R.id.clearKeywordButton);
        clearKeywordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keywordText.setText("");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSpeaker.close();
        mDictionary.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dictionary_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                goToAbout();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchLanguage() {
        TextView t1 = (TextView) findViewById(R.id.langLeft);
        TextView t2 = (TextView) findViewById(R.id.langRight);

        if (mDictionary.getTranslationType() == Dictionary.TranslationType.EN_TO_ID) {
            mDictionary.setTranslationType(Dictionary.TranslationType.ID_TO_EN);
            t1.setText(getResources().getText(R.string.lang_id));
            t2.setText(getResources().getText(R.string.lang_en));
        } else {
            mDictionary.setTranslationType(Dictionary.TranslationType.EN_TO_ID);
            t1.setText(getResources().getText(R.string.lang_en));
            t2.setText(getResources().getText(R.string.lang_id));
        }
    }

    private void goToAbout() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    @Override
    public void afterTextChanged(Editable editable) {}

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i1, int i2, int i3) {
        List<Word> res = mDictionary.search(charSequence.toString().trim());
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Word w = mWAdapter.getWord(i);
        if (w != null) {
            mSpeaker.speak(w.getWord());
            mSpeaker.pause(1000);
        }
    }
}
