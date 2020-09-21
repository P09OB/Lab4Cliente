package com.example.lab4cliente.model;

public class Confirmacion {

    private String id;
    private Boolean respuesta;

    public Confirmacion() {
    }

    public Confirmacion(String id, Boolean respuesta) {
        this.id = id;
        this.respuesta = respuesta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Boolean respuesta) {
        this.respuesta = respuesta;
    }
}
