package com.biblioteca.exception;

// Regla de negocio: el usuario ya tiene el máximo de préstamos simultáneos de su tipo.

public class LimitePrestamosExcedidoException extends BibliotecaException {

    public LimitePrestamosExcedidoException(String mensaje) {
        super(mensaje);
    }
}