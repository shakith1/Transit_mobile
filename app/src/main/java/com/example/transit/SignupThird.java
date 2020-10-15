package com.example.transit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class SignupThird extends AppCompatActivity {

    TextView title;
    Button next,login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup_third);

        next = findViewById(R.id.signup_next_btn);
        title = findViewById(R.id.signup_title);
        login = findViewById(R.id.signup_login_btn);
    }

    public void callOTP(View view) {

        String fullName = getIntent().getStringExtra("fullName");
        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");
        String pwd = getIntent().getStringExtra("pwd");
        String gender = getIntent().getStringExtra("gender");
        String date = getIntent().getStringExtra("date");

        Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(next, "transition_next");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignupThird.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    public void back(View view){
        Intent intent = new Intent(getApplicationContext(), SignUpSecond.class);

        Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(title, "transition_title");
        pairs[1] = new Pair<View, String>(next, "transition_next");
        pairs[2] = new Pair<View, String>(login, "transition_login");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignupThird.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }
}