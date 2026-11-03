package com.biblioteca.model;

public class Libro extends Material {

    private static final int DIAS_PRESTAMO = 14;            // reglas propias del tipo
    private static final double TARIFA_MULTA_DIARIA = 1000;

    public Libro(String id, String titulo, String autor, int anioPublicacion) {
        super(id, titulo, autor, anioPublicacion);              // valida y guarda lo común
    }

    @Override
    public String getTipo() {
        return "Libro";
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