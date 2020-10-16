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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

public class SignupThird extends AppCompatActivity {

    TextView title;
    Button next, login;
    CountryCodePicker countryCodePicker;
    TextInputLayout phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup_third);

        next = findViewById(R.id.signup_next_btn);
        title = findViewById(R.id.signup_title);
        login = findViewById(R.id.signup_login_btn);

        countryCodePicker = findViewById(R.id.country_code_picker);
        phoneNo = findViewById(R.id.phone_no);
    }

    public void callOTP(View view) {

        if(!validatePhoneNo())
            return;

        String fullName = getIntent().getStringExtra("fullName");
        String username = getIntent().getStringExtra("username");
        String passport = getIntent().getStringExtra("passport");
        String email = getIntent().getStringExtra("email");
        String pwd = getIntent().getStringExtra("pwd");
        String gender = getIntent().getStringExtra("gender");
        String date = getIntent().getStringExtra("date");

        String userEnteredPhoneNo = phoneNo.getEditText().getText().toString().trim();

        // Remove the "0" if user enters 0 at the begining.
        if (userEnteredPhoneNo.charAt(0) == '0') {
            userEnteredPhoneNo = userEnteredPhoneNo.substring(1);
        }

        String country = countryCodePicker.getSelectedCountryName();

        String _phoneNo = "+" + countryCodePicker.getFullNumber() + userEnteredPhoneNo;

        Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);

        // Pass fields for the next activity
        intent.putExtra("fullName", fullName);
        intent.putExtra("username", username);
        intent.putExtra("passport", passport);
        intent.putExtra("email", email);
        intent.putExtra("pwd", pwd);
        intent.putExtra("gender", gender);
        intent.putExtra("date", date);
        intent.putExtra("phoneNo", _phoneNo);
        intent.putExtra("country", country);
        intent.putExtra("whatToDo", "createUser");

        // Add Transition
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(next, "transition_next");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignupThird.this, pairs);
            startActivity(intent, options.toBundle());
            finish();
        } else {
            startActivity(intent);
            finish();
        }
    }

    private boolean validatePhoneNo() {
        if(phoneNo.getEditText().getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Phone No can not be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }

    public void back(View view) {
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