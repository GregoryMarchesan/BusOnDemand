package com.marchesan.gregory.busondemand;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


public class Request {

    private String id;
    private double latitude;
    private double longitude;
    private boolean validPosition;
    private boolean busRequested;
    private String horario;

    public Request() {
        this.id = "";
        this.latitude = 0;
        this.longitude = 0;
        this.validPosition = false;
        this.busRequested = false;
        this.horario = "";
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isBusRequested() {
        return busRequested;
    }

    public void setBusRequested(boolean busRequested) {
        this.busRequested = busRequested;
    }

    public boolean isValidPosition() {
        return validPosition;
    }

    public void setValidPosition(boolean validPosition) {
        this.validPosition = validPosition;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> request = new HashMap<>();
        request.put("ID", id);
        request.put("latitude", latitude);
        request.put("longitude", longitude);
        request.put("Posição válida", validPosition);
        request.put("Onibus pedido", busRequested);
        request.put("Horario", horario);

        return request;
    }
}
