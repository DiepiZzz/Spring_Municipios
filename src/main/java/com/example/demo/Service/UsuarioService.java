package com.example.demo.Service;

import java.util.List; // Asegúrate de la ruta correcta
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // Importa UserDetails
import org.springframework.beans.factory.annotation.Autowired; // Importa UserDetailsService
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.UsuarioRepository;
import com.example.demo.model.Usuario;

@Service // Marca esta clase como un componente de servicio
public class UsuarioService implements UserDetailsService { // ¡AHORA IMPLEMENTA UserDetailsService!

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class); // Logger para esta clase

    @Autowired // Inyecta una instancia de UsuarioRepository
    private UsuarioRepository usuarioRepository;

    // ////////////////////////////////////////////////////
    // Métodos de UserDetailsService
    // ////////////////////////////////////////////////////

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Intentando cargar usuario desde UsuarioService (UserDetailsService): {}", username);

        // Busca el usuario por su nombre de usuario
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Usuario '{}' no encontrado en la base de datos para autenticación.", username);
                    return new UsernameNotFoundException("Usuario no encontrado: " + username);
                });

        logger.info("Usuario '{}' encontrado para autenticación. Rol: {}", usuario.getUsername(), usuario.getRole());
        // Como tu entidad Usuario ya implementa UserDetails, puedes retornarla directamente.
        return usuario;
    }

    // ////////////////////////////////////////////////////
    // Tus métodos de servicio existentes (lógica de negocio)
    // ////////////////////////////////////////////////////

    public Usuario saveUsuario(Usuario usuario) {
        // Aquí se pueden agregar validaciones o lógica de negocio específica para el guardado.
        // La codificación de contraseña se hace en RegistrationController.
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> getUsuarioByUsername(String username) {
        // Este método ya lo tenías, ahora coexistirá con loadUserByUsername
        return usuarioRepository.findByUsername(username);
    }

    public Usuario updateUsuario(Long id, Usuario usuarioDetails) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario existingUsuario = optionalUsuario.get();
            existingUsuario.setUsername(usuarioDetails.getUsername());
            // ¡CUIDADO AQUÍ! Si actualizas la contraseña, DEBES codificarla.
            // Para un update de contraseña, probablemente tendrías un método dedicado.
            // existingUsuario.setPassword(passwordEncoder.encode(usuarioDetails.getPassword()));
            existingUsuario.setPassword(usuarioDetails.getPassword()); // Por ahora, mantén así si no la codificas en el update
            existingUsuario.setNombre(usuarioDetails.getNombre());
            existingUsuario.setEmail(usuarioDetails.getEmail());
            // Asegúrate de que el rol también se actualice si es necesario
            existingUsuario.setRole(usuarioDetails.getRole());
            return usuarioRepository.save(existingUsuario);
        } else {
            return null;
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