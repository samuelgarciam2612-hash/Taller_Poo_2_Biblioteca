package com.biblioteca.pattern;
import com.biblioteca.model.Material;
import com.biblioteca.model.TrabajoDeGrado;

public class TrabajoDeGradoFactory implements MaterialFactory {
    @Override
    public Material crearMaterial(String codigo, String titulo, String autor, int anio) {
        return new Revista(codigo, titulo, autor, anio);
    }
}