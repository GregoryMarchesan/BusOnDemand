package com.marchesan.gregory.busondemand;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.SystemClock;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
// ----------------------------------------------------------------------------
//                              Gregory Marchesan
//                              Vinicius Farias
// ----------------------------------------------------------------------------
//                               04/11/2017

public class MainActivity extends AppCompatActivity implements LocalDialog.NoticeDialogListener, OnMapReadyCallback,LocationListener,ActivityCompat.OnRequestPermissionsResultCallback,LocalDialog.OnAddMarker {

    private LocationManager lm;
    private static Location location;
    private double longitude = -29.695245;
    private double latitude = -53.814994;

    private FirebaseDatabase database;

    private static final int REQUEST_PERMISSION = 1;

    private GoogleMap map;
    public static String userID = " ";
    private static boolean busRequested = false;
    private static String lastKeyRef = "";

    private static String sentido;
    private static String linha;
    private static String horario;
    private static int hour;
    private static int minute;
    private static boolean sentToFirebase = false;

    private PeriodicService listener;
    private static Activity thisActivity = null;

    private static String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // contexto
        thisActivity = this;

        // referencia para a base de dados
        database = FirebaseDatabase.getInstance();

        // recupera as informações persistentes
        sharedPreferences = getSharedPreferences("Contexto", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(sharedPreferences.getBoolean("firstRun", true)){ // primeira execução/ grava os dados na memoria
            // guarda as informacoes de modo persistente
            editor.putString("sentido", "Bairro-UFSM");
            editor.putString("linha", "Universidade - Faixa Velha");
            editor.putInt("hora", 0);
            editor.putInt("minuto", 0);
            editor.putBoolean("busRequested", false);
            editor.putBoolean("sentToFirebase", false);
            editor.putString("lastKeyRef", "");
            editor.putBoolean("firstRun", false);
            editor.putString("userID", "000000");
            editor.apply();
        }else{ // carrega os dados
            busRequested = sharedPreferences.getBoolean("busRequested", false);
            sentToFirebase = sharedPreferences.getBoolean("sentToFirebase", false);
            hour = sharedPreferences.getInt("hora", 0);
            minute = sharedPreferences.getInt("minuto", 0);
            sentido = sharedPreferences.getString("sentido", "Bairro-UFSM");
            linha = sharedPreferences.getString("linha", "Universidade - Faixa Velha");
            lastKeyRef = sharedPreferences.getString("lastKeyRef", "");
            userID = sharedPreferences.getString("userID", "000000");
        }

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
                if(busRequested && sentToFirebase){
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("linhas").child(sentido)
                            .child(linha).child(lastKeyRef);
                    myRef.setValue(null);
                    busRequested = false;
                    sentToFirebase = false;
                    editor.putBoolean("busRequested", busRequested);
                    editor.putBoolean("sentToFirebase", sentToFirebase);
                    editor.apply();
                    Toast.makeText(MainActivity.this, "Requisição cancelada!", Toast.LENGTH_SHORT).show();
                }else if(busRequested){
                    busRequested = false;
                    editor.putBoolean("busRequested", false);
                    editor.apply();
                    Toast.makeText(MainActivity.this, "Requisição cancelada!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // botao para notificar que o ônibus foi pego
        FloatingActionButton getBus = (FloatingActionButton) findViewById(R.id.get_bus);
        getBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(busRequested && sentToFirebase){
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("linhas").child(sentido)
                            .child(linha).child(lastKeyRef);
                    myRef.setValue(null);
                    busRequested = false;
                    sentToFirebase = false;
                    editor.putBoolean("busRequested", busRequested).commit();
                    editor.putBoolean("sentToFirebase", sentToFirebase).commit();
                    editor.apply();
                    Toast.makeText(MainActivity.this, "Obrigado por utilizar o aplicativo! Boa viagem!",
                            Toast.LENGTH_SHORT).show();
                }else if(busRequested){
                    busRequested = false;
                    editor.putBoolean("busRequested", false);
                    editor.apply();
                }
            }
        });

