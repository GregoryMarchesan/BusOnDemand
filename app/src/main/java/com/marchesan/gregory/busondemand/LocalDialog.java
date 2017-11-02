package com.marchesan.gregory.busondemand;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.id;

/**
 * Created by bruno on 9/23/16.
 */

public class LocalDialog extends DialogFragment
{
    private static final String TAG = LocalDialog.class.getCanonicalName();
    private Activity activity = null;
    private OnAddMarker listner;
    private TimePickerDialog timePickerDialog;
    TextView hora_selecionada;
    int hour_x;
    int minute_x;
    public static final int TIME_PICKER_ID = 1;

    public static LocalDialog getInstance(OnAddMarker listner)
    {
        LocalDialog fragmentDialog = new LocalDialog();
        fragmentDialog.listner = listner;
        return fragmentDialog;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Solicitar ônibus");
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.dialog_local, null);

        // Sentido da linha
        final Spinner sentido = (Spinner) view.findViewById(R.id.sentido);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.sentido));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sentido.setAdapter(adapter);

        // Linha do ônibus
        Spinner linha = (Spinner) view.findViewById(R.id.linha);

        hora_selecionada = (TextView) view.findViewById(R.id.hora);
        hora_selecionada.setText(hour_x + " : " + minute_x);

        ArrayAdapter<String> adapter_linha = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.linha));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        linha.setAdapter(adapter_linha);

        Button button_hour = (Button) view.findViewById(R.id.select_hour);
        button_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
                timePickerDialog.show();
            }
        });

        builder.setView(view);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("linhas");
            Request request = new Request();
            request.setId("0");
            if(sentido.getSelectedItem().toString().equals("Bairro-UFSM")){
                myRef = myRef.child("Bairro-UFSM");

            }else if(sentido.getSelectedItem().toString().equals("UFSM-Bairro")){
                myRef = myRef.child("UFSM-Bairro");
            }


            //                request.setId(edtNome.getText().toString());
            //                request.setLatitude(Double.parseDouble(String.valueOf(location.getLatitude())));
            //                request.setLongitude(Double.parseDouble(String.valueOf(location.getLongitude())));

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(bairroUFSMref.push().getKey(), request.toMap());
            bairroUFSMref.updateChildren(childUpdates);
            listner.onAddMarker();
            dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }
    public interface OnAddMarker{
        void onAddMarker();
    }

    protected TimePickerDialog.OnTimeSetListener kTimePickerListener =
            new TimePickerDialog.OnTimeSetListener(){
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                    hour_x = hourOfDay;
                    minute_x = minuteOfDay;
                    hora_selecionada.setText(hour_x + " : " + minute_x);
                    Toast.makeText(activity, "A hora foi selecionada", Toast.LENGTH_SHORT).show();
                }
            };

    private void showTimeDialog(){
        timePickerDialog = new TimePickerDialog(activity, kTimePickerListener, hour_x, minute_x, true);
    }
}

