package com.example.transit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;


public class AddPayment extends Fragment {

    String username;
    TextView balance;
    MaterialSpinner spinner;
    TextInputLayout amount;
    Button pay;

    Float _balance;
    List<String> cardList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_payment, container, false);

        balance = view.findViewById(R.id.payment_balance);
        spinner = view.findViewById(R.id.spinner);
        pay = view.findViewById(R.id.pay_btn);
        amount = view.findViewById(R.id.pay_amount);

        username = ((Dashboard) getActivity()).getUserName();

        Query checkCredit = FirebaseDatabase.getInstance().getReference("Credit").orderByChild("username").equalTo(username);

        checkCredit.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    _balance = dataSnapshot.child(username).child("credit").getValue(Float.class);
                    balance.setText(String.valueOf(_balance));
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        String username = ((Dashboard) getActivity()).getUserName();
        Query getCardDetail = FirebaseDatabase.getInstance().getReference("Card").child(username);

        getCardDetail.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String no = dataSnapshot1.child("no").getValue(String.class);
                        cardList.add(no);
                    }
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinner.setItems(cardList);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int index = spinner.getSelectedIndex();
                String card = cardList.get(index);

                String _amount = amount.getEditText().getText().toString().trim();

                if(_amount.isEmpty()){
                    amount.setErrorEnabled(true);
                    amount.setError("Enter amount to add!");
                }else{
                    Float _amount_converted = Float.parseFloat(_amount);

                    Float new_amount = _balance + _amount_converted;

                    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                    DatabaseReference reference_credit = rootNode.getReference("Credit");
                    reference_credit.child(username).child("credit").setValue(new_amount);

                    Fragment fragment = new UserDashboard();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                    ((Dashboard) getActivity()).setChipNavigation();
                }
            }
        });

    }
}