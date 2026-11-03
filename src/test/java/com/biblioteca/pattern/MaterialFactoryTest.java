package com.biblioteca.pattern;

import com.biblioteca.model.Libro;
import com.biblioteca.model.Material;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaterialFactoryTest {

    private MaterialFactory libroFactory;

    @BeforeEach
    void setUp() {
        libroFactory = new LibroFactory();
    }

    @Test
    void testearCrearLibroCorrectamente() {
        Material material = libroFactory.crearMaterial("L001", "Libro Troll del Rubius", "Rubius", 2017);

        assertNotNull(material, "El material creado no debe ser nulo");
        assertTrue(material instanceof Libro, "El objeto debe ser una instancia de Libro");
        assertEquals("L001", material.getId());
        assertEquals("Libro Troll del Rubius", material.getTitulo());
        assertEquals("Rubius", material.getAutor());
        assertEquals(2017, material.getAnioPublicacion());
        assertEquals("Libro", material.getTipo());
    }

    @Test
    void testearPermitirUsoPolimorfico() {
        MaterialFactory fabrica = new LibroFactory();
        Material material = fabrica.crearMaterial("L002", "El Principito", "Antoine de Saint Exupery", 1943);

        assertInstanceOf(Libro.class, material);
        assertEquals("El Principito", material.getTitulo());
    }

    @Test
    void testearDatosInvalidosLanzaExcepcion() {
        // ID vacío
        assertThrows(IllegalArgumentException.class, () -> {
            libroFactory.crearMaterial("", "Hábitos Atómicos", "James Clear", 2018);
        });

        // Título vacío
        assertThrows(IllegalArgumentException.class, () -> {
            libroFactory.crearMaterial("L001", "", "James Clear", 2018);
        });

        // Año negativo
        assertThrows(IllegalArgumentException.class, () -> {
            libroFactory.crearMaterial("L002", "Hábitos Atómicos", "James Clear", -2018);
        });
    }
}