        // sincroniza a tarefa periodica para verificar a hora de pedir um ônibus
        Calendar cal = Calendar.getInstance();
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;
        myIntent = new Intent(MainActivity.this, PeriodicService.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,myIntent,0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 5*1000,pendingIntent);
    }

    // tarefa periodica
    public static class PeriodicService extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm");
            Date data = new Date();
            Calendar  cal = Calendar.getInstance();
            cal.setTime(data);
            Date data_atual = cal.getTime();
            String hora_atual = dateFormat_hora.format(data_atual);

            String[] tempo_minutos = hora_atual.split(":");

            //tempo atual em minutos
            int tempo_minutes_now = Integer.parseInt(tempo_minutos[0])*60 + Integer.parseInt(tempo_minutos[1]);
            verify_time(tempo_minutes_now); //verifica se esta na hora de solicitar um onibus
        }
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

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 9));

        loadMarker();
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

    // captura os dados da janela de dialogo
    @Override
    public void onDialogPositiveClick(String user, String sentido, String linha, int hour, int minute) {
        this.sentido = sentido;
        this.linha = linha;
        this.hour = hour;
        this.minute = minute;
        busRequested = true;
        userID = user;

        // guarda as informacoes de modo persistente
        editor.putString("sentido", sentido);
        editor.putString("linha", linha);
        editor.putInt("hora", hour);
        editor.putInt("minuto", minute);
        editor.putBoolean("busRequested", busRequested);
        editor.putString("userID", userID);
        editor.apply();
    }

    // verifica se esta na hora de chamar um onibus
    public static void verify_time(int time_minutes_now) {
        //carga dos valores da sharedPreferences
        sharedPreferences = thisActivity.getSharedPreferences("Contexto", MODE_PRIVATE);
        busRequested = sharedPreferences.getBoolean("busRequested", false);
        sentToFirebase = sharedPreferences.getBoolean("sentToFirebase", false);
        hour = sharedPreferences.getInt("hora", 0);
        minute = sharedPreferences.getInt("minuto", 0);
        sentido = sharedPreferences.getString("sentido", "Bairro-UFSM");
        linha = sharedPreferences.getString("linha", "Universidade - Faixa Velha");
        editor = sharedPreferences.edit();

//        Toast.makeText(thisActivity, "Escreve!" + sentToFirebase + " " + busRequested, Toast.LENGTH_SHORT);

//        // logica para solicitacao
        if((hour*60 + minute < time_minutes_now) && (busRequested)){
            Toast.makeText(thisActivity,"Solicitação Inválida!", Toast.LENGTH_SHORT).show();
            busRequested = false; // cancela a solicitação
            editor.putBoolean("busRequested", busRequested);
            editor.apply();
        }else if(((hour*60 + minute - time_minutes_now) <= 15) && busRequested && !sentToFirebase){//coloca na lista 15 minutos antes
            Request request = new Request();
            request.setId(userID);
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("linhas");
            if (sentido.equals("Bairro-UFSM")) {
                myRef = myRef.child("Bairro-UFSM").child(linha);
            } else if (sentido.equals("UFSM-Bairro")) {
                myRef = myRef.child("UFSM-Bairro").child(linha);
            }
            horario = hour + ":" + minute;
            if(location != null){
                request.setLatitude(location.getLatitude());
                request.setLongitude(location.getLongitude());
                request.setValidPosition(true);
            }else{
                request.setValidPosition(false);
            }
            request.setHorario(horario);
            request.setBusRequested(true);
            Map<String, Object> childUpdates = new HashMap<>();
            lastKeyRef = myRef.push().getKey();
            childUpdates.put(lastKeyRef, request.toMap());

            myRef.updateChildren(childUpdates);

            Toast.makeText(thisActivity, "Onibus solicitado!", Toast.LENGTH_SHORT).show();
            sentToFirebase = true;
            editor.putBoolean("sentToFirebase", sentToFirebase);
            editor.putString("lastKeyRef", lastKeyRef);
            editor.apply();
        }
    }
}


