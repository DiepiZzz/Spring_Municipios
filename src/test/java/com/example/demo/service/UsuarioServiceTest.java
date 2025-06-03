package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Repository.UsuarioRepository;
import com.example.demo.Service.UsuarioService;
import com.example.demo.model.Usuario;

/**
 * Clase de pruebas para el servicio de Usuario.
 * Utiliza @SpringBootTest para cargar el contexto completo de Spring Boot,
 * @Transactional para asegurar que cada test se ejecuta en una transacción aislada
 * y se revierte al finalizar, manteniendo la base de datos limpia.
 */
@SpringBootTest
@Transactional
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;
    /**
     * Se ejecuta antes de cada método de test.
     * Limpia la tabla de usuarios para asegurar un estado inicial limpio y predecible para cada test.
     */ // Usado para limpieza y verificación directa si es necesario

    @Test
    void testSaveUsuario() {
        // Dado
        Usuario usuario = new Usuario(null, "jdoe", "pass123", "John Doe", "john.doe@example.com");

        // Cuando
        Usuario savedUsuario = usuarioService.saveUsuario(usuario);

        // Entonces
        assertNotNull(savedUsuario.getId(), "El ID del usuario no debería ser nulo después de guardar");
        assertEquals("jdoe", savedUsuario.getUsername(), "El nombre de usuario guardado no coincide");
        assertTrue(usuarioRepository.findByUsername("jdoe").isPresent(), "El usuario debería encontrarse por username en el repositorio");
    }

    @Test
    void testGetAllUsuarios() {
        // Dado
        usuarioService.saveUsuario(new Usuario(null, "jdoe", "pass123", "John Doe", "john.doe@example.com"));
        usuarioService.saveUsuario(new Usuario(null, "asmith", "pass456", "Alice Smith", "alice.smith@example.com"));

        // Cuando
        List<Usuario> usuarios = usuarioService.getAllUsuarios();

        // Entonces
        assertEquals(2, usuarios.size(), "Debería haber dos usuarios en la base de datos");
    }

    @Test
    void testGetUsuarioById() {
        // Dado
        Usuario usuario = usuarioService.saveUsuario(new Usuario(null, "jdoe", "pass123", "John Doe", "john.doe@example.com"));

        // Cuando
        Optional<Usuario> foundUsuario = usuarioService.getUsuarioById(usuario.getId());

        // Entonces
        assertTrue(foundUsuario.isPresent(), "El usuario debería encontrarse por ID");
        assertEquals("John Doe", foundUsuario.get().getNombre(), "El nombre del usuario recuperado no coincide");
    }

    @Test
    void testGetUsuarioByIdNotFound() {
        // Cuando
        Optional<Usuario> foundUsuario = usuarioService.getUsuarioById(99L); // ID que no existe

        // Entonces
        assertFalse(foundUsuario.isPresent(), "No debería encontrarse un usuario con un ID inexistente");
    }

    @Test
    void testGetUsuarioByUsername() {
        // Dado
        usuarioService.saveUsuario(new Usuario(null, "jdoe", "pass123", "John Doe", "john.doe@example.com"));

        // Cuando
        Optional<Usuario> foundUsuario = usuarioService.getUsuarioByUsername("jdoe");

        // Entonces
        assertTrue(foundUsuario.isPresent(), "El usuario debería encontrarse por nombre de usuario");
        assertEquals("John Doe", foundUsuario.get().getNombre(), "El nombre del usuario recuperado no coincide");
    }

    @Test
    void testUpdateUsuario() {
        // Dado
        Usuario usuario = usuarioService.saveUsuario(new Usuario(null, "jdoe", "pass123", "John Doe", "john.doe@example.com"));
        Usuario updatedDetails = new Usuario(null, "johndoe_new", "newpass", "John Doe Updated", "john.doe.updated@example.com");

        // Cuando
        Usuario updatedUsuario = usuarioService.updateUsuario(usuario.getId(), updatedDetails);

        // Entonces
        assertNotNull(updatedUsuario, "El usuario actualizado no debería ser nulo");
        assertEquals("johndoe_new", updatedUsuario.getUsername(), "El nombre de usuario debería haberse actualizado");
        assertEquals("John Doe Updated", updatedUsuario.getNombre(), "El nombre completo debería haberse actualizado");
        assertEquals("john.doe.updated@example.com", updatedUsuario.getEmail(), "El email debería haberse actualizado");
    }

    @Test
    void testUpdateUsuarioNotFound() {
        // Dado
        Usuario updatedDetails = new Usuario(null, "johndoe_new", "newpass", "John Doe Updated", "john.doe.updated@example.com");

        // Cuando
        Usuario updatedUsuario = usuarioService.updateUsuario(99L, updatedDetails); // ID que no existe

        // Entonces
        assertNull(updatedUsuario, "Debería retornar null si el usuario a actualizar no se encuentra");
    }

    @Test
    void testDeleteUsuario() {
        // Dado
        Usuario usuario = usuarioService.saveUsuario(new Usuario(null, "jdoe", "pass123", "John Doe", "john.doe@example.com"));

        // Cuando
        boolean isDeleted = usuarioService.deleteUsuario(usuario.getId());

        // Entonces
        assertTrue(isDeleted, "El usuario debería haber sido eliminado exitosamente");
        assertFalse(usuarioRepository.findById(usuario.getId()).isPresent(), "El usuario no debería encontrarse después de la eliminación");
    }

    @Test
    void testDeleteUsuarioNotFound() {
        // Cuando
        boolean isDeleted = usuarioService.deleteUsuario(99L); // ID que no existe

        // Entonces
        assertFalse(isDeleted, "No debería reportar éxito al intentar eliminar un usuario inexistente");
    }

    @Test
    void testExistsByUsername() {
        // Dado
        usuarioService.saveUsuario(new Usuario(null, "testuser", "pass", "Test User", "test@example.com"));

        // Cuando & Entonces
        assertTrue(usuarioService.existsByUsername("testuser"), "Debería retornar true para un username existente");
        assertFalse(usuarioService.existsByUsername("nonexistent"), "Debería retornar false para un username inexistente");
    }

    @Test
    void testExistsByEmail() {
        // Dado
        usuarioService.saveUsuario(new Usuario(null, "testuser", "pass", "Test User", "test@example.com"));

        // Cuando & Entonces
        assertTrue(usuarioService.existsByEmail("test@example.com"), "Debería retornar true para un email existente");
        assertFalse(usuarioService.existsByEmail("nonexistent@example.com"), "Debería retornar false para un email inexistente");
    }
}