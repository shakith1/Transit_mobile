package com.example.transit.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.transit.R;
import com.example.transit.SignUp;
import com.example.transit.SignUpSecond;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ForgetPassword extends AppCompatActivity {

    TextInputLayout username;
    Button next;
    RelativeLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password);

        username = findViewById(R.id.forget_pwd_username);
        next = findViewById(R.id.forget_pwd_next_btn);
        progressBar = findViewById(R.id.progress_bar);
    }

    public void callMakeSelection(View view){

        if(!validateUsername()){
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Get username
        final String _username = username.getEditText().getText().toString().trim();

        // Database
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").equalTo(_username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    username.setError(null);
                    username.setErrorEnabled(false);

                    progressBar.setVisibility(View.GONE);

                    String phoneNo = dataSnapshot.child(_username).child("phoneNo").getValue(String.class);
                    String email = dataSnapshot.child(_username).child("email").getValue(String.class);

                    Intent intent = new Intent(getApplicationContext(), MakeSelection.class);
                    intent.putExtra("username",_username);
                    intent.putExtra("phoneNo",phoneNo);
                    intent.putExtra("email",email);

                    Pair[] pairs = new Pair[1];
                    pairs[0] = new Pair<View, String>(next, "forget_pwd");

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ForgetPassword.this, pairs);
                        startActivity(intent, options.toBundle());
                        finish();
                    } else {
                        startActivity(intent);
                        finish();
                    }
                }else{
                    progressBar.setVisibility(View.GONE);
                    username.setError("User not found!");
                    username.setErrorEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean validateUsername() {
        String value = username.getEditText().getText().toString().trim();

        if (value.isEmpty()) {
            username.setError("Username cannot be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }
}