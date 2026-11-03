package com.biblioteca.model;

public class Docente extends Usuario {

    private static final int LIMITE_PRESTAMOS = 5;     // préstamos simultáneos permitidos

    public Docente(String id, String nombre) {
        super(id, nombre);                            // valida y guarda lo común
    }

    @Override
    public int getLimitePrestamos() {
        return LIMITE_PRESTAMOS;
    }

    @Override
    public String getTipoUsuario() {
        return "Docente";
    }
}