package com.example.transit.HelperClasses;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    private static DatabaseReference instance;

    private FirebaseHelper(){}

    public static DatabaseReference getInstance(){
        if(instance == null){
            instance = FirebaseDatabase.getInstance().getReference("Credit");
        }
        return instance;
    }

}
