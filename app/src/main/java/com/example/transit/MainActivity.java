package com.example.transit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.transit.Common.OnBoarding;
import com.example.transit.Databases.SessionManager;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    Animation topAnimation, bottomAnimation;
    ImageView image;
    TextView logo, tag;

    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SessionManager sessionManager = new SessionManager(MainActivity.this, SessionManager.SESSION_USERSESSION);
        if (sessionManager.checkLogin()) {
            Intent intent = new Intent(MainActivity.this, Dashboard.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_main);

            topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
            bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

            image = findViewById(R.id.imageView);
            logo = findViewById(R.id.textView4);
            tag = findViewById(R.id.textView5);

            image.setAnimation(topAnimation);
            logo.setAnimation(bottomAnimation);
            tag.setAnimation(bottomAnimation);

            new Handler().postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {

                    onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
                    boolean isFirstTime = onBoardingScreen.getBoolean("firstTime", true);

                    if (isFirstTime) {

                        SharedPreferences.Editor editor = onBoardingScreen.edit();
                        editor.putBoolean("firstTime", false);
                        editor.commit();

                        Intent intent = new Intent(MainActivity.this, OnBoarding.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Intent intent = new Intent(MainActivity.this, Login.class);

                        Pair[] pairs = new Pair[2];
                        pairs[0] = new Pair<View, String>(image, "logo");
                        pairs[1] = new Pair<View, String>(logo, "logo_text");

                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                        startActivity(intent, options.toBundle());
                        finish();
                    }
                }
            }, SPLASH_SCREEN);
        }
    }
}