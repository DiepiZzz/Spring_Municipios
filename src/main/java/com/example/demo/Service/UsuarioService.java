package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.UsuarioRepository;
import com.example.demo.model.Usuario;

@Service // Marca esta clase como un componente de servicio
public class UsuarioService {

    @Autowired // Inyecta una instancia de UsuarioRepository
    private UsuarioRepository usuarioRepository;

    public Usuario saveUsuario(Usuario usuario) {
        // Aquí podrías agregar lógica de negocio, como encriptar la contraseña
        // Por ahora, solo guardamos el usuario directamente
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> getUsuarioByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Usuario updateUsuario(Long id, Usuario usuarioDetails) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario existingUsuario = optionalUsuario.get();
            existingUsuario.setUsername(usuarioDetails.getUsername());
            existingUsuario.setPassword(usuarioDetails.getPassword()); // En una app real, maneja esto con cuidado (e.g., Spring Security)
            existingUsuario.setNombre(usuarioDetails.getNombre());
            existingUsuario.setEmail(usuarioDetails.getEmail());
            return usuarioRepository.save(existingUsuario);
        } else {
            // Manejar caso donde el usuario no se encuentra (lanzar excepción, retornar null, etc.)
            return null; // O lanzar una excepción personalizada
        }
    }

    public boolean deleteUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean existsByUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}