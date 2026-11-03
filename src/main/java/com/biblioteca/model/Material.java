package com.biblioteca.model;

import com.biblioteca.exception.MaterialNoDisponibleException;
import com.biblioteca.pattern.ColaEspera;

import java.text.Collator;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

// Clase base de todo lo que hay en el estante. Abstracta: no existe "un material" genérico.
public abstract class Material implements Identificable, Comparable<Material> {

    // Comparador en español: con compareToIgnoreCase la "Á" quedaría después de la "z" ("Álgebra" tras "semana").
    // static: uno solo para toda la clase. PRIMARY: ignora tildes y mayúsculas.
    private static final Collator COMPARADOR_ES = Collator.getInstance(Locale.forLanguageTag("es"));
    static {                                   // se ejecuta una sola vez, al cargar la clase
        COMPARADOR_ES.setStrength(Collator.PRIMARY);
    }

    private final String id;
    private final String titulo;
    private final String autor;
    private final int anioPublicacion;
    private boolean disponible;
    // Fila de reservas en orden de llegada (FIFO): usa la clase genérica de Samuel
    private final ColaEspera<Usuario> filaDeEspera = new ColaEspera<>();

    // protected: solo las hijas (Libro, Revista...) pueden llamarlo
    // Mismo orden de parámetros que usan las factories de Nicolás: (codigo, titulo, autor, anio)
    protected Material(String id, String titulo, String autor, int anioPublicacion) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del material no puede estar vacío.");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título del material no puede estar vacío.");
        }
        if (autor == null || autor.isBlank()) {
            throw new IllegalArgumentException("El autor del material no puede estar vacío.");
        }
        if (anioPublicacion < 0) {
            throw new IllegalArgumentException("El año de publicación no puede ser negativo (se recibió " + anioPublicacion + ").");
        }
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
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
        filaDeEspera.encolar(usuario);
    }

    // Saca al primero de la fila. Optional: si nadie espera, caja vacía (no null)
    public Optional<Usuario> sacarSiguienteEnEspera() {
        return Optional.ofNullable(filaDeEspera.atenderSiguiente());
    }

    // Mira quién sigue sin sacarlo de la fila
    public Optional<Usuario> verSiguienteEnEspera() {
        return Optional.ofNullable(filaDeEspera.verSiguiente());
    }

    public int getCantidadEnEspera() {
        return filaDeEspera.obtenerTamano();
    }

    // ===== Préstamo del material =====
    // Regla de negocio: no se presta lo que ya está prestado
    public void prestar() throws MaterialNoDisponibleException {
        if (!disponible) {
            throw new MaterialNoDisponibleException("El material '" + titulo + "' ya está prestado.");
        }
        disponible = false;
    }

    public void devolver() {
        disponible = true;
    }

    // ===== Getters y setters =====
    @Override                                   // lo exige Identificable
    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
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
        return getTipo() + " [" + id + "] " + titulo + " - " + autor + " (" + anioPublicacion + ") - "
                + (disponible ? "Disponible" : "Prestado");
    }
}