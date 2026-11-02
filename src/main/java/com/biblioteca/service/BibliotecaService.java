package com.biblioteca.service;

import com.biblioteca.model.Usuario;
import com.biblioteca.model.Material;
import com.biblioteca.model.Prestamo;
import com.biblioteca.pattern.ValidadorPrestamo;
import com.biblioteca.exception.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class BibliotecaService {

    private List<Usuario> usuarios = new ArrayList<>();
    private List<Material> materiales = new ArrayList<>();
    private List<Prestamo> prestamos = new ArrayList<>();

    public Optional<Usuario> buscarUsuarioPorId(String id) {
        return usuarios.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    public Optional<Material> buscarMaterialPorId(String id) {
        return materiales.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    public Prestamo prestarMaterial(String usuarioId, String materialId) {

        Usuario usuario = buscarUsuarioPorId(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("El usuario con ID " + usuarioId + " no existe."));

        Material material = buscarMaterialPorId(materialId)
                .orElseThrow(() -> new MaterialNoEncontradoException("El material con ID " + materialId + " no existe."));

        ValidadorPrestamo reglaPrestamo = (u, m) -> m.isDisponible() && !u.tieneMultasPendientes();

        if (!reglaPrestamo.validar(usuario, material)) {
            throw new ReglaPrestamoVioladaException("No se puede realizar el préstamo: El material no está disponible o el usuario tiene multas.");
        }

        material.setDisponible(false);

        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaLimite = fechaInicio.plusDays(14);
        Prestamo nuevoPrestamo = new Prestamo(usuario, material, fechaInicio, fechaLimite);

        prestamos.add(nuevoPrestamo);

        return nuevoPrestamo;
    }

    public void devolverMaterial(String materialId) {

        Material material = buscarMaterialPorId(materialId)
                .orElseThrow(() -> new MaterialNoEncontradoException("El material con ID " + materialId + " no existe."));

        Prestamo prestamoActivo = prestamos.stream()
                .filter(p -> p.getMaterial().getId().equals(materialId) && p.getFechaDevolucionReal() == null) // Asumiendo que si no hay fecha real, está activo
                .findFirst()
                .orElseThrow(() -> new PrestamoNoEncontradoException("No se encontró un préstamo activo para el material con ID " + materialId));

        material.setDisponible(true);

        prestamoActivo.setFechaDevolucionReal(LocalDate.now());

    }

    public List<Material> obtenerMaterialesDisponibles() {
        return materiales.stream()
                .filter(m -> m.isDisponible())
                .toList();
    }

    public long contarPrestamosActivosPorUsuario(String usuarioId) {
        return prestamos.stream()
                .filter(p -> p.getUsuario().getId().equals(usuarioId) && p.getFechaDevolucionReal() == null)
                .count();
    }

    public List<Prestamo> obtenerPrestamosVencidos() {
        return prestamos.stream()
                .filter(p -> p.getFechaDevolucionReal() == null && p.getFechaLimiteDevolucion().isBefore(LocalDate.now()))
                .toList();
    }

    public void cobrarMulta(String usuarioId, double monto) {
        Usuario usuario = buscarUsuarioPorId(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + usuarioId));

        usuario.pagarMulta(monto);
    }

    public void registrarReserva(String usuarioId, String materialId) {
        Material material = buscarMaterialPorId(materialId)
                .orElseThrow(() -> new MaterialNoEncontradoException("Material no encontrado con ID: " + materialId));

        Usuario usuario = buscarUsuarioPorId(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + usuarioId));

        material.agregarALaFilaDeEspera(usuario);
    }


    public List<Material> obtenerMaterialesMasSolicitados() {
        return prestamos.stream()
                .map(p -> p.getMaterial())
                .collect(Collectors.groupingBy(m -> m, Collectors.counting()))
                .entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue())) // Orden descendente (más solicitado primero)
                .map(entry -> entry.getKey())
                .toList();
    }

    public Map<String, Double> obtenerReporteFinancieroPorTipoMaterial() {
        return prestamos.stream()
                .collect(Collectors.groupingBy(p -> p.getMaterial().getTipo(),
                        Collectors.summingDouble(p -> p.getMultaGenerada())));
    }

}

