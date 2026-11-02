package com.biblioteca.pattern;
import com.biblioteca.model.Material;

public interface MaterialFactory {
    Material crearMaterial(String codigo, String titulo, String autor, int anio);
}