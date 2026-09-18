package com.biblioteca.service;

import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class PrestamoServiceTest {

    @Test
    void calcularMultaConPrestamoNuloDevuelveCero() {
        PrestamoService service = new PrestamoService(
                mock(UsuarioRepository.class),
                mock(LibroRepository.class),
                mock(PrestamoRepository.class));

        assertEquals(0.0, service.calcularMulta(null));
    }
}
