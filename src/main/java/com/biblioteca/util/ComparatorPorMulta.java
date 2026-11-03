package com.biblioteca.util;

import com.biblioteca.model.Material;
import java.util.Comparator;

public class ComparatorPorMulta implements Comparator<Material> {

    @Override
    public int compare(Material m1, Material m2) {
        return Double.compare(m1.getTarifaMultaDiaria(), m2.getTarifaMultaDiaria());
    }
}

//valores decimales pueden dar error por eso es diferente al otro aunque sean numeros