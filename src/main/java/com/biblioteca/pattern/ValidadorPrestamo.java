package com.biblioteca.pattern;

import com.biblioteca.model.Usuario;
import com.biblioteca.model.Material;

    @FunctionalInterface
    public interface ValidadorPrestamo {
        boolean validar(Usuario usuario, Material material);
}
