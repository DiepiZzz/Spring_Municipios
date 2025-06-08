package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class HolaMundoController {

    @GetMapping("/hola") 
    public String holaMundo() {
        return "¡Hola desde Spring Boot con VS Code!";
    }

    @GetMapping("/saludo")
    public String saludoPersonalizado() {
        return "¡Bienvenido a tu aplicación Spring Boot en VS Code!";
    }
}
