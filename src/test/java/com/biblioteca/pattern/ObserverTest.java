package com.biblioteca.pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObserverTest {

    private GestorUsuarios gestorUsuarios;

    private static class ObservadorFalso implements Observador {
        private final String nombre;
        private final List<String> mensajesRecibidos = new ArrayList<>();

        public ObservadorFalso(String nombre) {
            this.nombre = nombre;
        }

        @Override
        public void estaDisponible(String mensaje) {
            mensajesRecibidos.add(mensaje);
        }

        public List<String> getMensajesRecibidos() {
            return mensajesRecibidos;
        }
    }

    @BeforeEach
    void setUp() {
        gestorUsuarios = new GestorUsuarios();
    }

    @Test
    void testearNotificaSoloAlPrimeroFIFO() {
        // metemos a los del equipo a hacer fila
        ObservadorFalso obs1 = new ObservadorFalso("Nicolas");
        ObservadorFalso obs2 = new ObservadorFalso("Samu");
        ObservadorFalso obs3 = new ObservadorFalso("Camila");

        gestorUsuarios.agregarObservador(obs1);
        gestorUsuarios.agregarObservador(obs2);
        gestorUsuarios.agregarObservador(obs3);

        gestorUsuarios.MaterialDisponible("Fundamentos de Java");

        assertEquals(1, obs1.getMensajesRecibidos().size());
        assertEquals(0, obs2.getMensajesRecibidos().size());
        assertEquals(0, obs3.getMensajesRecibidos().size());
    }

    @Test
    void testearNotificacionSucesivaOrdenFIFO() {
        ObservadorFalso obs1 = new ObservadorFalso("Nicolas");
        ObservadorFalso obs2 = new ObservadorFalso("Samu");

        gestorUsuarios.agregarObservador(obs1);
        gestorUsuarios.agregarObservador(obs2);

        // primera devuelta le avisa al primero
        gestorUsuarios.MaterialDisponible("Fundamentos de Java");
        // segunda devuelta avisa al siguiente
        gestorUsuarios.MaterialDisponible("Fundamentos de Java");

        assertEquals(1, obs1.getMensajesRecibidos().size());
        assertEquals(1, obs2.getMensajesRecibidos().size());
        assertTrue(obs1.getMensajesRecibidos().get(0).contains("Fundamentos de Java"));
    }

    @Test
    void testearColaVaciaNoLanzaError() {
        // por si acaso no hay nadie esperando y devuelven el libro
        assertDoesNotThrow(() -> {
            gestorUsuarios.MaterialDisponible("El Principito");
        });
    }

    @Test
    void testearEliminarObservadorFunciona() {
        ObservadorFalso obs1 = new ObservadorFalso("Nicolas");
        ObservadorFalso obs2 = new ObservadorFalso("David");

        gestorUsuarios.agregarObservador(obs1);
        gestorUsuarios.agregarObservador(obs2);
        gestorUsuarios.eliminarObservador(obs1);
        gestorUsuarios.MaterialDisponible("Habitos Atomicos");

        assertEquals(0, obs1.getMensajesRecibidos().size());
        assertEquals(1, obs2.getMensajesRecibidos().size());
    }
}