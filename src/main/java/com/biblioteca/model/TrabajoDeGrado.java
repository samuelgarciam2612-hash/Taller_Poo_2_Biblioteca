package com.biblioteca.model;

public class TrabajoDeGrado extends Material {

    private static final int DIAS_PRESTAMO = 3;
    private static final double TARIFA_MULTA_DIARIA = 2000;

    private final String programaAcademico;

    public TrabajoDeGrado(String id, String titulo, int anioPublicacion, String programaAcademico) {
        super(id, titulo, anioPublicacion);
        if (programaAcademico == null || programaAcademico.isBlank()) {
            throw new IllegalArgumentException("El programa académico no puede estar vacío.");
        }
        this.programaAcademico = programaAcademico;
    }

    public String getProgramaAcademico() {
        return programaAcademico;
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