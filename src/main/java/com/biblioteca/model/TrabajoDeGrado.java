package com.biblioteca.model;

public class TrabajoDeGrado extends Material {

    private static final int DIAS_PRESTAMO = 3;
    private static final double TARIFA_MULTA_DIARIA = 2000;

    public TrabajoDeGrado(String id, String titulo, String autor, int anioPublicacion) {
        super(id, titulo, autor, anioPublicacion);
    }

    @Override
    public String getTipo() {
        return "Trabajo de grado";
    }

    @Override
    public int getDiasPrestamo() {
        return DIAS_PRESTAMO;
    }

    @Override
    public double getTarifaMultaDiaria() {
        return TARIFA_MULTA_DIARIA;
    }
}