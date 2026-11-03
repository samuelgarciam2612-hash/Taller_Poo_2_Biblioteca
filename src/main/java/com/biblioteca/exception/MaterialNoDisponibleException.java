package com.biblioteca.exception;

// Regla de negocio: el material está prestado y no se puede prestar otra vez.
public class MaterialNoDisponibleException extends BibliotecaException {

    public MaterialNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}