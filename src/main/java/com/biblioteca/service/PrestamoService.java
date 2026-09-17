package com.biblioteca.service;

import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PrestamoService {

    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;

    public PrestamoService(
            UsuarioRepository usuarioRepository,
            LibroRepository libroRepository,
            PrestamoRepository prestamoRepository) {

        this.usuarioRepository = usuarioRepository;
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
    }

    public Prestamo prestarLibro(Long usuarioId, Long libroId) {

        Usuario usuario =
                usuarioRepository.findById(usuarioId).orElse(null);

        Libro libro =
                libroRepository.findById(libroId).orElse(null);

        if (usuario == null) {
            throw new IllegalArgumentException("Usuario inexistente");
        }

        if (libro == null) {
            throw new IllegalArgumentException("Libro inexistente");
        }

        if (!usuario.isActivo()) {
            throw new IllegalStateException("El usuario está inactivo");
        }

        if (usuario.isMoroso()) {
            throw new IllegalStateException("El usuario tiene una deuda");
        }

        if (libro.isPrestado()) {
            throw new IllegalStateException("El libro ya está prestado");
        }

        if (usuario.getPrestamos().size() >= 3) {
            throw new IllegalStateException(
                    "El usuario alcanzó el máximo de préstamos");
        }

        Prestamo prestamo = new Prestamo(usuario, libro);

        libro.setPrestado(true);

        usuario.getPrestamos().add(prestamo);

        libroRepository.save(libro);
        usuarioRepository.save(usuario);

        return prestamoRepository.save(prestamo);
    }

    public double calcularRecargoA(Prestamo prestamo) {
        return calcularRecargo(prestamo);
    }

    public double calcularRecargoB(Prestamo prestamo) {
        return calcularRecargo(prestamo);
    }

    private double calcularRecargo(Prestamo prestamo) {
        if (prestamo == null) {
            return 0;
        }

        long dias = ChronoUnit.DAYS.between(
                prestamo.getFechaPrestamo(),
                LocalDate.now(ZoneId.systemDefault()));

        if (dias <= 7) {
            return 0;
        }
        if (dias <= 14) {
            return (dias - 7) * 100.0;
        }
        if (dias <= 21) {
            return (dias - 7) * 150.0;
        }
        if (dias <= 30) {
            return (dias - 7) * 200.0;
        }
        if (dias <= 45) {
            return (dias - 7) * 300.0;
        }
        if (dias <= 60) {
            return (dias - 7) * 400.0;
        }
        if (dias <= 90) {
            return (dias - 7) * 500.0;
        }
        return (dias - 7) * 750.0;
    }

    public void devolverLibro(Long prestamoId) {

        Prestamo prestamo =
                prestamoRepository.findById(prestamoId).orElse(null);

        if (prestamo == null) {
            throw new IllegalArgumentException("Préstamo inexistente");
        }

        if (prestamo.isDevuelto()) {
            throw new IllegalStateException("El préstamo ya fue devuelto");
        }

        prestamo.devolver();

        Libro libro = prestamo.getLibro();

        libro.setPrestado(false);

        libroRepository.save(libro);
        prestamoRepository.save(prestamo);
    }

    public void devolverLibroAntiguo(Long prestamoId) {
        devolverLibro(prestamoId);
    }

    public double calcularMulta(Prestamo prestamo) {

        if (prestamo == null) {
            return 0;
        }

        LocalDate fechaFinal = prestamo.isDevuelto()
                ? prestamo.getFechaDevolucion()
                : LocalDate.now(ZoneId.systemDefault());
        long dias = ChronoUnit.DAYS.between(prestamo.getFechaPrestamo(), fechaFinal);

        return calcularMultaPorDias(dias);
    }

    private double calcularMultaPorDias(long dias) {
        if (dias <= 7) {
            return 0;
        }
        if (dias <= 14) {
            return (dias - 7) * 100.0;
        }
        if (dias <= 30) {
            return (dias - 7) * 200.0;
        }
        return (dias - 7) * 500.0;
    }

    public List<Prestamo> listarPrestamos() {
        return prestamoRepository.findAll();
    }
}
