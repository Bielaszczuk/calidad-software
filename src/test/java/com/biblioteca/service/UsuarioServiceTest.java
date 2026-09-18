package com.biblioteca.service;

import com.biblioteca.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class UsuarioServiceTest {

    private final UsuarioService service = new UsuarioService(mock(UsuarioRepository.class));

    @Test
    void rechazaNombreNulo() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> service.crearUsuario(null, "ana@email.com"));

        assertEquals("El nombre es obligatorio", error.getMessage());
    }

    @Test
    void rechazaEmailNulo() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> service.crearUsuario("Ana", null));

        assertEquals("El email es obligatorio", error.getMessage());
    }
}
