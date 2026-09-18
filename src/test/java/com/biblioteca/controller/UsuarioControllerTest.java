package com.biblioteca.controller;

import com.biblioteca.model.Usuario;
import com.biblioteca.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    private UsuarioService usuarioService;
    private UsuarioController controller;

    @BeforeEach
    void setUp() {
        usuarioService = mock(UsuarioService.class);
        controller = new UsuarioController(usuarioService);
    }

    @Test
    void crearDelegaEnElServicio() {
        Usuario usuario = new Usuario("Ana", "ana@email.com");
        when(usuarioService.crearUsuario("Ana", "ana@email.com")).thenReturn(usuario);

        assertSame(usuario, controller.crear("Ana", "ana@email.com"));
    }

    @Test
    void listarDelegaEnElServicio() {
        List<Usuario> usuarios = List.of(new Usuario());
        when(usuarioService.listarUsuarios()).thenReturn(usuarios);

        assertSame(usuarios, controller.listar());
    }

    @Test
    void buscarDelegaEnElServicio() {
        Usuario usuario = new Usuario();
        when(usuarioService.buscarUsuario(1L)).thenReturn(usuario);

        assertSame(usuario, controller.buscar(1L));
    }

    @Test
    void eliminarDelegaEnElServicio() {
        controller.eliminar(1L);

        verify(usuarioService).eliminarUsuario(1L);
    }
}
