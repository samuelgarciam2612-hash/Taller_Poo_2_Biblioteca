package com.biblioteca.exception;

// Búsqueda: no hay un préstamo activo para ese material (la usa BibliotecaService).
public class PrestamoNoEncontradoException extends BibliotecaException {

    public PrestamoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}