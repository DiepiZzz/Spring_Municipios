package com.example.demo.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Repository.PasswordResetTokenRepository; // Para manejar transacciones
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
    private PasswordEncoder passwordEncoder; // Necesitarás el PasswordEncoder para el nuevo password

    // Método para iniciar el proceso de recuperación
    @Transactional
    public String createPasswordResetTokenForUser(String userEmail) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(userEmail);
        if (optionalUsuario.isEmpty()) {
            // Usuario no encontrado, no deberíamos lanzar una excepción directa por seguridad
            // Pero sí devolver algo que indique que no se pudo iniciar el proceso
            return null;
        }

        Usuario usuario = optionalUsuario.get();
        // Eliminar tokens antiguos para este usuario
        tokenRepository.deleteByUsuario(usuario);

        String token = UUID.randomUUID().toString(); // Genera un token único
        // Token válido por 24 horas (puedes ajustar el tiempo)
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(24);

        PasswordResetToken resetToken = new PasswordResetToken(token, usuario, expiryDate);
        tokenRepository.save(resetToken);

        // Prepara el correo electrónico
        String recipientAddress = usuario.getEmail();
        String subject = "Recuperación de Contraseña";
        String confirmationUrl = "http://localhost:8080/reset-password?token=" + token; // URL de tu frontend
        String message = "Para restablecer tu contraseña, haz clic en el siguiente enlace:\n" + confirmationUrl;

        emailService.sendEmail(recipientAddress, subject, message);

        return "Se ha enviado un enlace de recuperación a tu email.";
    }

    // Método para validar el token y permitir el cambio de contraseña
    public Optional<Usuario> validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> resetToken = tokenRepository.findByToken(token);

        if (resetToken.isEmpty() || resetToken.get().isExpired()) {
            return Optional.empty(); // Token no encontrado o expirado
        }

        return Optional.of(resetToken.get().getUsuario());
    }

    // Método para guardar la nueva contraseña
    @Transactional
    public void resetPassword(Usuario usuario, String newPassword) {
        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
        // Opcional: Eliminar el token una vez usada la contraseña
        tokenRepository.deleteByUsuario(usuario);
    }
}