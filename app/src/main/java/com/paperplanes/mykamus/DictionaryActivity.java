package com.paperplanes.mykamus;

import android.animation.Animator;
import android.animation.AnimatorInflater;
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
import android.widget.TextView;

public class DictionaryActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private NavigationView mNavViewDrawer;
    private DictionaryFragment mDictFragment;
    private BookmarkFragment mBookmFragment;
    private ActionBarDrawerToggle toggle;

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
        mNavViewDrawer = (NavigationView) findViewById(R.id.navView);
        mNavViewDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectNavDrawerItem(item);
                return true;
            }
        });

        mBookmFragment = new BookmarkFragment();
        mDictFragment = new DictionaryFragment();
        selectNavDrawerItem(mNavViewDrawer.getMenu().getItem(0));

        toggle =
                new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.open_drawer, R.string.close_drawer);

        mDrawer.addDrawerListener(toggle);
    }

    private void enableToolbarTitle(boolean enable) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(enable);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
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

    private void switchLanguage() {
        TextView t1 = (TextView) findViewById(R.id.langLeft);
        TextView t2 = (TextView) findViewById(R.id.langRight);

        if (mDictFragment.getTranslationType() == Dictionary.TranslationType.EN_TO_ID) {
            t1.setText(getResources().getText(R.string.lang_id));
            t2.setText(getResources().getText(R.string.lang_en));
        } else {
            t1.setText(getResources().getText(R.string.lang_en));
            t2.setText(getResources().getText(R.string.lang_id));
        }
        mDictFragment.switchDictionaryLang();

        Animator anim = AnimatorInflater.loadAnimator(this, R.animator.fade_in);
        anim.setTarget(t1);
        anim.start();

        Animator anim2 = AnimatorInflater.loadAnimator(this, R.animator.fade_in);
        anim2.setTarget(t2);
        anim2.start();

        View switchBtn = findViewById(R.id.switchBtn);
        Animator rotAnim = AnimatorInflater.loadAnimator(this, R.animator.rotation);
        rotAnim.setTarget(switchBtn);
        rotAnim.start();
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
                .replace(R.id.fragmentContainer, mBookmFragment)
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
