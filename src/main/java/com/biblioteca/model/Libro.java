package com.biblioteca.model;

public class Libro extends Material {

    private static final int DIAS_PRESTAMO = 14;            // reglas propias del tipo
    private static final double TARIFA_MULTA_DIARIA = 1000;

    private final String isbn;                               // atributo propio

    public Libro(String id, String titulo, int anioPublicacion, String isbn) {
        super(id, titulo, anioPublicacion);                  // valida y guarda lo común
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("El ISBN del libro no puede estar vacío.");
        }
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
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