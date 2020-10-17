package com.example.transit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.transit.Databases.SessionManager;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.HashMap;

public class Dashboard extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);

        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        chipNavigationBar.setItemSelected(R.id.bottom_nav_dashboard, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserDashboard()).commit();
        bottomMenu();

        String fullName = getFullName();
        String userName = getUserName();


    }

    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.bottom_nav_dashboard:
                        fragment = new UserDashboard();
                        chipNavigationBar.setItemSelected(R.id.bottom_nav_dashboard, true);
                        break;
                    case R.id.bottom_nav_card:
                        fragment = new UserPayment();
                        chipNavigationBar.setItemSelected(R.id.bottom_nav_card, true);
                        break;
                    case R.id.bottom_nav_qrcode:
                        fragment = new UserQR();
                        chipNavigationBar.setItemSelected(R.id.bottom_nav_qrcode, true);
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        });
    }

    public String getUserName() {

        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();

        String userName = userDetails.get(SessionManager.KEY_USERNAME);

        return userName;
    }

    public String getFullName() {

        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();

        String fullName = userDetails.get(SessionManager.KEY_FULLNAME);

        return fullName;
    }

    public String getphoneNo() {

        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();

        String phoneNo = userDetails.get(SessionManager.KEY_PHONENO);

        return phoneNo;
    }

    public String getCountry() {

        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();

        String country = userDetails.get(SessionManager.KEY_COUNTRY);

        return country;
    }
}