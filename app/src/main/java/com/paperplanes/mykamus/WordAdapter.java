package com.paperplanes.mykamus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by abdularis on 28/03/17.
 */

public class WordAdapter extends ArrayAdapter<Word>
        implements Dictionary.SearchResultListener {

    private Context mContext;
    private int mResId;
    private List<Word> mWords;

    public WordAdapter(Context context, int resId) {
        super(context, resId);

        mContext = context;
        mResId = resId;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        View row = convertView;
        RowHolder holder;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(mResId, parent, false);

            holder = new RowHolder();
            holder.wordTextView = (TextView) row.findViewById(R.id.text_item_word);
            holder.transTextView = (TextView) row.findViewById(R.id.text_item_translation);

            row.setTag(holder);
        }

        holder = (RowHolder) row.getTag();

        Word w = mWords.get(pos);

        holder.wordTextView.setText(w.getWord());
        holder.transTextView.setText(w.getTrans());

        return row;
    }

    @Override
    public void onFound(List<Word> results) {
        this.clear();
        this.addAll(results);
        mWords = results;
    }

    @Override
    public void onNotFound(String keyword) {
        this.clear();
    }

    public Word getWord(int i) {
        if (i >= 0 && i < mWords.size())
            return mWords.get(i);
        return null;
    }


    private class RowHolder {
        TextView wordTextView;
        TextView transTextView;
    }
}
