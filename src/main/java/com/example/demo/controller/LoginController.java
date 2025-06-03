package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired; // Importa el nuevo servicio
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Service.PasswordResetService;

@Controller
public class LoginController {

    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot_password"; // Necesitarás crear este HTML
    }

    @PostMapping("/forgot-password")
    public String processForgotPasswordForm(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        String result = passwordResetService.createPasswordResetTokenForUser(email);

        if (result != null) {
            redirectAttributes.addFlashAttribute("success", result);
        } else {
            // Por seguridad, se recomienda no decir si el email existe o no.
            // Siempre muestra un mensaje genérico.
            redirectAttributes.addFlashAttribute("error", "Si el email está registrado, se enviará un enlace de recuperación.");
        }
        return "redirect:/forgot-password";
    }
}