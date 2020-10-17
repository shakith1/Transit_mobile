package com.example.transit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class UserDashboard extends Fragment {

    TextView fullName,username,country;

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

        String _country = ((Dashboard) getActivity()).getCountry();
        System.out.println(_country);

        fullName.setText(((Dashboard) getActivity()).getFullName());
        username.setText(((Dashboard) getActivity()).getUserName());

        if(_country.equals("Sri Lanka")){
            country.setText("Local");
        }else{
            country.setText("Foreign");
        }

    }
}