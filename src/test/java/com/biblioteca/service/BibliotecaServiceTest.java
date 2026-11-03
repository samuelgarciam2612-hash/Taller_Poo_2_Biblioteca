package com.biblioteca.service;

import com.biblioteca.exception.*;
import com.biblioteca.model.Estudiante;
import com.biblioteca.model.Usuario;
import com.biblioteca.model.Material;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BibliotecaServiceTest {

    private BibliotecaService bibliotecaService;

    @BeforeEach
    void setUp() {
        bibliotecaService = new BibliotecaService();
    }

    @Test
    @DisplayName("Debe lanzar UsuarioNoEncontradoException al operar con un usuario inexistente")
    void testBuscarUsuarioInexistente() {
        assertThrows(
                UsuarioNoEncontradoException.class,
                () -> bibliotecaService.cobrarMulta("U_INEXISTENTE", 50.0),
                "Debería lanzar UsuarioNoEncontradoException cuando el usuario no existe"
        );
    }

    @Test
    @DisplayName("Debe lanzar MaterialNoEncontradoException al intentar prestar un material inexistente")
    void testBuscarMaterialInexistente() {
        assertThrows(
                MaterialNoEncontradoException.class,
                () -> bibliotecaService.prestarMaterial("U001", "M_INEXISTENTE"),
                "Debería lanzar MaterialNoEncontradoException si el material no existe"
        );
    }

    @Test
    @DisplayName("Debe lanzar ReglaPrestamoVioladaException si el material no está disponible")
    void testPrestarMaterialReglaViolada() {
        Usuario usuario = new Estudiante("U001", "Juan Pérez");
        Libro material = new Libro("M001", "Libro Java", "Autor Prueba", 2023);
        material.setDisponible(false);

        bibliotecaService.getUsuarios().add(usuario);
        bibliotecaService.getMateriales().add(material);

        assertThrows(
                ReglaPrestamoVioladaException.class,
                () -> bibliotecaService.prestarMaterial("U001", "M001"),
                "Debería lanzar ReglaPrestamoVioladaException cuando el material no está disponible"
        );
    }

    @Test
    @DisplayName("Debe lanzar PrestamoNoEncontradoException al intentar devolver un material sin préstamo activo")
    void testDevolucionSinPrestamoActivo() {
        assertThrows(
                PrestamoNoEncontradoException.class,
                () -> bibliotecaService.devolverMaterial("M_INEXISTENTE"),
                "Debería lanzar PrestamoNoEncontradoException si no existe un préstamo activo para ese material"
        );
    }

    @Test
    @DisplayName("Debe realizar un préstamo con éxito cuando el usuario y el material son válidos y disponibles")
    void testPrestarMaterialExitoso() {
        Usuario usuario = new Estudiante("U001", "Juan Pérez");
        Libro material = new Libro("M001", "Libro Java", "Autor Prueba", 2023);
        material.setDisponible(true);

        bibliotecaService.getUsuarios().add(usuario);
        bibliotecaService.getMateriales().add(material);

        assertDoesNotThrow(() -> {
            bibliotecaService.prestarMaterial("U001", "M001");
        }, "El préstamo debería realizarse correctamente sin lanzar ninguna excepción");
    }

    @Test
    @DisplayName("Debe registrar la devolución con éxito usando solo el ID del material")
    void testDevolverMaterialExitoso() throws ReglaPrestamoVioladaException, MaterialNoEncontradoException, UsuarioNoEncontradoException {
        Usuario usuario = new Estudiante("U001", "Juan Pérez");
        Libro material = new Libro("M001", "Libro Java", "Autor Prueba", 2023);
        material.setDisponible(true);

        bibliotecaService.getUsuarios().add(usuario);
        bibliotecaService.getMateriales().add(material);

        bibliotecaService.prestarMaterial("U001", "M001");

        assertDoesNotThrow(() -> {
            bibliotecaService.devolverMaterial("M001");
        }, "La devolución debería completarse con éxito");
    }

    @Test
    @DisplayName("Debe cobrar una multa con éxito a un usuario válido")
    void testCobrarMultaExitoso() {
        Usuario usuario = new Estudiante("U001", "Juan Pérez");
        bibliotecaService.getUsuarios().add(usuario);

        assertDoesNotThrow(() -> {
            bibliotecaService.cobrarMulta("U001", 20.0);
        }, "El cobro de la multa debería realizarse sin lanzar excepciones");
    }

    @Test
    @DisplayName("Debe registrar una reserva con éxito cuando el usuario y el material son válidos")
    void testRegistrarReservaExitoso() {
        Usuario usuario = new Estudiante("U001", "Juan Pérez");
        Libro material = new Libro("M001", "Libro Java", "Autor Prueba", 2023);

        bibliotecaService.getUsuarios().add(usuario);
        bibliotecaService.getMateriales().add(material);

        assertDoesNotThrow(() -> {
            bibliotecaService.registrarReserva("U001", "M001");
        }, "La reserva debería registrarse correctamente sin lanzar excepciones");
    }

    @Test
    @DisplayName("Debe retornar solo los materiales que se encuentran disponibles")
    void testObtenerMaterialesDisponibles() {
        Libro materialDisponible = new Libro("M001", "Libro A", "Autor A", 2021);
        materialDisponible.setDisponible(true);

        Libro materialNoDisponible = new Libro("M002", "Libro B", "Autor B", 2022);
        materialNoDisponible.setDisponible(false);

        bibliotecaService.getMateriales().add(materialDisponible);
        bibliotecaService.getMateriales().add(materialNoDisponible);

        var disponibles = bibliotecaService.obtenerMaterialesDisponibles();

        assertEquals(1, disponibles.size(), "Debería haber exactamente 1 material disponible");
        assertEquals("M001", disponibles.get(0).getId(), "El material disponible debe ser M001");
    }

    @Test
    @DisplayName("Debe contar correctamente los préstamos activos de un usuario")
    void testContarPrestamosActivosPorUsuario() throws ReglaPrestamoVioladaException, MaterialNoEncontradoException, UsuarioNoEncontradoException {
        Usuario usuario = new Estudiante("U001", "Ana Gómez");
        Libro material = new Libro("M001", "Libro Java", "Autor Prueba", 2023);
        material.setDisponible(true);

        bibliotecaService.getUsuarios().add(usuario);
        bibliotecaService.getMateriales().add(material);

        bibliotecaService.prestarMaterial("U001", "M001");

        long prestamosActivos = bibliotecaService.contarPrestamosActivosPorUsuario("U001");

        assertEquals(1, prestamosActivos, "El usuario debería tener exactamente 1 préstamo activo");
    }

    @Test
    @DisplayName("Debe retornar los préstamos que se encuentran vencidos")
    void testObtenerPrestamosVencidos() {
        Usuario usuario = new Estudiante("U001", "Ana Gómez");
        Libro material = new Libro("M001", "Libro Java", "Autor Prueba", 2023);

        LocalDate fechaInicio = LocalDate.now().minusDays(20);
        LocalDate fechaLimite = LocalDate.now().minusDays(5); // Fecha límite en el pasado
        Prestamo prestamoVencido = new Prestamo(usuario, material, fechaInicio, fechaLimite);

        bibliotecaService.getUsuarios().add(usuario);
        bibliotecaService.getMateriales().add(material);
        bibliotecaService.getPrestamos().add(prestamoVencido);

        List<Prestamo> vencidos = bibliotecaService.obtenerPrestamosVencidos();

        assertEquals(1, vencidos.size(), "Debería haber exactamente 1 préstamo vencido");
        assertEquals("M001", vencidos.get(0).getMaterial().getId(), "El material del préstamo vencido debe coincidir");
    }

    @Test
    @DisplayName("Debe retornar los materiales ordenados de mayor a menor según sus solicitudes")
    void testObtenerMaterialesMasSolicitados() {
        Usuario usuario = new Estudiante("U001", "Ana Gómez");
        Libro material1 = new Libro("M001", "Libro A", "Autor A", 2021);
        Libro material2 = new Libro("M002", "Libro B", "Autor B", 2022);

        Prestamo prestamo1 = new Prestamo(usuario, material1, LocalDate.now().minusDays(10), LocalDate.now().plusDays(4));
        Prestamo prestamo2 = new Prestamo(usuario, material1, LocalDate.now().minusDays(5), LocalDate.now().plusDays(9));
        Prestamo prestamo3 = new Prestamo(usuario, material2, LocalDate.now().minusDays(2), LocalDate.now().plusDays(12));

        bibliotecaService.getUsuarios().add(usuario);
        bibliotecaService.getMateriales().add(material1);
        bibliotecaService.getMateriales().add(material2);
        bibliotecaService.getPrestamos().add(prestamo1);
        bibliotecaService.getPrestamos().add(prestamo2);
        bibliotecaService.getPrestamos().add(prestamo3);

        List<Material> masSolicitados = bibliotecaService.obtenerMaterialesMasSolicitados();

        assertEquals(2, masSolicitados.size(), "Debería haber 2 materiales en la lista");
        assertEquals("M001", masSolicitados.get(0).getId(), "El material más solicitado debe ser M001 con 2 préstamos");
    }

    @Test
    @DisplayName("Debe generar el reporte financiero agrupado por tipo de material")
    void testObtenerReporteFinancieroPorTipoMaterial() {
        Usuario usuario = new Estudiante("U001", "Ana Gómez");
        Libro material = new Libro("M001", "Libro A", "Autor A", 2021);

        Prestamo prestamo = new Prestamo(usuario, material, LocalDate.now().minusDays(10), LocalDate.now().plusDays(4));

        bibliotecaService.getUsuarios().add(usuario);
        bibliotecaService.getMateriales().add(material);
        bibliotecaService.getPrestamos().add(prestamo);

        Map<String, Double> reporte = bibliotecaService.obtenerReporteFinancieroPorTipoMaterial();

        assertNotNull(reporte, "El reporte financiero no debe ser nulo");
    }
}
