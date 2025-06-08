package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Service.PasswordResetService;
import com.example.demo.model.Usuario;

@Controller
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        Optional<Usuario> usuario = passwordResetService.validatePasswordResetToken(token);

        if (usuario.isEmpty()) {
            model.addAttribute("error", "Token de recuperación inválido o expirado.");
            return "error_page"; 
        }

        model.addAttribute("token", token);
        return "reset_password"; 
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("password") String newPassword,
                                       @RequestParam("confirmPassword") String confirmPassword,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            model.addAttribute("token", token); 
            return "reset_password";
        }

        Optional<Usuario> optionalUsuario = passwordResetService.validatePasswordResetToken(token);

        if (optionalUsuario.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Token de recuperación inválido o expirado.");
            return "redirect:/login";
        }

        passwordResetService.resetPassword(optionalUsuario.get(), newPassword);
        redirectAttributes.addFlashAttribute("success", "Tu contraseña ha sido restablecida exitosamente. Por favor, inicia sesión.");
        return "redirect:/login";
    }
}