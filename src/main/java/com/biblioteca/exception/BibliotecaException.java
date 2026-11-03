package com.biblioteca.exception;

// Excepción base del dominio: todas las de la biblioteca heredan de esta.
// Hereda de Exception (chequeada): Java obliga a capturarla o declararla con throws.
public class BibliotecaException extends Exception {

    public BibliotecaException(String mensaje) {
        super(mensaje);
    }

    // Para envolver otra excepción sin perder la causa original
    public BibliotecaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}