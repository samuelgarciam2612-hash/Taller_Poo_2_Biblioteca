package com.biblioteca.util;

import com.biblioteca.model.Libro;
import com.biblioteca.model.Material;
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
        // probando la clase que ordena por fecha de publicacion
        materiales.sort(new ComparatorPorAnio());
        assertEquals(2014, materiales.get(0).getAnioPublicacion());
        assertEquals(2023, materiales.get(2).getAnioPublicacion());
    }

    @Test
    void testearOrdenarPorTipo() {
        // revisando que agrupe o compare por tipo de material
        materiales.sort(new ComparatorPorTipo());
        assertEquals(3, materiales.size()); //revisa q siga teniendo los 3 elementos
        assertEquals("Libro", materiales.get(0).getTipo());
    }

    @Test
    void testearOrdenarPorMulta() {
        // le asignamos multas de prueba a los libros rapido
        materiales.get(0).setTarifaMultaDiaria(1500.0);
        materiales.get(1).setTarifaMultaDiaria(500.0);
        materiales.get(2).setTarifaMultaDiaria(1000.0);

        materiales.sort(new ComparatorPorMulta());

        assertEquals(500.0, materiales.get(0).getTarifaMultaDiaria());
        assertEquals(1500.0, materiales.get(2).getTarifaMultaDiaria());
    }

    @Test
    void testearComparatorLCAnio() {
        // probando la constante de anio del ComparatorLC
        materiales.sort(ComparatorLC.anio); //diferente por ser un lambda

        assertEquals(2014, materiales.get(0).getAnioPublicacion());
    }

    @Test
    void testearComparatorAnioTituloInvertido() {
        // anio_titulo: año mas reciente primero y si empatan titulo de Z a A
        materiales.sort(ComparatorLC.anio_titulo);

        // el mas nuevo 2023 tiene que saltar de primero de una vez
        assertEquals(2023, materiales.get(0).getAnioPublicacion());
        assertEquals("Estructuras de Datos", materiales.get(0).getTitulo());
    }
}