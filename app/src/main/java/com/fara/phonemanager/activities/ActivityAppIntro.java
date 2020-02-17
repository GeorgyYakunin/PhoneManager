package com.fara.phonemanager.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fara.phonemanager.R;
import com.fara.phonemanager.fragments.IntroFirstFragment;
import com.fara.phonemanager.fragments.IntroFourthFragment;
import com.fara.phonemanager.fragments.IntroSecondFragment;
import com.fara.phonemanager.fragments.IntroThirdFragment;
import com.fara.phonemanager.helpers.ActivitiesHelpers;
import com.github.paolorotolo.appintro.AppIntro;

/*
* This is welcome app tour activity that is showing major features of the app. Intro Activity is started
* when the app is launched for the first time. Intro Apps is looks like slider where user can swipe through
* few slides before getting into app.
 * */
public class ActivityAppIntro extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(new IntroFirstFragment());
        addSlide(new IntroSecondFragment());
        addSlide(new IntroThirdFragment());

        showSkipButton(false);

        setDoneText(getResources().getString(R.string.activity_intro_button_done));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        ActivitiesHelpers.getInstance(ActivityAppIntro.this).gotoActivityMain();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        ActivitiesHelpers.getInstance(ActivityAppIntro.this).gotoActivityMain();
    }

    /*
    * To change the color of status bar & navigation bar in accordance with slider background.
    * */
    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);

        if (newFragment instanceof IntroFirstFragment) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.parseColor("#820010"));
                getWindow().setNavigationBarColor(Color.parseColor("#820010"));
            }
        } else if (newFragment instanceof IntroSecondFragment) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.parseColor("#113ea4"));
                getWindow().setNavigationBarColor(Color.parseColor("#113ea4"));
            }
        } else if (newFragment instanceof IntroThirdFragment) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
                getWindow().setNavigationBarColor(Color.parseColor("#000000"));
            }
        } else if (newFragment instanceof IntroFourthFragment) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.parseColor("#600072"));
                getWindow().setNavigationBarColor(Color.parseColor("#600072"));
            }
        }
    }
}