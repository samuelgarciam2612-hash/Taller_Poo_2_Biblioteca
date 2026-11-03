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
        // Resultado esperado: Se dispara UsuarioNoEncontradoException y se emite log WARNING de fallo al cobrar multa.
    }

    @Test
    @DisplayName("Debe lanzar MaterialNoEncontradoException al intentar prestar un material inexistente")
    void testBuscarMaterialInexistente() {
        Usuario usuario = new Estudiante("U001", "Juan Pérez");
        bibliotecaService.getUsuarios().add(usuario);

        assertThrows(
                MaterialNoEncontradoException.class,
                () -> bibliotecaService.prestarMaterial("U001", "M_INEXISTENTE"),
                "Debería lanzar MaterialNoEncontradoException si el material no existe"
        );
        // Resultado esperado: Tras validar al usuario existente, se interrumpe el flujo y se lanza MaterialNoEncontradoException con log WARNING.
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
        // Resultado esperado: La expresión lambda ValidadorPrestamo deniega la operación y dispara ReglaPrestamoVioladaException.
    }

    @Test
    @DisplayName("Debe lanzar PrestamoNoEncontradoException al intentar devolver un material sin préstamo activo")
    void testDevolucionSinPrestamoActivo() {
        Libro material = new Libro("M001", "Libro Java", "Autor Prueba", 2023);
        bibliotecaService.getMateriales().add(material);

        assertThrows(
                PrestamoNoEncontradoException.class,
                () -> bibliotecaService.devolverMaterial("M001"),
                "Debería lanzar PrestamoNoEncontradoException si no existe un préstamo activo para ese material"
        );
        // Resultado esperado: El material existe en el sistema pero, al no registrar préstamo activo en la lista, lanza PrestamoNoEncontradoException.
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
        // Resultado esperado: Préstamo creado exitosamente, material marcado como no disponible y registro añadido a las colecciones sin excepciones.
    }

    @Test
    @DisplayName("Debe registrar la devolución con éxito usando solo el ID del material")
    void testDevolverMaterialExitoso() throws BibliotecaException {
        Usuario usuario = new Estudiante("U001", "Juan Pérez");
        Libro material = new Libro("M001", "Libro Java", "Autor Prueba", 2023);
        material.setDisponible(true);

        bibliotecaService.getUsuarios().add(usuario);
        bibliotecaService.getMateriales().add(material);

        bibliotecaService.prestarMaterial("U001", "M001");

        assertDoesNotThrow(() -> {
            bibliotecaService.devolverMaterial("M001");
        }, "La devolución debería completarse con éxito");
        // Resultado esperado: El estado del material pasa a disponible, se asigna fecha real de devolución y se notifica la cola de espera si aplica.
    }

    @Test
    @DisplayName("Debe cobrar una multa con éxito a un usuario válido")
    void testCobrarMultaExitoso() {
        Usuario usuario = new Estudiante("U001", "Juan Pérez");
        bibliotecaService.getUsuarios().add(usuario);

        assertDoesNotThrow(() -> {
            bibliotecaService.cobrarMulta("U001", 20.0);
        }, "El cobro de la multa debería realizarse sin lanzar excepciones");
        // Resultado esperado: El monto se aplica correctamente sobre el saldo del usuario y se registra en la bitácora con Logger Level INFO.
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
        // Resultado esperado: El usuario ingresa a la ColaEspera interna del material y se emite la traza de auditoría correspondiente.
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
        // Resultado esperado: Filtrado mediante Java Streams retornando únicamente la lista de materiales con isDisponible() == true.
    }

    @Test
    @DisplayName("Debe contar correctamente los préstamos activos de un usuario")
    void testContarPrestamosActivosPorUsuario() throws BibliotecaException {
        Usuario usuario = new Estudiante("U001", "Ana Gómez");
        Libro material = new Libro("M001", "Libro Java", "Autor Prueba", 2023);
        material.setDisponible(true);

        bibliotecaService.getUsuarios().add(usuario);
        bibliotecaService.getMateriales().add(material);

        bibliotecaService.prestarMaterial("U001", "M001");

        long prestamosActivos = bibliotecaService.contarPrestamosActivosPorUsuario("U001");

        assertEquals(1, prestamosActivos, "El usuario debería tener exactamente 1 préstamo activo");
        // Resultado esperado: Conteo exacto de 1 préstamo activo asociado al ID "U001" procesado con Stream count().
    }

    @Test
    @DisplayName("Debe retornar los préstamos que se encuentran vencidos")
    void testObtenerPrestamosVencidos() {
        Usuario usuario = new Estudiante("U001", "Ana Gómez");
        Libro material = new Libro("M001", "Libro Java", "Autor Prueba", 2023);

        LocalDate fechaInicio = LocalDate.now().minusDays(20);
        LocalDate fechaLimite = LocalDate.now().minusDays(5);

        Prestamo prestamoVencido = new Prestamo(usuario, material, fechaInicio, fechaLimite);

        bibliotecaService.getUsuarios().add(usuario);
        bibliotecaService.getMateriales().add(material);
        bibliotecaService.getPrestamos().add(prestamoVencido);

        List<Prestamo> vencidos = bibliotecaService.obtenerPrestamosVencidos();

        assertEquals(1, vencidos.size(), "Debería haber exactamente 1 préstamo vencido");
        assertEquals("M001", vencidos.get(0).getMaterial().getId(), "El material del préstamo vencido debe coincidir");
        // Resultado esperado: Identificación de préstamos cuya fecha límite es anterior a LocalDate.now() y reporte de cantidad en logs.
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
        // Resultado esperado: Agrupamiento por frecuencia y ordenamiento descendente, ubicando "M001" en la primera posición.
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
        // Resultado esperado: Mapa consolidado donde las llaves corresponden al tipo de material y los valores a la sumatoria de multas generadas.
    }
}