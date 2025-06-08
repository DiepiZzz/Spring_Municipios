package com.example.demo.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Repository.PasswordResetTokenRepository; 
import com.example.demo.Repository.UsuarioRepository;
import com.example.demo.model.PasswordResetToken;
import com.example.demo.model.Usuario;

@Service
public class PasswordResetService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    
    @Transactional
    public String createPasswordResetTokenForUser(String userEmail) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(userEmail);
        if (optionalUsuario.isEmpty()) {
            
            
            return null;
        }

        Usuario usuario = optionalUsuario.get();
        
        tokenRepository.deleteByUsuario(usuario);

        String token = UUID.randomUUID().toString(); 
        
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(24);

        PasswordResetToken resetToken = new PasswordResetToken(token, usuario, expiryDate);
        tokenRepository.save(resetToken);

        
        String recipientAddress = usuario.getEmail();
        String subject = "Recuperación de Contraseña";
        String confirmationUrl = "http://localhost:8080/reset-password?token=" + token; 
        String message = "Para restablecer tu contraseña, haz clic en el siguiente enlace:\n" + confirmationUrl;

        emailService.sendEmail(recipientAddress, subject, message);

        return "Se ha enviado un enlace de recuperación a tu email.";
    }

    
    public Optional<Usuario> validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> resetToken = tokenRepository.findByToken(token);

        if (resetToken.isEmpty() || resetToken.get().isExpired()) {
            return Optional.empty(); 
        }

        return Optional.of(resetToken.get().getUsuario());
    }

    
    @Transactional
    public void resetPassword(Usuario usuario, String newPassword) {
        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
       
        tokenRepository.deleteByUsuario(usuario);
    }
}