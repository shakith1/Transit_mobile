package com.example.transit;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.transit.Databases.SessionManager;
import com.example.transit.R;

import java.util.HashMap;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class UserQR extends Fragment {

    ImageView qrcode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_q_r, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        qrcode = view.findViewById(R.id.qr_code);

        String userName = ((Dashboard) getActivity()).getUserName();

        QRGEncoder qrgEncoder = new QRGEncoder(userName,null, QRGContents.Type.TEXT,500);

        Bitmap qrBits = qrgEncoder.getBitmap();
        qrcode.setImageBitmap(qrBits);
    }
}