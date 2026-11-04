package com.biblioteca.model;

import com.biblioteca.exception.MaterialNoDisponibleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaterialTest {

    private Libro libro;

    @BeforeEach
    void setUp() {
        // Cada prueba arranca con un libro nuevo y disponible
        libro = new Libro("L1", "Cien años de soledad", "García Márquez", 1967);
    }

    @Test
    @DisplayName("Un material nuevo guarda sus datos y queda disponible")
    void constructorGuardaDatos() {
        assertEquals("L1", libro.getId());
        assertEquals("Cien años de soledad", libro.getTitulo());
        assertEquals("García Márquez", libro.getAutor());
        assertEquals(1967, libro.getAnioPublicacion());
        assertTrue(libro.isDisponible(), "Todo material nuevo debe llegar disponible");
    }

    @Test
    @DisplayName("No se puede crear un material con id vacío, sin autor o con año negativo")
    void datosInvalidosLanzanExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Libro("", "Título", "Autor", 2020));
        assertThrows(IllegalArgumentException.class, () -> new Revista("R1", "Semana", " ", 2020));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new TrabajoDeGrado("T1", "Tesis", "Ana", -5));
        assertTrue(e.getMessage().contains("-5"), "El mensaje debe indicar el valor recibido");
    }

    @Test
    @DisplayName("Cada tipo de material define su tipo, sus días y su multa (polimorfismo)")
    void cadaTipoDefineSusReglas() {
        Material revista = new Revista("R1", "Semana", "Varios", 2024);
        Material trabajo = new TrabajoDeGrado("T1", "Tesis", "Ana", 2025);
        Material digital = new RecursoDigital("D1", "Curso Java", "EIA", 2023);

        assertEquals("Libro", libro.getTipo());
        assertEquals(14, libro.getDiasPrestamo());
        assertEquals(1000, libro.getTarifaMultaDiaria());

        assertEquals(7, revista.getDiasPrestamo());
        assertEquals(500, revista.getTarifaMultaDiaria());

        assertEquals(3, trabajo.getDiasPrestamo());
        assertEquals(2000, trabajo.getTarifaMultaDiaria());

        assertEquals(7, digital.getDiasPrestamo());
        assertEquals(300, digital.getTarifaMultaDiaria());
    }

    @Test
    @DisplayName("La multa usa la tarifa del tipo y no acepta días negativos")
    void calcularMultaUsaTarifaDelTipo() {
        Material revista = new Revista("R1", "Semana", "Varios", 2024);

        assertEquals(3000, libro.calcularMulta(3), "3 días × $1.000 de libro");
        assertEquals(1500, revista.calcularMulta(3), "3 días × $500 de revista");
        assertEquals(0, libro.calcularMulta(0), "Sin retraso no hay multa");
        assertThrows(IllegalArgumentException.class, () -> libro.calcularMulta(-1));
    }

    @Test
    @DisplayName("Un material prestado no se puede volver a prestar hasta devolverlo")
    void prestarYDevolver() throws MaterialNoDisponibleException {
        libro.prestar();
        assertFalse(libro.isDisponible());
        assertThrows(MaterialNoDisponibleException.class, () -> libro.prestar());

        libro.devolver();
        assertTrue(libro.isDisponible());
    }

    @Test
    @DisplayName("La fila de espera respeta el orden de llegada y vacía devuelve Optional vacío")
    void filaDeEsperaEsFifo() {
        Usuario ana = new Estudiante("U1", "Ana");
        Usuario luis = new Docente("U2", "Luis");

        libro.agregarALaFilaDeEspera(ana);
        libro.agregarALaFilaDeEspera(luis);

        assertEquals(2, libro.getCantidadEnEspera());
        assertEquals(ana, libro.sacarSiguienteEnEspera().orElseThrow(), "Primero en llegar, primero en salir");
        assertEquals(luis, libro.sacarSiguienteEnEspera().orElseThrow());
        assertTrue(libro.sacarSiguienteEnEspera().isEmpty(), "Si nadie espera, no devuelve null");
    }

    @Test
    @DisplayName("Dos materiales con el mismo id son el mismo material")
    void equalsPorId() {
        Material copia = new Libro("L1", "Otro título", "Otro autor", 2000);

        assertEquals(libro, copia);
        assertEquals(libro.hashCode(), copia.hashCode());
    }

    @Test
    @DisplayName("El orden natural es por título, ignora tildes y desempata por id")
    void ordenNaturalPorTitulo() {
        Material algebra = new Libro("L2", "Álgebra", "Baldor", 1941);
        Material semana = new Revista("R1", "semana", "Varios", 2024);
        Material otroEjemplar = new Libro("L9", "Cien años de soledad", "García Márquez", 1967);

        List<Material> lista = new ArrayList<>(List.of(semana, otroEjemplar, libro, algebra));
        Collections.sort(lista);   // usa compareTo de Material

        assertEquals(algebra, lista.get(0), "Álgebra va primero aunque empiece con tilde");
        assertEquals(libro, lista.get(1), "Mismo título: L1 va antes que L9");
        assertEquals(otroEjemplar, lista.get(2));
        assertEquals(semana, lista.get(3));
    }
}