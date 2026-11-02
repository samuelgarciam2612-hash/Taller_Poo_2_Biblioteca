package com.biblioteca.util;
import com.biblioteca.model.Material;
import java.util.Comparator;

// lambda y compuesto abreviado ps el titulo LC
public class ComparatorLC {

    //lambda
    public static final Comparator<Material> anio = (Material a, Material b) -> {
        return Integer.compare(a.getAnioPublicacion(), b.getAnioPublicacion());
    };

    //compuesto
    public static final Comparator<Material> anio_titulo =
            Comparator.comparing(Material::getAnioPublicacion)
                    .thenComparing(Material::getTitulo)
                    .reversed();

}