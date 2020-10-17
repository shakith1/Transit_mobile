package com.example.transit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transit.Common.ForgetPassword;
import com.example.transit.Databases.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    Button signup,forget_pwd;
    TextInputLayout username, pwd;
    TextInputEditText usernameEditText,pwdEditText;
    RelativeLayout progressBar;
    CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        signup = findViewById(R.id.signup_btn);

        username = findViewById(R.id.login_username);
        pwd = findViewById(R.id.login_pwd);
        forget_pwd = findViewById(R.id.forget_pwd);
        progressBar = findViewById(R.id.progress_bar);

        rememberMe = findViewById(R.id.remember_me);

        usernameEditText = findViewById(R.id.login_username_edittext);
        pwdEditText = findViewById(R.id.login_pwd_edittext);

        if (!isConnected(this)) {
            showCustomDialog();
            return;
        }

        // Check weather phone number and password is already in the session
        SessionManager sessionManager = new SessionManager(Login.this,SessionManager.SESSION_REMEMBERME);
        if(sessionManager.checkRememberMe()){
            HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailFromSession();
            usernameEditText.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONUSERNAME));
            pwdEditText.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPASSWORD));
        }

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

    public void callForgetPwd(View view){
        Intent intent = new Intent(getApplicationContext(), ForgetPassword.class);

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(forget_pwd, "forget_pwd");

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

        progressBar.setVisibility(View.VISIBLE);

        // Get data
        final String _username = username.getEditText().getText().toString().trim();
        final String _pwd = pwd.getEditText().getText().toString().trim();

        if(rememberMe.isChecked()){
            SessionManager sessionManager = new SessionManager(Login.this,SessionManager.SESSION_REMEMBERME);
            sessionManager.createRememberMeSession(_username,_pwd);
        }

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
                        String _password = dataSnapshot.child(_username).child("password").getValue(String.class);
                        String _country = dataSnapshot.child(_username).child("country").getValue(String.class);

                        // Create Session
                        SessionManager sessionManager = new SessionManager(Login.this,SessionManager.SESSION_USERSESSION);
                        sessionManager.createLoginSession(_fullName,_username,_email,_passport,_phoneNo,_password,_date,_gender,_country);

                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(),Dashboard.class));
                        finish();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Password does not match!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "No such user exists!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Login.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Check Internet Conectivity
    private boolean isConnected(Login login) {
        ConnectivityManager connectivityManager = (ConnectivityManager) login.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else
            return false;
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("Please connect to the internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                        System.exit(0);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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