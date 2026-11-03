package com.biblioteca.util;

import com.biblioteca.model.Audiovisual;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Material;
import com.biblioteca.model.Revista;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComparatorTest {

    private List<Material> materiales;

    @BeforeEach
    void setUp() {
        materiales = new ArrayList<>();
        materiales.add(new Libro("001", "Calculo En Varias Variables", "Gabriel Castaño", 2020));
        materiales.add(new Libro("002", "Libro troll del Rubius", "Rubius", 2014));
        materiales.add(new Libro("003", "Estructuras de Datos", "Isis", 2023));
    }

    @Test
    void testearOrdenarPorAnio() {
        materiales.sort(new ComparatorPorAnio());
        assertEquals(2014, materiales.get(0).getAnioPublicacion());
        assertEquals(2023, materiales.get(2).getAnioPublicacion());
    }

    @Test
    void testearOrdenarPorTipo() {
        materiales.sort(new ComparatorPorTipo());
        assertEquals(3, materiales.size());
        assertEquals("Libro", materiales.get(0).getTipo());
    }

    @Test
    void testearOrdenarPorMulta() {
        List<Material> diversosMateriales = new ArrayList<>();
        diversosMateriales.add(new Libro("001", "Calculo En Varias Variables", "Gabriel Castaño", 2020));
        diversosMateriales.add(new Revista("002", "IEEE Software", "IEEE", 2021));
        diversosMateriales.add(new Audiovisual("003", "Video Conferencia POO", "Universidad", 2022));

        diversosMateriales.sort(new ComparatorPorMulta());

        double multaPrimero = diversosMateriales.get(0).getTarifaMultaDiaria();
        double multaSegundo = diversosMateriales.get(1).getTarifaMultaDiaria();
        double multaTercero = diversosMateriales.get(2).getTarifaMultaDiaria();

        assertTrue(multaPrimero <= multaSegundo);
        assertTrue(multaSegundo <= multaTercero);
    }

    @Test
    void testearComparatorLCAnio() {
        materiales.sort(ComparatorLC.anio);
        assertEquals(2014, materiales.get(0).getAnioPublicacion());
    }

    @Test
    void testearComparatorAnioTituloInvertido() {
        materiales.sort(ComparatorLC.anio_titulo);
        assertEquals(2023, materiales.get(0).getAnioPublicacion());
        assertEquals("Estructuras de Datos", materiales.get(0).getTitulo());
    }
}