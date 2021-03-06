package com.example.transit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.transit.Databases.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class UserDashboard extends Fragment {

    TextView fullName,username,country,balance;
    RelativeLayout progressBar;
    LinearLayout journey;
    ImageView logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fullName = view.findViewById(R.id.fullName);
        username = view.findViewById(R.id.userName);
        country = view.findViewById(R.id.local_foreign);
        balance = view.findViewById(R.id.balance_dashboard);
        progressBar = view.findViewById(R.id.progress_bar);
        logout = view.findViewById(R.id.logout);

        journey = view.findViewById(R.id.journey_list);

        String _country = ((Dashboard) getActivity()).getCountry();
        final String _username = ((Dashboard) getActivity()).getUserName();

        fullName.setText(((Dashboard) getActivity()).getFullName());
        username.setText(((Dashboard) getActivity()).getUserName());

        progressBar.setVisibility(View.VISIBLE);

        if(_country.equals("Sri Lanka")){
            country.setText("Local");
        }else{
            country.setText("Foreign");
        }

        Query checkCredit = FirebaseDatabase.getInstance().getReference("Credit").orderByChild("username").equalTo(_username);

        checkCredit.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Float _balance = dataSnapshot.child(_username).child("credit").getValue(Float.class);
                    balance.setText(String.valueOf(_balance));
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query getJourney = FirebaseDatabase.getInstance().getReference("Journey").child(_username);

        getJourney.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String journey = dataSnapshot1.child("journey").getValue(String.class);
                        Long price = dataSnapshot1.child("price").getValue(Long.class);
                        String date = dataSnapshot1.child("date").getValue(String.class);
                        addJourneyCard(journey,price,date);
                    }
                    progressBar.setVisibility(View.GONE);
                }else {
                    addDefaultJourney();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setCancelable(true);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure to Logout?");

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SessionManager sessionManager = new SessionManager(getContext(), SessionManager.SESSION_USERSESSION);
                        sessionManager.logoutUserFromSession();
                        Intent intent = new Intent(getContext(), Login.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                builder.show();
            }
        });
    }

    private void addDefaultJourney() {
        View journeyView = getLayoutInflater().inflate(R.layout.default_journey,null,false);
        journeyView.findViewById(R.id.defalut_journey);
        journey.addView(journeyView);
    }

    private void addJourneyCard(String _journey,Long _price,String _date){
        View journeyView = getLayoutInflater().inflate(R.layout.journey_layout,null,false);

        TextView _journey_desc = (TextView)journeyView.findViewById(R.id.journey_desc);
        _journey_desc.setText(_journey);

        TextView _journey_price = (TextView)journeyView.findViewById(R.id.journey_price);
        _journey_price.setText(String.valueOf(_price));

        TextView _journey_date = (TextView)journeyView.findViewById(R.id.journey_date);
        _journey_date.setText(_date);

        journey.addView(journeyView);
    }
}