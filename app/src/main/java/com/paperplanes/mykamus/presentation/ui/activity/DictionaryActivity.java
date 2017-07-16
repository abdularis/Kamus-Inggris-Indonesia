package com.paperplanes.mykamus.presentation.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.paperplanes.mykamus.presentation.ui.fragment.BookmarkFragmentContainer;
import com.paperplanes.mykamus.R;
import com.paperplanes.mykamus.domain.model.TranslationMode;
import com.paperplanes.mykamus.presentation.ui.fragment.DictionaryFragment;

public class DictionaryActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private DictionaryFragment mDictFragment;
    private BookmarkFragmentContainer mBookmarkFrag;
    private ActionBarDrawerToggle toggle;

    private ImageView switchBtn;
    private View mLangSwitcherView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        enableToolbarTitle(false);

        mLangSwitcherView = findViewById(R.id.switcher_layout);
        mLangSwitcherView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLanguage();
            }
        });

        mDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        NavigationView navView = (NavigationView) findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectNavDrawerItem(item);
                return true;
            }
        });

        mBookmarkFrag = new BookmarkFragmentContainer();
        mDictFragment = new DictionaryFragment();
        selectNavDrawerItem(navView.getMenu().getItem(0));

        toggle =
                new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.open_drawer, R.string.close_drawer);

        mDrawer.addDrawerListener(toggle);
        mDrawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideSoftKeyboard();
            }
        });

        TextView l1 = (TextView) findViewById(R.id.langLeft);
        TextView l2 = (TextView) findViewById(R.id.langRight);
        switchBtn = (ImageView) findViewById(R.id.switchBtn); // left to right arrow -->

        l1.setText(R.string.lang_en);
        l2.setText(R.string.lang_id);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
        syncLangSwitcher();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawers();
        }
        else {
            super.onBackPressed();
        }
    }

    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void enableToolbarTitle(boolean enable) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(enable);
    }

    private void switchLanguage() {
        mDictFragment.switchLang();
        syncLangSwitcher();
    }

    private void syncLangSwitcher() {
        float rotation = mDictFragment.getTranslationMode() == TranslationMode.EN_TO_ID
                ? 0 : 180;

        switchBtn.animate()
                .rotation(rotation)
                .withLayer()
                .setDuration(200)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    private void goToDictionary() {
        mLangSwitcherView.setVisibility(View.VISIBLE);
        enableToolbarTitle(false);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragmentContainer, mDictFragment)
                .commit();
    }

    private void goToBookmark() {
        mLangSwitcherView.setVisibility(View.GONE);
        enableToolbarTitle(true);
        setTitle(getResources().getString(R.string.bookmark));

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragmentContainer, mBookmarkFrag)
                .commit();
    }

    private void goToAbout() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void selectNavDrawerItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_item_dictionary :
                item.setChecked(true);
                goToDictionary();
                break;
            case R.id.nav_item_bookmark :
                item.setChecked(true);
                goToBookmark();
                break;
            case R.id.nav_item_about :
                goToAbout();
                break;
        }

        mDrawer.closeDrawers();
    }
}
