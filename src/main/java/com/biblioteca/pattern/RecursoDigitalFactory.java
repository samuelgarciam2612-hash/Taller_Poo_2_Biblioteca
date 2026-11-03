package com.biblioteca.pattern;

import com.biblioteca.model.Material;
import com.biblioteca.model.RecursoDigital;

public class RecursoDigitalFactory implements MaterialFactory {
    @Override
    public Material crearMaterial(String codigo, String titulo, String autor, int anio) {
        return new RecursoDigital(codigo, titulo, autor, anio);
    }
}