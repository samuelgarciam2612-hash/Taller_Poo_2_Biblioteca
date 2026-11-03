package com.biblioteca.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

// Registro de que un usuario se llevó un material
public class Prestamo implements Identificable {

    private static int contadorPrestamos = 0;  // compartido: genera ids P-1, P-2...

    private final String id;
    private final Usuario usuario;
    private final Material material;
    private final LocalDate fechaInicio;
    private final LocalDate fechaLimiteDevolucion;
    private LocalDate fechaDevolucionReal;     // null mientras el préstamo sigue activo
    private Multa multa;                       // null si se devolvió a tiempo

    // Firma exacta que usa BibliotecaService: new Prestamo(usuario, material, inicio, limite)
    public Prestamo(Usuario usuario, Material material, LocalDate fechaInicio, LocalDate fechaLimiteDevolucion) {
        if (usuario == null || material == null) {
            throw new IllegalArgumentException("El préstamo necesita un usuario y un material.");
        }
        if (fechaInicio == null || fechaLimiteDevolucion == null) {
            throw new IllegalArgumentException("Las fechas del préstamo no pueden ser nulas.");
        }
        if (fechaLimiteDevolucion.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha límite no puede ser anterior a la fecha de inicio.");
        }
        contadorPrestamos++;
        this.id = "P-" + contadorPrestamos;
        this.usuario = usuario;
        this.material = material;
        this.fechaInicio = fechaInicio;
        this.fechaLimiteDevolucion = fechaLimiteDevolucion;
    }

    // Registra la devolución: si hay retraso genera la multa y se la asigna al usuario
    public void setFechaDevolucionReal(LocalDate fechaDevolucion) {
        if (fechaDevolucion == null) {
            throw new IllegalArgumentException("La fecha de devolución no puede ser nula.");
        }
        if (fechaDevolucion.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La devolución no puede ser anterior al inicio del préstamo.");
        }
        if (!estaActivo()) {
            throw new IllegalStateException("El préstamo " + id + " ya fue devuelto.");
        }
        this.fechaDevolucionReal = fechaDevolucion;

        long diasRetraso = ChronoUnit.DAYS.between(fechaLimiteDevolucion, fechaDevolucion);
        if (diasRetraso > 0) {                 // la multa depende del tipo de material (polimorfismo)
            double monto = material.calcularMulta((int) diasRetraso);
            this.multa = new Multa(id + "-M", monto, fechaDevolucion, material.getTipo());
            usuario.agregarMulta(multa);
        }
        usuario.finalizarPrestamo(this);
    }

    public boolean estaActivo() {
        return fechaDevolucionReal == null;
    }

    public boolean estaVencido(LocalDate hoy) {
        return estaActivo() && fechaLimiteDevolucion.isBefore(hoy);
    }

    // Lo usa BibliotecaService para el reporte de multas: 0 si no hubo multa
    public double getMultaGenerada() {
        return multa == null ? 0 : multa.getMonto();
    }

    // Optional: si no hubo multa, caja vacía en vez de null
    public Optional<Multa> getMulta() {
        return Optional.ofNullable(multa);
    }

    @Override                                   // lo exige Identificable
    public String getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Material getMaterial() {
        return material;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaLimiteDevolucion() {
        return fechaLimiteDevolucion;
    }

    // Devuelve null si sigue activo (BibliotecaService compara con null)
    public LocalDate getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prestamo)) return false;
        Prestamo otro = (Prestamo) o;
        return Objects.equals(id, otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Préstamo [" + id + "] " + material.getTitulo() + " → " + usuario.getNombre()
                + " (vence " + fechaLimiteDevolucion + ")" + (estaActivo() ? " - Activo" : " - Devuelto");
    }
}