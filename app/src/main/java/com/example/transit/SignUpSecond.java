package com.example.transit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class SignUpSecond extends AppCompatActivity {

    ImageView back, image;
    TextView title, title_text;
    Button next, login;

    RadioGroup radioGroup;
    RadioButton gender;
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_second);

        back = findViewById(R.id.signup_back);
        title = findViewById(R.id.signup_title);
        next = findViewById(R.id.signup_next_btn);
        login = findViewById(R.id.signup_login_btn);
        image = findViewById(R.id.imageView);
        title_text = findViewById(R.id.title_text);

        radioGroup = findViewById(R.id.radio_group);
        datePicker = findViewById(R.id.age_picker);
    }

    public void callNextSignupScreen(View view) {

        if (!validateGender() | !validateAge()) {
            return;
        }

        gender = findViewById(radioGroup.getCheckedRadioButtonId());
        String selectedGender = gender.getText().toString();

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        String date = day + "/" + month + "/" + year;

        String fullName = getIntent().getStringExtra("fullName");
        String username = getIntent().getStringExtra("userName");
        String passport = getIntent().getStringExtra("passport");
        String email = getIntent().getStringExtra("email");
        String pwd = getIntent().getStringExtra("pwd");

        Intent intent = new Intent(getApplicationContext(), SignupThird.class);

        intent.putExtra("fullName", fullName);
        intent.putExtra("username", username);
        intent.putExtra("passport", passport);
        intent.putExtra("email", email);
        intent.putExtra("pwd", pwd);
        intent.putExtra("gender", selectedGender);
        intent.putExtra("date", date);

        Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(title, "transition_title");
        pairs[1] = new Pair<View, String>(next, "transition_next");
        pairs[2] = new Pair<View, String>(login, "transition_login");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpSecond.this, pairs);
            startActivity(intent, options.toBundle());
            finish();
        } else {
            startActivity(intent);
            finish();
        }
    }

    public void back(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUp.class);

        Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(title, "transition_title");
        pairs[1] = new Pair<View, String>(next, "transition_next");
        pairs[2] = new Pair<View, String>(login, "transition_login");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpSecond.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    private boolean validateGender() {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select Gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAge() {
        int cureentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        int isAgeValid = cureentYear - userAge;

        if (isAgeValid < 16) {
            Toast.makeText(this, "You are not eligible to use this service.", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }
}