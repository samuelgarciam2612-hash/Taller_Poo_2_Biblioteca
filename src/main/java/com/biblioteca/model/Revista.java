package com.biblioteca.model;

public class Revista extends Material {

    private static final int DIAS_PRESTAMO = 7;
    private static final double TARIFA_MULTA_DIARIA = 500;

    private final int numeroEdicion;

    public Revista(String id, String titulo, int anioPublicacion, int numeroEdicion) {
        super(id, titulo, anioPublicacion);
        if (numeroEdicion <= 0) {
            throw new IllegalArgumentException("El número de edición debe ser mayor que 0 (se recibió " + numeroEdicion + ").");
        }
        this.numeroEdicion = numeroEdicion;
    }

    public int getNumeroEdicion() {
        return numeroEdicion;
    }

    @Override
    public String getTipo() {
        return "Revista";
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