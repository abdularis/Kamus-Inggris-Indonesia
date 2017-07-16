package com.paperplanes.mykamus.presentation.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.paperplanes.mykamus.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView tv = (TextView) findViewById(R.id.github_link);
        tv.setText(
                Html.fromHtml("<a href=\"https://github.com/abdularis/Kamus-Inggris-Indonesia\">" +
                        "https://github.com</a>"));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
