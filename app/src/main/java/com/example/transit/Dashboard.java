package com.example.transit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.transit.Databases.SessionManager;

import java.util.HashMap;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TextView textView = findViewById(R.id.textView);

        SessionManager sessionManager = new SessionManager(this,SessionManager.SESSION_USERSESSION);
        HashMap<String,String> userDetails =  sessionManager.getUserDetailFromSession();

        String fullName = userDetails.get(SessionManager.KEY_FULLNAME);

        textView.setText(fullName);
    }
}