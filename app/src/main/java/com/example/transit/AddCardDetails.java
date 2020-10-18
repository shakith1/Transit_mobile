package com.example.transit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.transit.Databases.CardHelperClass;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCardDetails extends Fragment {

    TextInputLayout name, no, date, cvc;
    Button save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_card_details, container, false);

        name = view.findViewById(R.id.details_holder_name);
        no = view.findViewById(R.id.details_holder_card_number);
        date = view.findViewById(R.id.details_holder_card_expire_date);
        cvc = view.findViewById(R.id.details_holder_cvc);
        save = view.findViewById(R.id.save_card);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _name = name.getEditText().getText().toString().trim();
                String _no = no.getEditText().getText().toString().trim();
                String _date = date.getEditText().getText().toString().trim();
                String _cvc = cvc.getEditText().getText().toString().trim();

                String username = ((Dashboard) getActivity()).getUserName();

                FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                DatabaseReference reference = rootNode.getReference("Card");

                CardHelperClass addNewCard = new CardHelperClass(_name, _no, _date, _cvc);

                reference.child(username).child(_no).setValue(addNewCard);

                Fragment fragment = new CardPayment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        });
    }
}