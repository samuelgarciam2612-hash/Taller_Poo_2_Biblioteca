package com.biblioteca.model;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

// Clase base de todo lo que hay en el estante. Abstracta: no existe "un material" genérico.
// Nicolás agrega aquí "Comparable<Material>" y su compareTo en su PR.
public abstract class Material implements Identificable {

    private final String id;
    private final String titulo;
    private final int anioPublicacion;
    private boolean disponible;
    // Fila de reservas en orden de llegada (FIFO).
    // TEMPORAL: se reemplaza por ColaEspera<Usuario> cuando Samuel la termine.
    private final Queue<Usuario> filaDeEspera = new ArrayDeque<>();

    // protected: solo las hijas (Libro, Revista...) pueden llamarlo
    protected Material(String id, String titulo, int anioPublicacion) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del material no puede estar vacío.");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título del material no puede estar vacío.");
        }
        if (anioPublicacion < 0) {
            throw new IllegalArgumentException("El año de publicación no puede ser negativo (se recibió " + anioPublicacion + ").");
        }
        this.id = id;
        this.titulo = titulo;
        this.anioPublicacion = anioPublicacion;
        this.disponible = true;                 // todo material nuevo llega al estante
    }

    // ===== Métodos abstractos: cada tipo define sus reglas (polimorfismo) =====
    public abstract String getTipo();           // "Libro", "Revista"... (Camila agrupa por esto)
    public abstract int getDiasPrestamo();      // cada tipo se presta un tiempo distinto
    public abstract double getTarifaMultaDiaria(); // cada tipo genera una multa distinta

    // Un solo cálculo que sirve para los 4 tipos: usa la tarifa de la hija
    public double calcularMulta(int diasRetraso) {
        if (diasRetraso < 0) {
            throw new IllegalArgumentException("Los días de retraso no pueden ser negativos (se recibió " + diasRetraso + ").");
        }
        return diasRetraso * getTarifaMultaDiaria();
    }

    // ===== Fila de espera (nombre exacto que usa BibliotecaService) =====
    public void agregarALaFilaDeEspera(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario que reserva no puede ser nulo.");
        }
        if (!filaDeEspera.contains(usuario)) {  // la misma persona no aparta dos veces
            filaDeEspera.add(usuario);
        }
    }

    // Saca al primero de la fila. Optional: si nadie espera, caja vacía (no null)
    public Optional<Usuario> sacarSiguienteEnEspera() {
        return Optional.ofNullable(filaDeEspera.poll());
    }

    // Copia de solo lectura: nadie modifica la fila desde afuera
    public List<Usuario> getFilaDeEspera() {
        return List.copyOf(filaDeEspera);
    }

    // ===== Getters y setters =====
    // Sin @Override por ahora: Identificable todavía no declara getId()
    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    // equals / hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Material)) return false;
        Material otro = (Material) o;
        return Objects.equals(id, otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString
    @Override
    public String toString() {
        return getTipo() + " [" + id + "] " + titulo + " (" + anioPublicacion + ") - "
                + (disponible ? "Disponible" : "Prestado");
    }
}