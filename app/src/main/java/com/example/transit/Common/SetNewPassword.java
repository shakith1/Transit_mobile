package com.example.transit.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transit.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetNewPassword extends AppCompatActivity {

    Button ok;
    ImageView imageView;
    TextView title, desc;
    TextInputLayout pwd, newPwd;
    String value_pwd, value_new_pwd;

    Animation bottomAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_set_new_password);

        ok = findViewById(R.id.credentials_ok);
        pwd = findViewById(R.id.credentials_pwd);
        newPwd = findViewById(R.id.credentials_new_pwd);

        imageView = findViewById(R.id.credentials_logo);
        title = findViewById(R.id.credentials_title);
        desc = findViewById(R.id.credentials_desc);

        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        imageView.setAnimation(bottomAnimation);
        title.setAnimation(bottomAnimation);
        desc.setAnimation(bottomAnimation);
        pwd.setAnimation(bottomAnimation);
        newPwd.setAnimation(bottomAnimation);
        ok.setAnimation(bottomAnimation);
    }

    public void setNewPassword(View view) {

        if (!validatePassword() | !validateConfirmPassword()) {
            return;
        }

        String username = getIntent().getStringExtra("username");
        String password = pwd.getEditText().getText().toString().trim();

        //Update Data in the DB
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(username).child("password").setValue(password);

        startActivity(new Intent(getApplicationContext(), ForgetPasswordSuccess.class));
        finish();
    }

    private boolean validatePassword() {
        value_pwd = pwd.getEditText().getText().toString().trim();
        value_new_pwd = newPwd.getEditText().getText().toString().trim();
        String checkPwd =  "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (value_pwd.isEmpty() && value_new_pwd.isEmpty()) {
            pwd.setError("Password cannot be empty");
            newPwd.setError("Confirm Password cannot be empty");
            return false;
        } else if (value_pwd.isEmpty()) {
            pwd.setError("Password cannot be empty");
            return false;
        } else if (value_new_pwd.isEmpty()) {
            newPwd.setError("Confirm Password cannot be empty");
            return false;
        }else if (!value_pwd.matches(checkPwd) && !value_new_pwd.matches(checkPwd)) {
            pwd.setError("Password should contain 4 characters.");
            return false;
        }
        else {
            pwd.setError(null);
            newPwd.setError(null);
            pwd.setErrorEnabled(false);
            newPwd.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        if (value_pwd.equals(value_new_pwd))
            return true;
        else {
            Toast.makeText(this, "Passwords did not match", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}