package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Marca esta clase como un controlador REST
public class HolaMundoController {

    @GetMapping("/hola") // Mapea la ruta /hola a este método para peticiones GET
    public String holaMundo() {
        return "¡Hola desde Spring Boot con VS Code!";
    }

    @GetMapping("/saludo")
    public String saludoPersonalizado() {
        return "¡Bienvenido a tu aplicación Spring Boot en VS Code!";
    }
}
