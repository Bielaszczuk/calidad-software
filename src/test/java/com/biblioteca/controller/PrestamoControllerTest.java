package com.biblioteca.controller;

import com.biblioteca.model.Prestamo;
import com.biblioteca.service.PrestamoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class PrestamoControllerTest {

    private PrestamoService prestamoService;
    private PrestamoController controller;

    @BeforeEach
    void setUp() {
        prestamoService = mock(PrestamoService.class);
        controller = new PrestamoController(prestamoService);
    }

    @Test
    void prestarDelegaEnElServicio() {
        Prestamo prestamo = new Prestamo();
        when(prestamoService.prestarLibro(1L, 2L)).thenReturn(prestamo);

        assertSame(prestamo, controller.prestar(1L, 2L));
    }

    @Test
    void devolverDelegaEnElServicio() {
        controller.devolver(1L);

        verify(prestamoService).devolverLibro(1L);
    }

    @Test
    void listarDelegaEnElServicio() {
        List<Prestamo> prestamos = List.of(new Prestamo());
        when(prestamoService.listarPrestamos()).thenReturn(prestamos);

        assertSame(prestamos, controller.listar());
    }

    @Test
    void multaCalculaSobreElPrestamoEncontrado() {
        Prestamo prestamo = new Prestamo();
        prestamo.setId(1L);
        when(prestamoService.listarPrestamos()).thenReturn(List.of(prestamo));
        when(prestamoService.calcularMulta(prestamo)).thenReturn(300.0);

        assertEquals(300.0, controller.multa(1L));
    }

    @Test
    void multaUsaNullCuandoNoEncuentraElPrestamo() {
        when(prestamoService.listarPrestamos()).thenReturn(List.of());
        when(prestamoService.calcularMulta(null)).thenReturn(0.0);

        assertEquals(0.0, controller.multa(99L));
        verify(prestamoService).calcularMulta(null);
    }
}
