package com.example.demo.Service;

import java.util.List; 
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.UsuarioRepository;
import com.example.demo.model.Usuario;

@Service 
public class UsuarioService implements UserDetailsService { 

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class); 

    @Autowired 
    private UsuarioRepository usuarioRepository;

    
    
    

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Intentando cargar usuario desde UsuarioService (UserDetailsService): {}", username);

        
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Usuario '{}' no encontrado en la base de datos para autenticación.", username);
                    return new UsernameNotFoundException("Usuario no encontrado: " + username);
                });

        logger.info("Usuario '{}' encontrado para autenticación. Rol: {}", usuario.getUsername(), usuario.getRole());
        
        return usuario;
    }

    
    
    

    public Usuario saveUsuario(Usuario usuario) {
        
        
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
            
            
            
            existingUsuario.setPassword(usuarioDetails.getPassword()); 
            existingUsuario.setNombre(usuarioDetails.getNombre());
            existingUsuario.setEmail(usuarioDetails.getEmail());
            
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