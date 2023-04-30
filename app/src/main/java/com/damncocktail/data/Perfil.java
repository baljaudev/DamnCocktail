package com.damncocktail.data;

import java.io.Serializable;

public class Perfil implements Serializable {
    private String nombre;
    private String email;

    public Perfil() {
    }

    public Perfil(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

}
