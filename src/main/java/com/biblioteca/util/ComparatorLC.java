package com.biblioteca.util;

import com.biblioteca.model.Material;
import java.util.Comparator;

public class ComparatorLC {

    // Comparador simple con lambda por año
    public static final Comparator<Material> anio = (Material a, Material b) -> {
        return Integer.compare(a.getAnioPublicacion(), b.getAnioPublicacion());
    };

    // Comparador compuesto: año y luego título (en orden inverso)
    public static final Comparator<Material> anio_titulo =
            Comparator.comparing(Material::getAnioPublicacion)
                    .thenComparing(Material::getTitulo)
                    .reversed();
}