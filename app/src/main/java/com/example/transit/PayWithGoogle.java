package com.example.transit;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class PayWithGoogle extends Fragment {

    public static final String GPAY_PACKAGE_NAME = "com.google.android.walletnfcrel";
    TextView balance;
    Button pay;
    TextInputLayout amount;
    String username, phoneNo, note, status;
    Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay_with_google, container, false);

        balance = view.findViewById(R.id.balance);

        username = ((Dashboard) getActivity()).getUserName();
        phoneNo = ((Dashboard) getActivity()).getphoneNo();

        Query checkCredit = FirebaseDatabase.getInstance().getReference("Credit").orderByChild("username").equalTo(username);

        checkCredit.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Float _balance = dataSnapshot.child(username).child("credit").getValue(Float.class);
                    balance.setText(String.valueOf(_balance));
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pay = view.findViewById(R.id.pay_btn);
        amount = view.findViewById(R.id.gpay_amount);

        note = "Recharge";

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float _amount = Float.parseFloat(amount.getEditText().getText().toString().trim());

                if (_amount != 0.0) {
                    amount.setErrorEnabled(false);

                    uri = getUpiPaymentUri(username, phoneNo, note, _amount);
                    payWithGpay(GPAY_PACKAGE_NAME);
                } else {
                    amount.setErrorEnabled(true);
                    amount.setError("Please enter amount to pay!");
                }
            }
        });
    }

    private static Uri getUpiPaymentUri(String name, String upiId, String note, Float amount) {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", String.valueOf(amount))
                .appendQueryParameter("cu", "INR")
                .build();
    }

    private void payWithGpay(String packageName) {
        if (isAppInstalled(getContext(), packageName)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(packageName);
            startActivityForResult(intent, 0);
        } else {
            Toast.makeText(getContext(), "Please install Gpay!", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            status = data.getStringExtra("Status").toLowerCase();
        }

        if (status.equals("success")) {
            Toast.makeText(getContext(), "Transaction was successfull!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Transaction failed!", Toast.LENGTH_SHORT).show();
        }
    }
}