package com.biblioteca.exception;

// Búsqueda: no existe un material con ese id (la usa BibliotecaService).
public class MaterialNoEncontradoException extends BibliotecaException {

    public MaterialNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}