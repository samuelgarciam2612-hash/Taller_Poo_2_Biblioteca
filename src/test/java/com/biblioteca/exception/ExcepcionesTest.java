package com.biblioteca.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExcepcionesTest {

    @Test
    @DisplayName("Las 7 excepciones heredan de BibliotecaException, que es chequeada")
    void jerarquiaDeExcepciones() {
        List<BibliotecaException> excepciones = List.of(   // compila solo si todas son BibliotecaException
                new UsuarioConMultasPendientesException("m"),
                new LimitePrestamosExcedidoException("m"),
                new MaterialNoDisponibleException("m"),
                new UsuarioNoEncontradoException("m"),
                new MaterialNoEncontradoException("m"),
                new PrestamoNoEncontradoException("m"),
                new ReglaPrestamoVioladaException("m"));

        assertEquals(7, excepciones.size());
        assertTrue(Exception.class.isAssignableFrom(BibliotecaException.class));
        assertFalse(RuntimeException.class.isAssignableFrom(BibliotecaException.class),
                "Chequeada: no hereda de RuntimeException");
    }

    @Test
    @DisplayName("La excepción conserva su mensaje y la causa original")
    void conservaMensajeYCausa() {
        IllegalStateException causa = new IllegalStateException("causa");
        BibliotecaException e = new BibliotecaException("El libro ya está prestado", causa);

        assertEquals("El libro ya está prestado", e.getMessage());
        assertEquals(causa, e.getCause());
    }

    @Test
    @DisplayName("Un catch de BibliotecaException atrapa cualquiera de sus hijas")
    void catchDeLaBaseAtrapaHijas() {
        BibliotecaException atrapada = assertThrows(BibliotecaException.class, () -> {
            throw new LimitePrestamosExcedidoException("límite superado");
        });

        assertTrue(atrapada instanceof LimitePrestamosExcedidoException);
    }
}