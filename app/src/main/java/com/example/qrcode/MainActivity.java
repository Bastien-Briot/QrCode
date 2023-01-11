package com.example.qrcode;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button scanBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBtn = findViewById(R.id.scanBtn);
        // Ajoute l'écoute du click
        scanBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // Lance le scanneur au clique
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scan un QR code");
        intentIntegrator.setCameraId(0); // Utilisez la caméra avant par défaut
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        // Si le QR Code n'a rien alors on renvoit un message "Annulé"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Annulé", Toast.LENGTH_SHORT).show();
            } else {
                // Renvoie le text du QR Code
                Toast.makeText(getBaseContext(), intentResult.getContents(), Toast.LENGTH_SHORT).show();
                // Récupère la latitude et longitude
                String[] value = intentResult.getContents().split(":");
                String coordonne = value[1];
                String[] value2 = coordonne.split(",");
                String longitude = value2[1];
                String latitude = value2[0];
                // Envoie la latitude et longitude à l'autre activité
                Intent MapsSender = new Intent(this, MapsSender.class);
                MapsSender.putExtra("latitude", latitude);
                MapsSender.putExtra("longitude", longitude);
                startActivity(MapsSender);
            }
        } else {
            // Renvoie les informations reçu
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}