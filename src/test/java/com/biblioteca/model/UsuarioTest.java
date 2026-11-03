package com.biblioteca.model;

import com.biblioteca.exception.BibliotecaException;
import com.biblioteca.exception.LimitePrestamosExcedidoException;
import com.biblioteca.exception.UsuarioConMultasPendientesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private static final LocalDate HOY = LocalDate.of(2026, 11, 1);   // fecha fija: la prueba no depende del día

    private Estudiante estudiante;

    @BeforeEach
    void setUp() {
        estudiante = new Estudiante("U1", "Ana");
    }

    // Crea un préstamo de un libro nuevo para el usuario
    private Prestamo nuevoPrestamo(Usuario usuario, String idLibro) {
        Libro libro = new Libro(idLibro, "Libro " + idLibro, "Autor", 2020);
        return new Prestamo(usuario, libro, HOY, HOY.plusDays(libro.getDiasPrestamo()));
    }

    // Deja al estudiante con una multa de $2.000 (libro devuelto 2 días tarde)
    private void generarMulta() throws BibliotecaException {
        Prestamo prestamo = nuevoPrestamo(estudiante, "L1");
        estudiante.registrarPrestamo(prestamo);
        prestamo.setFechaDevolucionReal(prestamo.getFechaLimiteDevolucion().plusDays(2));
    }

    @Test
    @DisplayName("No se puede crear un usuario con id o nombre vacío")
    void datosInvalidosLanzanExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Estudiante("", "Ana"));
        assertThrows(IllegalArgumentException.class, () -> new Docente("U2", ""));
    }

    @Test
    @DisplayName("Estudiante y docente tienen límites distintos (polimorfismo)")
    void limitesPorTipoDeUsuario() {
        Docente docente = new Docente("U2", "Luis");

        assertEquals(3, estudiante.getLimitePrestamos());
        assertEquals(5, docente.getLimitePrestamos());
        assertEquals("Estudiante", estudiante.getTipoUsuario());
        assertEquals("Docente", docente.getTipoUsuario());
    }

    @Test
    @DisplayName("Un estudiante puede tener hasta 3 préstamos y el cuarto se rechaza")
    void estudianteSuperaLimite() throws BibliotecaException {
        estudiante.registrarPrestamo(nuevoPrestamo(estudiante, "L1"));
        estudiante.registrarPrestamo(nuevoPrestamo(estudiante, "L2"));
        estudiante.registrarPrestamo(nuevoPrestamo(estudiante, "L3"));

        assertEquals(3, estudiante.getPrestamosActivos().size());
        assertThrows(LimitePrestamosExcedidoException.class,
                () -> estudiante.registrarPrestamo(nuevoPrestamo(estudiante, "L4")));
        assertThrows(UnsupportedOperationException.class,
                () -> estudiante.getPrestamosActivos().clear(), "La lista no se modifica desde afuera");
    }

    @Test
    @DisplayName("Con multas pendientes no se le presta nada")
    void conMultaNoPuedePrestar() throws BibliotecaException {
        assertFalse(estudiante.tieneMultasPendientes(), "Un usuario nuevo no debe nada");

        generarMulta();

        assertTrue(estudiante.tieneMultasPendientes());
        assertThrows(UsuarioConMultasPendientesException.class,
                () -> estudiante.registrarPrestamo(nuevoPrestamo(estudiante, "L2")));
    }

    @Test
    @DisplayName("Un pago insuficiente se rechaza y pagar el total salda las multas")
    void pagarMulta() throws BibliotecaException {
        generarMulta();   // debe $2.000

        assertThrows(IllegalArgumentException.class, () -> estudiante.pagarMulta(500));
        assertTrue(estudiante.tieneMultasPendientes(), "Con $500 la multa sigue pendiente");

        estudiante.pagarMulta(2000);
        assertFalse(estudiante.tieneMultasPendientes());
        assertEquals(0, estudiante.getTotalMultasPendientes());
    }
}