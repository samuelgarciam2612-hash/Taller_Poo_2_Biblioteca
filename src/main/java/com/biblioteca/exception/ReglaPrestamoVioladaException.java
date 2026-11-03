package com.biblioteca.exception;

// Regla de negocio general: no se cumple alguna condición del préstamo (la usa BibliotecaService).
public class ReglaPrestamoVioladaException extends BibliotecaException {

    public ReglaPrestamoVioladaException(String mensaje) {
        super(mensaje);
    }
}