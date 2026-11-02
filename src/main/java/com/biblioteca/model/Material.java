package com.biblioteca.model;

import java.text.Collator;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

// Clase base de todo lo que hay en el estante. Abstracta: no existe "un material" genérico.
public abstract class Material implements Identificable, Comparable<Material> {

    // Comparador en español: con compareToIgnoreCase la Á quedaría después de la z.
    // static: uno solo para toda la clase. PRIMARY: ignora tildes y mayúsculas.
    private static final Collator COMPARADOR_ES = Collator.getInstance(Locale.forLanguageTag("es"));
    static {                                   // se ejecuta una sola vez, al cargar la clase
        COMPARADOR_ES.setStrength(Collator.PRIMARY);
    }

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

    // equals / hashCode por id
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

    // Comparable: alfabético por título
    // Si dos materiales tienen el mismo título, desempata por id.
    @Override
    public int compareTo(Material otro) {
        int porTitulo = COMPARADOR_ES.compare(this.titulo, otro.titulo);
        if (porTitulo != 0) {
            return porTitulo;
        }
        return this.id.compareTo(otro.id);
    }

    @Override
    public String toString() {
        return getTipo() + " [" + id + "] " + titulo + " (" + anioPublicacion + ") - "
                + (disponible ? "Disponible" : "Prestado");
    }
}