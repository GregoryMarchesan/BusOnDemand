package com.marchesan.gregory.busondemand;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.Manifest;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements LocalDialog.NoticeDialogListener, OnMapReadyCallback,LocationListener,ActivityCompat.OnRequestPermissionsResultCallback,LocalDialog.OnAddMarker {

    private LocationManager lm;
    private Location location;
    private double longitude = -29.41400;
    private double latitude = -53.48079;

    private FirebaseDatabase database;

    private static final int REQUEST_PERMISSION = 1;

    private GoogleMap map;
    public String userID = " ";
    private boolean busRequested = false;
    private String lastKeyRef = "";

    private String sentido, linha, horario;


    private static String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};


    public void setRequestInfo(String horario, String sentido, String linha){
        String oi = horario;
        Toast.makeText(this, oi, Toast.LENGTH_SHORT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initMaps();

        // botao para requisitar um onibus
        FloatingActionButton newReq = (FloatingActionButton) findViewById(R.id.newRequest);
        newReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!busRequested){
                    LocalDialog localDialog = LocalDialog.getInstance(MainActivity.this);
                    localDialog.show(getSupportFragmentManager(), "localDialog");
                }else{
                    Toast.makeText(MainActivity.this, "Horario já agendado. Cancele essa solicitação para fazer uma nova!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // botao para cancelar requisicao
        FloatingActionButton cancelReq = (FloatingActionButton) findViewById(R.id.cancelRequest);
        cancelReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(busRequested){
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("linhas").child(sentido)
                            .child(linha).child(lastKeyRef);
                    myRef.setValue(null);
                    busRequested = false;
                    Toast.makeText(MainActivity.this, "Requisição cancelada!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // botao para notificar que o ônibus foi pego
        FloatingActionButton getBus = (FloatingActionButton) findViewById(R.id.get_bus);
        getBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(busRequested){
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("linhas").child(sentido)
                            .child(linha).child(lastKeyRef);
                    myRef.setValue(null);
                    busRequested = false;
                    Toast.makeText(MainActivity.this, "Obrigado por utilizar o aplicativo! Boa viagem!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void initMaps(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions();

        } else {
            lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 60000, this);
        }
    }


    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        if (lm != null) {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
        map.setTrafficEnabled(true);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 11));

        loadMarker();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.menu, menu);
////        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addMenu:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onLocationChanged(Location arg0) {

    }

    @Override
    public void onProviderDisabled(String arg0) {

    }

    @Override
    public void onProviderEnabled(String arg0) {

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"Autorizado",Toast.LENGTH_SHORT).show();
                    initMaps();

                } else {
                    Toast.makeText(this,"Permissão negada",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {


            Toast.makeText(this,"Permissão negada",Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this,PERMISSIONS , REQUEST_PERMISSION);
        }
    }

    public void loadMarker(){
        DatabaseReference locais = database.getReference("linhas").child("Bairro-UFSM");
        locais.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                map.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshots) {
                    Request request = dataSnapshot1.getValue(Request.class);
                    map.addMarker(new MarkerOptions().position(new LatLng(request.getLatitude(), request.getLongitude())).title(String.valueOf(request.getId())));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    @Override
    public void onAddMarker() {
        loadMarker();
    }

    @Override
    public void onDialogPositiveClick(String sentido, String linha, String horario) {
        this.sentido = sentido;
        this.linha = linha;
        this.horario = horario;
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("linhas");
        if (sentido.equals("Bairro-UFSM")) {
            myRef = myRef.child("Bairro-UFSM").child(linha);
        } else if (sentido.equals("UFSM-Bairro")) {
            myRef = myRef.child("UFSM-Bairro").child(linha.toString());
        }
        busRequested = true;
        Request request = new Request();
        request.setId(userID);
        request.setLatitude(location.getLatitude());
        request.setLongitude(location.getLongitude());
        request.setValidPosition(false);
        request.setHorario(horario);
        request.setBusRequested(true);

        Map<String, Object> childUpdates = new HashMap<>();
        lastKeyRef = myRef.push().getKey();
        childUpdates.put(lastKeyRef, request.toMap());

        myRef.updateChildren(childUpdates);

        Toast.makeText(MainActivity.this, "Onibus solicitado!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogNegativeClick() {

    }
}


