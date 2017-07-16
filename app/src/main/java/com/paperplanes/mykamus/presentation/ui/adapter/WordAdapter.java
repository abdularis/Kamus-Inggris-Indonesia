package com.paperplanes.mykamus.presentation.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paperplanes.mykamus.R;
import com.paperplanes.mykamus.domain.model.Word;

/**
 * Created by abdularis on 28/03/17.
 */

public class WordAdapter extends ArrayAdapter<Word> {

    private OnBookmarkItemClickListener mBookmarkClickedListener;
    private int mResId;

    public WordAdapter(Context context, int resId) {
        super(context, resId);
        mResId = resId;
    }

    public void setBookmarkClickedListener(OnBookmarkItemClickListener bookmarkClickedListener) {
        mBookmarkClickedListener = bookmarkClickedListener;
    }

    @NonNull
    @Override
    public View getView(int pos, View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        RowHolder holder;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(mResId, parent, false);

            holder = new RowHolder();
            holder.wordTextView = (TextView) row.findViewById(R.id.text_item_word);
            holder.transTextView = (TextView) row.findViewById(R.id.text_item_translation);
            holder.imgBookmark = (ImageView) row.findViewById(R.id.img_bookmark);

            row.setTag(holder);
        }

        holder = (RowHolder) row.getTag();

        Word w = getItem(pos);

        if (w != null) {
            setHolderWithWord(holder, w);
        }

        return row;
    }

    private void setHolderWithWord(RowHolder holder, Word w) {
        holder.wordTextView.setText(w.getWord());
        holder.transTextView.setText(w.getTrans());
        if (w.isBookmarked()) {
            holder.imgBookmark.setImageResource(R.drawable.ic_bookmarked);
        }
        else {
            holder.imgBookmark.setImageResource(R.drawable.ic_bookmark_clear);
        }

        if (holder.listener == null) {
            holder.listener = new BookmarkButtonClickListener(holder, w);
            holder.imgBookmark.setOnClickListener(holder.listener);
        }
        else {
            holder.listener.word = w;
        }
    }

    public interface OnBookmarkItemClickListener {

        void onBookmarkItemClick(Word clickedWord);

    }

    private class RowHolder {
        TextView wordTextView;
        TextView transTextView;
        ImageView imgBookmark;
        BookmarkButtonClickListener listener;
    }

    private class BookmarkButtonClickListener implements View.OnClickListener {

        RowHolder holder;
        Word word;

        BookmarkButtonClickListener(RowHolder holder, Word w) {
            this.holder = holder;
            this.word = w;
        }

        @Override
        public void onClick(View v) {
            if (!word.isBookmarked()) {
                holder.imgBookmark.setImageResource(R.drawable.ic_bookmarked);
            }
            else {
                holder.imgBookmark.setImageResource(R.drawable.ic_bookmark_clear);
            }
            if (mBookmarkClickedListener != null) {
                mBookmarkClickedListener.onBookmarkItemClick(word);
            }
        }
    }
}
