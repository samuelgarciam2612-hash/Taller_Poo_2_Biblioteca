package com.biblioteca.repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class EstanteriaTest {

    private Estanteria<ElementoPrueba> estanteria;

    static class ElementoPrueba implements com.biblioteca.model.Identificable {
        private String id;
        private String nombre;

        public ElementoPrueba(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        @Override
        public String getId() {
            return id;
        }
    }
    @BeforeEach
    void setUp() {
        // Esto se ejecuta antes de cada prueba para limpiar la estantería y empezar desde cero
        estanteria = new Estanteria<>();
    }

    @Test
    void testGuardarYBuscarPorId() {
        // Preparar un elemento nuevo
        ElementoPrueba libro = new ElementoPrueba("L001", "Cien Años de Soledad");

        // Actuar: Guardarlo en la estantería
        estanteria.guardar(libro);

        // Verificar: Buscarlo por su ID y comprobar que sí lo encontró
        Optional<ElementoPrueba> encontrado = estanteria.buscarPorId("L001");

        assertTrue(encontrado.isPresent(), "El elemento debería encontrarse en la estantería");
        assertEquals("Cien Años de Soledad", encontrado.get().nombre, "El nombre del elemento debe coincidir");
    }
    @Test
    void testEliminarElemento() {
        // Preparar y guardar un elemento
        ElementoPrueba libro = new ElementoPrueba("L002", "El Principito");
        estanteria.guardar(libro);

        // Actuar: Eliminarlo por su ID
        estanteria.eliminar("L002");

        // Verificar: Buscarlo de nuevo y comprobar que ya no está (debe estar vacío)
        Optional<ElementoPrueba> encontrado = estanteria.buscarPorId("L002");
        assertFalse(encontrado.isPresent(), "El elemento eliminado ya no debería existir en la estantería");
    }
    @Test
    void testListarTodos() {
        // Guardar varios elementos
        estanteria.guardar(new ElementoPrueba("1", "Libro A"));
        estanteria.guardar(new ElementoPrueba("2", "Libro B"));

        // Verificar que la lista contenga exactamente los 2 elementos
        var lista = estanteria.listarTodos();
        assertEquals(2, lista.size(), "La estantería debería tener exactamente 2 elementos");
    }
    /*
     * RESULTADO ESPERADO AL EJECUTAR:
     * - Deben pasar los 3 tests en verde sin problemas.
     * - En la consola van a salir mensajes informativos (INFO) del Logger avisando
     *   cada vez que se guarda o se elimina un elemento. Es parte de la bitácora normal.
     */
}
