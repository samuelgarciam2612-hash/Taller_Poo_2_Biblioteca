package com.biblioteca.exception;

// Búsqueda: no existe un usuario con ese id (la usa BibliotecaService).
public class UsuarioNoEncontradoException extends BibliotecaException {

    public UsuarioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}