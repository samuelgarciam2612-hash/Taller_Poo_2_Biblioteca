package com.biblioteca.model;

import com.biblioteca.exception.BibliotecaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PrestamoTest {

    private static final LocalDate INICIO = LocalDate.of(2026, 11, 1);

    private Estudiante usuario;
    private Libro libro;
    private Prestamo prestamo;

    @BeforeEach
    void setUp() throws BibliotecaException {
        usuario = new Estudiante("U1", "Ana");
        libro = new Libro("L1", "Cien años de soledad", "García Márquez", 1967);
        prestamo = new Prestamo(usuario, libro, INICIO, INICIO.plusDays(14));   // vence el 15/11
        usuario.registrarPrestamo(prestamo);
    }

    @Test
    @DisplayName("Un préstamo nuevo está activo, sin multa y con un id propio")
    void prestamoNuevoActivo() {
        Prestamo otro = new Prestamo(usuario, libro, INICIO, INICIO.plusDays(14));

        assertTrue(prestamo.estaActivo());
        assertNull(prestamo.getFechaDevolucionReal(), "Sin devolución todavía");
        assertEquals(0, prestamo.getMultaGenerada());
        assertNotEquals(prestamo.getId(), otro.getId(), "Cada préstamo genera un id distinto");
    }

    @Test
    @DisplayName("Un préstamo necesita usuario y una fecha límite posterior al inicio")
    void datosInvalidosLanzanExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Prestamo(null, libro, INICIO, INICIO.plusDays(14)));
        assertThrows(IllegalArgumentException.class,
                () -> new Prestamo(usuario, libro, INICIO, INICIO.minusDays(1)));
    }

    @Test
    @DisplayName("Devolver a tiempo no genera multa, cierra el préstamo y no se puede repetir")
    void devolucionATiempo() {
        prestamo.setFechaDevolucionReal(INICIO.plusDays(10));

        assertFalse(prestamo.estaActivo());
        assertTrue(prestamo.getMulta().isEmpty(), "Sin retraso, la multa es un Optional vacío");
        assertEquals(0, usuario.getPrestamosActivos().size(), "Sale de los préstamos activos");
        assertThrows(IllegalStateException.class,
                () -> prestamo.setFechaDevolucionReal(INICIO.plusDays(11)), "No se devuelve dos veces");
    }

    @Test
    @DisplayName("Devolver 3 días tarde genera una multa de $3.000 para el usuario")
    void devolucionTardeGeneraMulta() {
        prestamo.setFechaDevolucionReal(INICIO.plusDays(17));   // 3 días después del 15/11

        assertEquals(3000, prestamo.getMultaGenerada(), "3 días × $1.000 de libro");
        assertEquals("Libro", prestamo.getMulta().orElseThrow().getTipoMaterial());
        assertTrue(usuario.tieneMultasPendientes(), "La multa queda asignada al usuario");
    }

    @Test
    @DisplayName("Un préstamo activo está vencido después de su fecha límite")
    void estaVencido() {
        assertFalse(prestamo.estaVencido(INICIO.plusDays(14)), "El día límite todavía no está vencido");
        assertTrue(prestamo.estaVencido(INICIO.plusDays(15)));
    }

    @Test
    @DisplayName("Una multa necesita monto positivo y se paga una sola vez")
    void multaValidaYSePagaUnaVez() {
        assertThrows(IllegalArgumentException.class, () -> new Multa("M1", 0, INICIO, "Libro"));

        Multa multa = new Multa("M2", 1000, INICIO, "Libro");
        multa.pagar();

        assertTrue(multa.isPagada());
        assertThrows(IllegalStateException.class, multa::pagar);
    }
}