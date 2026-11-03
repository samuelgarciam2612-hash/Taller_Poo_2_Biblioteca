package com.biblioteca.service;

import com.biblioteca.model.Usuario;
import com.biblioteca.model.Material;
import com.biblioteca.model.Prestamo;
import com.biblioteca.pattern.ValidadorPrestamo;
import com.biblioteca.exception.BibliotecaException;
import com.biblioteca.exception.UsuarioNoEncontradoException;
import com.biblioteca.exception.MaterialNoEncontradoException;
import com.biblioteca.exception.PrestamoNoEncontradoException;
import com.biblioteca.exception.ReglaPrestamoVioladaException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class BibliotecaService {

    private static final Logger LOGGER = Logger.getLogger(BibliotecaService.class.getName());

    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Material> materiales = new ArrayList<>();
    private final List<Prestamo> prestamos = new ArrayList<>();

    public Optional<Usuario> buscarUsuarioPorId(String id) {
        if (id == null || id.isBlank()) {
            LOGGER.log(Level.WARNING, "Búsqueda de usuario con ID nulo o vacío.");
            return Optional.empty();
        }
        return usuarios.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    public Optional<Material> buscarMaterialPorId(String id) {
        if (id == null || id.isBlank()) {
            LOGGER.log(Level.WARNING, "Búsqueda de material con ID nulo o vacío.");
            return Optional.empty();
        }
        return materiales.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    public Prestamo prestarMaterial(String usuarioId, String materialId) throws BibliotecaException {
        LOGGER.log(Level.INFO, "Iniciando solicitud de préstamo: Usuario ID [{0}], Material ID [{1}]",
                new Object[]{usuarioId, materialId});

        Usuario usuario = buscarUsuarioPorId(usuarioId)
                .orElseThrow(() -> {
                    LOGGER.log(Level.WARNING, "Error en préstamo: Usuario no encontrado con ID [{0}]", usuarioId);
                    return new UsuarioNoEncontradoException("El usuario con ID " + usuarioId + " no existe.");
                });

        Material material = buscarMaterialPorId(materialId)
                .orElseThrow(() -> {
                    LOGGER.log(Level.WARNING, "Error en préstamo: Material no encontrado con ID [{0}]", materialId);
                    return new MaterialNoEncontradoException("El material con ID " + materialId + " no existe.");
                });

        ValidadorPrestamo reglaPrestamo = (u, m) -> m.isDisponible() && !u.tieneMultasPendientes();

        if (!reglaPrestamo.validar(usuario, material)) {
            LOGGER.log(Level.WARNING, "Préstamo denegado: Material [{0}] no disponible o Usuario [{1}] con multas pendientes.",
                    new Object[]{material.getTitulo(), usuario.getId()});
            throw new ReglaPrestamoVioladaException("No se puede realizar el préstamo: El material no está disponible o el usuario tiene multas.");
        }

        material.prestar();

        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaLimite = fechaInicio.plusDays(material.getDiasPrestamo());
        Prestamo nuevoPrestamo = new Prestamo(usuario, material, fechaInicio, fechaLimite);

        usuario.registrarPrestamo(nuevoPrestamo);
        prestamos.add(nuevoPrestamo);

        LOGGER.log(Level.INFO, "Préstamo exitoso registrado: Material [{0}] asignado a [{1}] hasta [{2}]",
                new Object[]{material.getTitulo(), usuario.getId(), fechaLimite});

        return nuevoPrestamo;
    }

    public void devolverMaterial(String materialId) throws BibliotecaException {
        LOGGER.log(Level.INFO, "Procesando devolución para el material con ID [{0}]", materialId);

        Material material = buscarMaterialPorId(materialId)
                .orElseThrow(() -> {
                    LOGGER.log(Level.WARNING, "Error en devolución: Material no encontrado con ID [{0}]", materialId);
                    return new MaterialNoEncontradoException("El material con ID " + materialId + " no existe.");
                });

        Prestamo prestamoActivo = prestamos.stream()
                .filter(p -> p.getMaterial().getId().equals(materialId) && p.estaActivo())
                .findFirst()
                .orElseThrow(() -> {
                    LOGGER.log(Level.WARNING, "Error en devolución: No existe préstamo activo para el material con ID [{0}]", materialId);
                    return new PrestamoNoEncontradoException("No se encontró un préstamo activo para el material con ID " + materialId);
                });

        material.devolver();
        LocalDate fechaDevolucion = LocalDate.now();
        prestamoActivo.setFechaDevolucionReal(fechaDevolucion);

        LOGGER.log(Level.INFO, "Material [{0}] devuelto exitosamente en fecha [{1}]",
                new Object[]{material.getTitulo(), fechaDevolucion});

        material.sacarSiguienteEnEspera().ifPresent(siguienteUsuario -> {
            LOGGER.log(Level.INFO, "Notificando al siguiente en cola de espera: Usuario [{0}] para el material [{1}]",
                    new Object[]{siguienteUsuario.getId(), material.getTitulo()});
        });
    }

    public List<Material> obtenerMaterialesDisponibles() {
        return materiales.stream()
                .filter(Material::isDisponible)
                .toList();
    }

    public long contarPrestamosActivosPorUsuario(String usuarioId) {
        return prestamos.stream()
                .filter(p -> p.getUsuario().getId().equals(usuarioId) && p.estaActivo())
                .count();
    }

    public List<Prestamo> obtenerPrestamosVencidos() {
        LocalDate hoy = LocalDate.now();
        List<Prestamo> vencidos = prestamos.stream()
                .filter(p -> p.estaVencido(hoy))
                .toList();

        LOGGER.log(Level.INFO, "Consulta de préstamos vencidos realizada a fecha [{0}]. Total encontrados: {1}",
                new Object[]{hoy, vencidos.size()});

        return vencidos;
    }

    public void cobrarMulta(String usuarioId, double monto) throws BibliotecaException {
        LOGGER.log(Level.INFO, "Procesando cobro de multa: Usuario ID [{0}], Monto: ${1}",
                new Object[]{usuarioId, monto});

        Usuario usuario = buscarUsuarioPorId(usuarioId)
                .orElseThrow(() -> {
                    LOGGER.log(Level.WARNING, "Error al cobrar multa: Usuario no encontrado con ID [{0}]", usuarioId);
                    return new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + usuarioId);
                });

        usuario.pagarMulta(monto);
        LOGGER.log(Level.INFO, "Multa cobrada exitosamente al usuario [{0}]", usuarioId);
    }

    public void registrarReserva(String usuarioId, String materialId) throws BibliotecaException {
        LOGGER.log(Level.INFO, "Registrando reserva: Usuario ID [{0}] en lista de espera de Material ID [{1}]",
                new Object[]{usuarioId, materialId});

        Material material = buscarMaterialPorId(materialId)
                .orElseThrow(() -> {
                    LOGGER.log(Level.WARNING, "Error en reserva: Material no encontrado con ID [{0}]", materialId);
                    return new MaterialNoEncontradoException("Material no encontrado con ID: " + materialId);
                });

        Usuario usuario = buscarUsuarioPorId(usuarioId)
                .orElseThrow(() -> {
                    LOGGER.log(Level.WARNING, "Error en reserva: Usuario no encontrado con ID [{0}]", usuarioId);
                    return new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + usuarioId);
                });

        material.agregarALaFilaDeEspera(usuario);
        LOGGER.log(Level.INFO, "Reserva completada: Usuario [{0}] añadido a la cola de [{1}]",
                new Object[]{usuario.getId(), material.getTitulo()});
    }

    public List<Material> obtenerMaterialesMasSolicitados() {
        return prestamos.stream()
                .map(Prestamo::getMaterial)
                .collect(Collectors.groupingBy(m -> m, Collectors.counting()))
                .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .map(Map.Entry::getKey)
                .toList();
    }

    public Map<String, Double> obtenerReporteFinancieroPorTipoMaterial() {
        LOGGER.log(Level.INFO, "Generando reporte financiero consolidado por tipo de material.");
        return prestamos.stream()
                .collect(Collectors.groupingBy(p -> p.getMaterial().getTipo(),
                        Collectors.summingDouble(Prestamo::getMultaGenerada)));
    }

    public void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
        LOGGER.log(Level.INFO, "Usuario registrado en el sistema: ID [{0}]", usuario.getId());
    }

    public void registrarMaterial(Material material) {
        materiales.add(material);
        LOGGER.log(Level.INFO, "Material registrado en el sistema: ID [{0}], Título: [{1}]",
                new Object[]{material.getId(), material.getTitulo()});
    }
}