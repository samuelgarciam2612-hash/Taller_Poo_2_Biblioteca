package com.biblioteca.model;

public class RecursoDigital extends Material {

    private static final int DIAS_PRESTAMO = 7;
    private static final double TARIFA_MULTA_DIARIA = 300;

    public RecursoDigital(String id, String titulo, String autor, int anioPublicacion) {
        super(id, titulo, autor, anioPublicacion);
    }

    @Override
    public String getTipo() {
        return "Recurso digital";
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