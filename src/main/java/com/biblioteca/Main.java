package com.biblioteca;

import com.biblioteca.exception.BibliotecaException;
import com.biblioteca.model.*;
import com.biblioteca.pattern.*;
import com.biblioteca.service.BibliotecaService;
import com.biblioteca.util.BibliotecaUtils;
import com.biblioteca.util.ComparatorLC;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        BibliotecaService service = new BibliotecaService();
        //Dominio y factory method
        Usuario estudiante1 = new Estudiante("U001", "Samuel García");
        Usuario docente = new Docente("U002", "Camila Hernández");
        Usuario estudiante2 = new Estudiante("U003", "David Ceballos");

        service.getUsuarios().add(estudiante1);
        service.getUsuarios().add(docente);
        service.getUsuarios().add(estudiante2);

        MaterialFactory libroFactory = new LibroFactory();
        MaterialFactory revistaFactory = new RevistaFactory();

        Material libroJava = libroFactory.crearMaterial("M001", "Java a Fondo", "Pablo Sznajdleder", 2021);
        Material libroPatrones = libroFactory.crearMaterial("M002", "Design Patterns", "Gang of Four", 1994);
        Material revistaTech = revistaFactory.crearMaterial("M003", "IEEE Software", "IEEE", 2023);

        service.getMateriales().add(libroJava);
        service.getMateriales().add(libroPatrones);
        service.getMateriales().add(revistaTech);

        LOGGER.log(Level.INFO, "Materiales cargados: {0}", service.getMateriales().size());


        // 2. ordenamienot y genericos

        List<Material> listaParaOrdenar = new ArrayList<>(service.getMateriales());

        // Ordenamiento genérico manual (Burbuja / Inserción implementado en BibliotecaUtils)
        BibliotecaUtils.ordenar(listaParaOrdenar);
        LOGGER.info("Materiales ordenados por orden natural (Comparable):");
        listaParaOrdenar.forEach(m -> LOGGER.log(Level.INFO, " - [{0}] {1} ({2})",
                new Object[]{m.getId(), m.getTitulo(), m.getAnioPublicacion()}));

        // Ordenamiento con Comparator compuesto (ComparatorLC)
        listaParaOrdenar.sort(ComparatorLC.porAnioYTituloDescendente());
        LOGGER.info("Materiales ordenados con Comparator:");
        listaParaOrdenar.forEach(m -> LOGGER.log(Level.INFO, " - [{0}] {1} ({2})",
                new Object[]{m.getId(), m.getTitulo(), m.getAnioPublicacion()}));


        try {
            // Préstamo regular exitoso
            LOGGER.info("Solicitando préstamo de 'Java a Fondo' para Samuel...");
            service.prestarMaterial("U001", "M001");

            // Intento de préstamo del mismo material ocupado (debe denegarse)
            LOGGER.info("Intentando prestar el mismo material a Camila (debe fallar)...");
            service.prestarMaterial("U002", "M001");
        } catch (BibliotecaException e) {
            LOGGER.log(Level.WARNING, "Excepción capturada con éxito: {0}", e.getMessage());
        }

        // Caso de error con usuario inexistente
        try {
            LOGGER.info("Probando validación con ID inexistente...");
            service.cobrarMulta("U_FANTASMA", 50.0);
        } catch (BibliotecaException e) {
            LOGGER.log(Level.WARNING, "Excepción esperada capturada: {0}", e.getMessage());
        }


        try {
            // Se registra reserva del libro ocupado
            service.registrarReserva("U002", "M001");
            LOGGER.info("Camila quedó en cola de espera. Ahora Samuel devuelve el libro...");

            // Samuel devuelve el libro, Notifica automáticamente al observer de Camila
            service.devolverMaterial("M001");
        } catch (BibliotecaException e) {
            LOGGER.log(Level.SEVERE, "Fallo inesperado en flujo de devolución: {0}", e.getMessage());
        }


        List<Material> disponibles = service.obtenerMaterialesDisponibles();
        LOGGER.log(Level.INFO, "Total de materiales disponibles actualmente: {0}", disponibles.size());

        long activosDavid = service.contarPrestamosActivosPorUsuario("U003");
        LOGGER.log(Level.INFO, "Préstamos activos de David Ceballos: {0}", activosDavid);

        var masSolicitados = service.obtenerMaterialesMasSolicitados();
        LOGGER.log(Level.INFO, "Materiales con mayor demanda en el sistema: {0}", masSolicitados.size());

    }
}