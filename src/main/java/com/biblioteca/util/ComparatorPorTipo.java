package com.biblioteca.util;
import java.util.Comparator;
import com.biblioteca.model.Material;

public class ComparatorPorTipo implements Comparator<Material> {
    @Override
    public int compare(Material m1, Material m2){
        return m1.getTipo().compareTo(m2.getTipo());
    }
}