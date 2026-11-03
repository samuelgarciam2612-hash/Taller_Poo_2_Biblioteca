package com.biblioteca.util;

import com.biblioteca.model.Material;
import java.util.Comparator;

public class ComparatorPorAnio implements Comparator<Material> {

    @Override
    public int compare(Material m1, Material m2) {
        return Integer.compare(m1.getAnioPublicacion(), m2.getAnioPublicacion());
    }
}