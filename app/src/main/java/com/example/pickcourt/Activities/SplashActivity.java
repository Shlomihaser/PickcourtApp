package com.example.pickcourt.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import com.example.pickcourt.Activities.Authentication.LoginActivity;
import com.example.pickcourt.R;

/*
    * Need to replace this splash screen with Splash screen API.
    * 1. Remove SplashScreen Activity
    * 2. Follow the steps here: https://www.youtube.com/watch?v=Oy0oXwv3kSc&t=337s
    * NOT Doing it here because it takes a lot of time and more difficult.
    * For now i disabled the warning by Line 24.
 */
@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private AppCompatImageView splash_IMG_logo;
    private AppCompatTextView splash_TITLE_big;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViews();
        startAnimation(splash_IMG_logo, splash_TITLE_big);
    }

    private void findViews() {
        splash_IMG_logo = findViewById(R.id.splash_IMG_logo);
        splash_TITLE_big = findViewById(R.id.splash_TITLE_big);
    }


    private void startAnimation(View logo, View title) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        // Set initial positions and properties
        logo.setY(-height / 2.0f);
        title.setY(height / 2.0f);
        logo.setAlpha(0);
        title.setAlpha(0);

        // Create animations
        ObjectAnimator logoTranslate = ObjectAnimator.ofFloat(logo, "translationY", 0);
        ObjectAnimator titleTranslate = ObjectAnimator.ofFloat(title, "translationY", 0);
        ObjectAnimator logoFadeIn = ObjectAnimator.ofFloat(logo, "alpha", 0, 1);
        ObjectAnimator titleFadeIn = ObjectAnimator.ofFloat(title, "alpha", 0, 1);
        ObjectAnimator logoScaleX = ObjectAnimator.ofFloat(logo, "scaleX", 0.5f, 1f);
        ObjectAnimator logoScaleY = ObjectAnimator.ofFloat(logo, "scaleY", 0.5f, 1f);
        ObjectAnimator titleScaleX = ObjectAnimator.ofFloat(title, "scaleX", 0.5f, 1f);
        ObjectAnimator titleScaleY = ObjectAnimator.ofFloat(title, "scaleY", 0.5f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                logoTranslate, titleTranslate,
                logoFadeIn, titleFadeIn,
                logoScaleX, logoScaleY,
                titleScaleX, titleScaleY
        );

        animatorSet.setDuration(2000);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.addListener(new AnimatorSet.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull android.animation.Animator animation) {}
            @Override
            public void onAnimationEnd(@NonNull android.animation.Animator animation) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onAnimationCancel(@NonNull android.animation.Animator animation) {}
            @Override
            public void onAnimationRepeat(@NonNull android.animation.Animator animation) {}
        });

        animatorSet.start();
    }
}