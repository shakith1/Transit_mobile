package com.example.transit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    ImageView back, image;
    TextView title, title_text;
    Button next, login;

    RelativeLayout progressBar;

    TextInputLayout fullName, username, passport, email, pwd;

    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        back = findViewById(R.id.signup_back);
        title = findViewById(R.id.signup_title);
        next = findViewById(R.id.signup_next_btn);
        login = findViewById(R.id.signup_login_btn);
        image = findViewById(R.id.imageView);
        title_text = findViewById(R.id.title_text);

        fullName = findViewById(R.id.signup_fullname);
        username = findViewById(R.id.signup_username);
        passport = findViewById(R.id.signup_id_passport);
        email = findViewById(R.id.signup_email);
        pwd = findViewById(R.id.signup_pwd);

        progressBar = findViewById(R.id.signup_progress_bar);
    }

    public void callNextSignupScreen(View view) {

        user = username.getEditText().getText().toString().trim();

        if (!validateFullName() | !validateUsername(user) | !validateEmail() | !validatePassword()) {
            return;
        }

        String _username = username.getEditText().getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").equalTo(_username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUp.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(getApplicationContext(), SignUpSecond.class);

                    intent.putExtra("fullName", fullName.getEditText().getText().toString());
                    intent.putExtra("userName", username.getEditText().getText().toString());
                    intent.putExtra("passport", passport.getEditText().getText().toString());
                    intent.putExtra("email", email.getEditText().getText().toString());
                    intent.putExtra("pwd", pwd.getEditText().getText().toString());

                    Pair[] pairs = new Pair[3];
                    pairs[0] = new Pair<View, String>(title, "transition_title");
                    pairs[1] = new Pair<View, String>(next, "transition_next");
                    pairs[2] = new Pair<View, String>(login, "transition_login");

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);
                        startActivity(intent, options.toBundle());
                        finish();
                    } else {
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // Validation Functions

    private boolean validateFullName() {
        String value = fullName.getEditText().getText().toString().trim();

        if (value.isEmpty()) {
            fullName.setError("Full Name cannot be empty");
            return false;
        } else {
            fullName.setError(null);
            fullName.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateUsername(String value) {

        String checkSpaces = "\\A\\w{4,20}\\z";

        if (value.isEmpty()) {
            username.setError("Username cannot be empty");
            return false;
        } else if (value.length() > 20) {
            username.setError("Username is too large!");
            return false;
        } else if (value.length() < 4) {
            username.setError("Username is too small!");
            return false;
        } else if (!value.matches(checkSpaces)) {
            username.setError("No white spaces are allowed.");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String value = email.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (value.isEmpty()) {
            email.setError("Username cannot be empty");
            return false;
        } else if (!value.matches(checkEmail)) {
            email.setError("Invalid Emai!.");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String value = pwd.getEditText().getText().toString().trim();
        String checkPwd = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (value.isEmpty()) {
            pwd.setError("Password cannot be empty");
            return false;
        } else if (!value.matches(checkPwd)) {
            pwd.setError("Password should contain 4 characters.");
            return false;
        } else {
            pwd.setError(null);
            pwd.setErrorEnabled(false);
            return true;
        }
    }

}