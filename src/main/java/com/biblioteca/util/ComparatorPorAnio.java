package com.biblioteca.util;
import java.util.Comparator;
import com.biblioteca.model.Material;

public class ComparatorPorAnio implements Comparator<Material> {
    @Override
    public int compare(Material m1, Material m2){
        return (m1.getAnio() - m2.getAnio());
    }
}