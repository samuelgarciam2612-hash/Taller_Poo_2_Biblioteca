package com.biblioteca.model;

public class RecursoDigital extends Material {

    private static final int DIAS_PRESTAMO = 7;
    private static final double TARIFA_MULTA_DIARIA = 300;

    private final String formato;                // PDF, video, audio...

    public RecursoDigital(String id, String titulo, int anioPublicacion, String formato) {
        super(id, titulo, anioPublicacion);
        if (formato == null || formato.isBlank()) {
            throw new IllegalArgumentException("El formato del recurso digital no puede estar vacío.");
        }
        this.formato = formato;
    }

    public String getFormato() {
        return formato;
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