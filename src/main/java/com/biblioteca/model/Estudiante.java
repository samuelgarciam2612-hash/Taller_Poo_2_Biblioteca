package com.biblioteca.model;

public class Estudiante extends Usuario {

    private static final int LIMITE_PRESTAMOS = 3;     // préstamos simultáneos permitidos

    public Estudiante(String id, String nombre) {
        super(id, nombre);                            // valida y guarda lo común
    }

    @Override
    public int getLimitePrestamos() {
        return LIMITE_PRESTAMOS;
    }

    @Override
    public String getTipoUsuario() {
        return "Estudiante";
    }
}