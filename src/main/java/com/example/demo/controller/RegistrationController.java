package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.Repository.UsuarioRepository;
import com.example.demo.model.Usuario;

@Controller
public class RegistrationController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute Usuario usuario, Model model) {
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            model.addAttribute("error", "El nombre de usuario ya existe. Por favor, elige otro.");
            return "register";
        }
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            model.addAttribute("error", "El email ya está registrado. Por favor, usa otro.");
            return "register";
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRole("ROLE_USER");

        usuarioRepository.save(usuario);

        model.addAttribute("success", "¡Registro exitoso! Por favor, inicia sesión.");
        return "redirect:/login?registered";
    }
}