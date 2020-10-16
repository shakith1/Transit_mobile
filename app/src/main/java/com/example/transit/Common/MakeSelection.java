package com.example.transit.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.transit.R;
import com.example.transit.SignupThird;
import com.example.transit.VerifyOTP;

public class MakeSelection extends AppCompatActivity {

    TextView mobile_desc,email_desc;
    Button via_sms;
    String username,phoneNo,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_make_selection);

        mobile_desc = findViewById(R.id.mobile_desc);
        email_desc = findViewById(R.id.mail_desc);
        via_sms = findViewById(R.id.btn_via_sms);

        username = getIntent().getStringExtra("username");
        phoneNo = getIntent().getStringExtra("phoneNo");
        email = getIntent().getStringExtra("email");

        mobile_desc.setText(phoneNo);
        email_desc.setText(email);
    }

    public void callVerifyOTP(View view){

        Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);

        intent.putExtra("username",username);
        intent.putExtra("phoneNo",phoneNo);
        intent.putExtra("email",email);
        intent.putExtra("whatToDo","updateData");

        Pair[] pairs = new Pair[1];

        pairs[0] = new Pair<View, String>(via_sms, "transition_next");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MakeSelection.this, pairs);
            startActivity(intent, options.toBundle());
            finish();
        } else {
            startActivity(intent);
            finish();
        }

    }
}