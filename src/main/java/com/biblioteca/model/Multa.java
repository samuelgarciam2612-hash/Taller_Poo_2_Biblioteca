package com.biblioteca.model;

import java.time.LocalDate;
import java.util.Objects;

// Multa generada cuando un material se devuelve tarde
public class Multa implements Identificable {

    private final String id;
    private final double monto;
    private final LocalDate fechaGeneracion;
    private final String tipoMaterial;         // para el reporte de multas por tipo de material
    private boolean pagada;                    // empieza pendiente

    public Multa(String id, double monto, LocalDate fechaGeneracion, String tipoMaterial) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id de la multa no puede estar vacío.");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto de la multa debe ser mayor que 0 (se recibió " + monto + ").");
        }
        if (fechaGeneracion == null) {
            throw new IllegalArgumentException("La fecha de generación de la multa no puede ser nula.");
        }
        if (tipoMaterial == null || tipoMaterial.isBlank()) {
            throw new IllegalArgumentException("El tipo de material de la multa no puede estar vacío.");
        }
        this.id = id;
        this.monto = monto;
        this.fechaGeneracion = fechaGeneracion;
        this.tipoMaterial = tipoMaterial;
        this.pagada = false;
    }

    public void pagar() {
        if (pagada) {
            throw new IllegalStateException("La multa " + id + " ya estaba pagada.");
        }
        pagada = true;
    }

    @Override                                   // lo exige Identificable
    public String getId() {
        return id;
    }

    public double getMonto() {
        return monto;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public String getTipoMaterial() {
        return tipoMaterial;
    }

    public boolean isPagada() {
        return pagada;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Multa)) return false;
        Multa otra = (Multa) o;
        return Objects.equals(id, otra.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Multa [" + id + "] $" + monto + " (" + tipoMaterial + ", " + fechaGeneracion + ") - "
                + (pagada ? "Pagada" : "Pendiente");
    }
}