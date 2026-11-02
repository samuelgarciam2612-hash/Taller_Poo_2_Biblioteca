package com.biblioteca.pattern;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Material;

public class LibroFactory implements MaterialFactory {
    @Override
    public Material crearMaterial(String codigo, String titulo, String autor, int anio) {
        return new Libro(codigo, titulo, autor, anio);
    }
}