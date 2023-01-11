package com.example.qrcode;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.textfield.TextInputEditText;

public class MessageFragment extends Fragment implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    Button btnQuitter;
    Button btnEnvoyer;
    TextInputEditText edMessage;
    TextInputEditText edNumero;
    String phoneNo;
    String message;

    public MessageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnQuitter = view.findViewById(R.id.btnQuitter);
        edMessage = view.findViewById(R.id.EditText_Message);
        edNumero = view.findViewById(R.id.EditText_Phone);
        btnEnvoyer = view.findViewById(R.id.btnEnvoyer);
        // Ajoute l'écoute du click
        btnQuitter.setOnClickListener(this);
        btnEnvoyer.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view == btnQuitter) {
            // Retourne sur l'activité de base
            Intent MainActivity = new Intent(getActivity(), MainActivity.class);
            startActivity(MainActivity);
        }

        if (view == btnEnvoyer) {
            sensSMSMessage();
        }
    }

    public void updateDefaultMessage(double lat, double lng) {
        String messageText = edMessage.getText().toString();
        // If text field is empty or contains default value
        if (messageText.equals("") || messageText.startsWith(getString(R.string.messageParDefaut))) {
            edMessage.setText(String.format("%s%s, %s", getString(R.string.messageParDefaut), lat, lng));
        }
    }

    protected void sensSMSMessage() {
        phoneNo = edNumero.getText().toString();
        message = edMessage.getText().toString();

        if(PhoneNumberUtils.isGlobalPhoneNumber(phoneNo)) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.SEND_SMS)) {
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
                }
            } else {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, message, null, null);
                Toast.makeText(getActivity(), "SMS envoyé.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "Numéro de téléphone invalide.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permission[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getActivity(), "SMS envoyé.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "SMS non envoyé, autorisation refusée.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

}