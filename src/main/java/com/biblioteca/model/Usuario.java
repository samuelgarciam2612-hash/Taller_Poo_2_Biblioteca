package com.biblioteca.model;

import java.util.Objects;

// ESQUELETO INICIAL: solo lo necesario para que Material compile.
// En el siguiente paso se completa (límites, préstamos activos, multas, Estudiante y Docente).
public abstract class Usuario implements Identificable {

    private final String id;
    private final String nombre;

    protected Usuario(String id, String nombre) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del usuario no puede estar vacío.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario no puede estar vacío.");
        }
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario otro = (Usuario) o;
        return Objects.equals(id, otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}