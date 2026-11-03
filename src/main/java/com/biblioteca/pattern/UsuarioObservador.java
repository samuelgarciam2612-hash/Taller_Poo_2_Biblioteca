package com.biblioteca.pattern;

import java.util.logging.Logger;

public class UsuarioObservador implements Observador {

    private static final Logger LOGGER = Logger.getLogger(UsuarioObservador.class.getName());

    private final String nombre;

    public UsuarioObservador(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public void estaDisponible(String mensaje) {
        LOGGER.info(nombre + " recibió: " + mensaje);
    }
}