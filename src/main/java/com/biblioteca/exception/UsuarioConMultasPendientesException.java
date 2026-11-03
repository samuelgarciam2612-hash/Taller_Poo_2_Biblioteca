package com.biblioteca.exception;

// Regla de negocio: a quien tiene multas pendientes no se le presta nada.
public class UsuarioConMultasPendientesException extends BibliotecaException {

    public UsuarioConMultasPendientesException(String mensaje) {
        super(mensaje);
    }
}