package com.example.qrcode;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentResultListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsSender extends FragmentActivity implements OnMapReadyCallback, FragmentResultListener {
    MessageFragment messageFragment;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps_sender);

        MessageFragment messageFragment = new MessageFragment();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_message, messageFragment)
                .replace(R.id.map, mapFragment)
                .commit();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Récupère les informations du qrCode
        Intent receiveIntent = this.getIntent();
        String latitude = receiveIntent.getStringExtra("latitude");
        String longitude = receiveIntent.getStringExtra("longitude");
        // Niveau de zoom de la map
        float ZoomLevel = 16.0F;
        // Définit la position de la map


        LatLng position = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        // Ajoute un marker à la position
        mMap.addMarker(new MarkerOptions().position(position).title("Position GPS"));
        // Met la caméra sur la position avec le niveau de zoom
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, ZoomLevel));


    }

    @Override
    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
        // If message fragment is not null
        if (messageFragment != null) {
            messageFragment.updateDefaultMessage(
                    result.getDouble("latitude"),
                    result.getDouble("longitude"));
        }
    }
}