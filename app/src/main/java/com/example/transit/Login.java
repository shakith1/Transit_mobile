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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button signup;
    TextInputLayout username, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        signup = findViewById(R.id.signup_btn);

        username = findViewById(R.id.login_username);
        pwd = findViewById(R.id.login_pwd);
    }

    public void callSignUpScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUp.class);

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(signup, "transition_signup");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    public void login(View view) {

        // Validate Username and Password
        if (!validateFields()) {
            return;
        }

        // Get data
        final String _username = username.getEditText().getText().toString().trim();
        final String _pwd = pwd.getEditText().getText().toString().trim();

        // Database
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").equalTo(_username);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    username.setError(null);
                    username.setErrorEnabled(false);

                    String systemPassword = dataSnapshot.child(_username).child("password").getValue(String.class);
                    if (systemPassword.equals(_pwd)) {
                        pwd.setError(null);
                        pwd.setErrorEnabled(false);

                        String _fullName = dataSnapshot.child(_username).child("fullName").getValue(String.class);
                        String _passport = dataSnapshot.child(_username).child("passport").getValue(String.class);
                        String _email = dataSnapshot.child(_username).child("email").getValue(String.class);
                        String _date = dataSnapshot.child(_username).child("date").getValue(String.class);
                        String _gender = dataSnapshot.child(_username).child("gender").getValue(String.class);
                        String _phoneNo = dataSnapshot.child(_username).child("phoneNo").getValue(String.class);

                        Toast.makeText(Login.this, _fullName, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Login.this, "Password does not match!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "No such user exists!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Login.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateFields() {

        String _username = username.getEditText().getText().toString().trim();
        String _pwd = pwd.getEditText().getText().toString().trim();

        if (_username.isEmpty()) {
            username.setError("Username can not be empty");
            username.requestFocus();
            return false;
        } else if (_pwd.isEmpty()) {
            pwd.setError("Password can not be empty");
            pwd.requestFocus();
            return false;
        } else {
            return true;
        }
    }
}