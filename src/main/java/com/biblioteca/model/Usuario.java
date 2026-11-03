package com.biblioteca.model;

import com.biblioteca.exception.LimitePrestamosExcedidoException;
import com.biblioteca.exception.UsuarioConMultasPendientesException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Clase base de quien usa la biblioteca. Abstracta: solo existen Estudiante o Docente.
public abstract class Usuario implements Identificable {

    private final String id;
    private final String nombre;
    private final List<Prestamo> prestamosActivos = new ArrayList<>();
    private final List<Multa> multas = new ArrayList<>();

    // protected: solo las hijas (Estudiante, Docente) pueden llamarlo
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

    // ===== Métodos abstractos: cada tipo define sus reglas (polimorfismo) =====
    public abstract int getLimitePrestamos();   // estudiantes y docentes tienen topes distintos
    public abstract String getTipoUsuario();    // "Estudiante" o "Docente"

    // ===== Reglas de préstamo =====
    // Antes de registrar un préstamo valida las dos reglas del enunciado
    public void registrarPrestamo(Prestamo prestamo)
            throws UsuarioConMultasPendientesException, LimitePrestamosExcedidoException {
        if (prestamo == null) {
            throw new IllegalArgumentException("El préstamo no puede ser nulo.");
        }
        if (tieneMultasPendientes()) {
            throw new UsuarioConMultasPendientesException(
                    nombre + " tiene multas pendientes por $" + getTotalMultasPendientes() + " y no puede pedir préstamos.");
        }
        if (prestamosActivos.size() >= getLimitePrestamos()) {
            throw new LimitePrestamosExcedidoException(
                    nombre + " ya tiene " + prestamosActivos.size() + " préstamos (límite de " + getTipoUsuario() + ": " + getLimitePrestamos() + ").");
        }
        prestamosActivos.add(prestamo);
    }

    // Sin modificador (package-private): solo las clases de model, como Prestamo, lo usan al devolver
    void finalizarPrestamo(Prestamo prestamo) {
        prestamosActivos.remove(prestamo);
    }

    // ===== Multas =====
    void agregarMulta(Multa multa) {           // la llama Prestamo cuando hay retraso
        multas.add(multa);
    }

    public boolean tieneMultasPendientes() {    // lo usa BibliotecaService
        return multas.stream().anyMatch(m -> !m.isPagada());
    }

    public double getTotalMultasPendientes() {
        return multas.stream()
                .filter(m -> !m.isPagada())
                .mapToDouble(Multa::getMonto)
                .sum();
    }

    // Paga todas las multas pendientes; el monto debe cubrir el total (lo usa BibliotecaService)
    public void pagarMulta(double monto) {
        double totalPendiente = getTotalMultasPendientes();
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a pagar debe ser mayor que 0 (se recibió " + monto + ").");
        }
        if (monto < totalPendiente) {
            throw new IllegalArgumentException("El monto $" + monto + " no cubre el total pendiente de $" + totalPendiente + ".");
        }
        multas.stream()
                .filter(m -> !m.isPagada())
                .forEach(Multa::pagar);
    }

    // ===== Getters (copias de solo lectura para proteger las listas) =====
    @Override                                   // lo exige Identificable
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Prestamo> getPrestamosActivos() {
        return List.copyOf(prestamosActivos);
    }

    public List<Multa> getMultas() {
        return List.copyOf(multas);
    }

    // ===== equals / hashCode por id =====
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

    @Override
    public String toString() {
        return getTipoUsuario() + " [" + id + "] " + nombre + " - préstamos activos: "
                + prestamosActivos.size() + "/" + getLimitePrestamos();
    }
}