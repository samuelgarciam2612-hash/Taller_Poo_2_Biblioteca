package com.biblioteca.pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ColaEsperaTest {

    private ColaEspera<String> cola;

    @BeforeEach
    void setUp() {
        cola = new ColaEspera<>();
    }

    @Test
    void testEncolarYAtender() {
        cola.encolar("Usuario 1");

        String atendido = cola.atenderSiguiente();

        assertNotNull(atendido, "Debe atender al usuario en espera");
        assertEquals("Usuario 1", atendido, "El usuario atendido debe ser 'Usuario 1'");
    }

    @Test
    void testAtenderSiguienteOrdenFifo() {
        // Probar que atienda en orden de llegada (primero en entrar, primero en salir)
        cola.encolar("Primero");
        cola.encolar("Segundo");

        String atendido = cola.atenderSiguiente();

        assertNotNull(atendido, "Debe atender al primer elemento");
        assertEquals("Primero", atendido, "Debe atender a 'Primero'");
    }

    @Test
    void testAtenderColaVacia() {
        // Al estar vacía la cola, atenderSiguiente() retorna null
        String atendido = cola.atenderSiguiente();

        assertNull(atendido, "Si la cola está vacía, debe retornar null");
    }

    /*
     * RESULTADO ESPERADO AL EJECUTAR:
     * - Deben pasar los 3 tests en verde sin problemas.
     * - En la consola van a salir mensajes INFO cada vez que se encola o atiende alguien,
     *   y saldrá un WARNING en rojo al intentar atender la cola vacía porque el Logger
     *   avisa de la situación. Es el comportamiento normal de la bitácora.
     */
}