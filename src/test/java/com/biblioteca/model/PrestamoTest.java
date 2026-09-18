package com.biblioteca.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class PrestamoTest {

    @Test
    void constructorInicializaUnPrestamoActivo() {
        Usuario usuario = new Usuario();
        Libro libro = new Libro();

        Prestamo prestamo = new Prestamo(usuario, libro);

        assertAll(
                () -> assertSame(usuario, prestamo.getUsuario()),
                () -> assertSame(libro, prestamo.getLibro()),
                () -> assertEquals(LocalDate.now(ZoneId.systemDefault()), prestamo.getFechaPrestamo()),
                () -> assertFalse(prestamo.isDevuelto())
        );
    }

    @Test
    void devolverMarcaElPrestamoYRegistraLaFecha() {
        Prestamo prestamo = new Prestamo();

        prestamo.devolver();

        assertTrue(prestamo.isDevuelto());
        assertEquals(LocalDate.now(ZoneId.systemDefault()), prestamo.getFechaDevolucion());
    }

    @Test
    void gettersYSettersConservanLosDatos() {
        Prestamo prestamo = new Prestamo();
        Usuario usuario = new Usuario();
        Libro libro = new Libro();
        LocalDate fechaPrestamo = LocalDate.of(2026, 9, 1);
        LocalDate fechaDevolucion = LocalDate.of(2026, 9, 10);
        prestamo.setId(1L);
        prestamo.setFechaPrestamo(fechaPrestamo);
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamo.setDevuelto(true);
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);

        assertAll(
                () -> assertEquals(1L, prestamo.getId()),
                () -> assertEquals(fechaPrestamo, prestamo.getFechaPrestamo()),
                () -> assertEquals(fechaDevolucion, prestamo.getFechaDevolucion()),
                () -> assertTrue(prestamo.isDevuelto()),
                () -> assertSame(usuario, prestamo.getUsuario()),
                () -> assertSame(libro, prestamo.getLibro())
        );
    }
}
