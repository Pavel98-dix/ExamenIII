package com.example.exameniii;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static int PETICION_PERMISO_LOCALIZACION=101;
    private GoogleMap mMap;
    double lat=0.0, lng=0.0;
    LatLng coordenadas;
    private Marker marcador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Colocamos la vista del mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //La herramienta de Google Maps que nos permite colocar botones de zoom
        UiSettings mapSettings;
        mapSettings=mMap.getUiSettings();
        mapSettings.setCompassEnabled(true);
        mapSettings.setZoomControlsEnabled(true);
        mapSettings.setZoomGesturesEnabled(true);
        mapSettings.setRotateGesturesEnabled(true);
        mapSettings.setScrollGesturesEnabled(true);

        hospitales();
        direccion();
    }
    //Este método permitira identificar la ubicación de los hospitales.
    private void hospitales()
    {
        UiSettings mapHospitalSetting;
        mapHospitalSetting= mMap.getUiSettings();
        mapHospitalSetting.setZoomControlsEnabled(true);

        LatLng hospitalMelenium= new LatLng(19.1569262,-96.1485545);
        mMap.addMarker(new MarkerOptions().
                icon(BitmapDescriptorFactory.fromResource(R.drawable.hospitacono)).
                anchor(0.0f,1.0f).position(hospitalMelenium).title("Hospital Milenium"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hospitalMelenium));

        LatLng hospitalEspecialista= new LatLng(19.1733031,-96.1722438);
        mMap.addMarker(new MarkerOptions().
                icon(BitmapDescriptorFactory.fromResource(R.drawable.hospitacono)).
                anchor(0.0f,1.0f).position(hospitalEspecialista).title("Hospital Especialista Issste"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hospitalEspecialista));

        LatLng hospitalNaval= new LatLng(19.1997531,-96.1316888);
        mMap.addMarker(new MarkerOptions().
                icon(BitmapDescriptorFactory.fromResource(R.drawable.hospitacono)).
                anchor(0.0f,1.0f).position(hospitalNaval).title("Hospital Externa del Hospital Naval"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hospitalNaval));

        LatLng hospitalMilitar= new LatLng(19.1611657,-96.1408566);
        mMap.addMarker(new MarkerOptions().
                icon(BitmapDescriptorFactory.fromResource(R.drawable.hospitacono)).
                anchor(0.0f,1.0f).position(hospitalMilitar).title("Hospital Militar La Boticaria"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hospitalMilitar));

        LatLng hospitalMedica= new LatLng(19.1714604,-96.1905699);
        mMap.addMarker(new MarkerOptions().
                icon(BitmapDescriptorFactory.fromResource(R.drawable.hospitacono)).
                anchor(0.0f,1.0f).position(hospitalMedica).title("Start Médica"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hospitalMedica));





    }

    private void direccion()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PETICION_PERMISO_LOCALIZACION);
            return;
        }
        else
        {
            LocationManager locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            updateLocalizacion(location);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,30000,0,locationListener);

        }
    }

    private void Marcador (double lat, double lng)
    {
        coordenadas= new LatLng(lat,lng);
        CameraUpdate ubicacionCam= CameraUpdateFactory.newLatLngZoom(coordenadas,16);
        if (marcador!=null )marcador.remove();
        marcador=mMap.addMarker(new
                MarkerOptions().position(coordenadas).title("Dirección: "+"("+coordenadas+")").
                icon(BitmapDescriptorFactory.fromResource(R.drawable.narutocono)).anchor(0.0f,1.0f));

    }

    private void updateLocalizacion(Location location)
    {
        if (location!=null)
        {
            lat=location.getLatitude();
            lng=location.getLongitude();
            Marcador(lat,lng);

        }
    }




    LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            updateLocalizacion(location);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            Toast.makeText(getApplicationContext(),"GPS ACTIVADO",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            Toast.makeText(getApplicationContext(),"GPS DESACTIVADO",Toast.LENGTH_SHORT).show();
            locationStart();
        }
    };



    private void locationStart()
    {
        LocationManager locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnable= locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gpsEnable)
        {
            Intent settingIntent= new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingIntent);
        }
    }

}