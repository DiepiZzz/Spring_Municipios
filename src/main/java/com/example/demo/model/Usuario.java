package com.example.demo.model;

import jakarta.persistence.Column; // Usa jakarta.persistence en Spring Boot 3+
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Genera getters, setters, toString, equals y hashCode con Lombok
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
@Entity // Marca esta clase como una entidad JPA
@Table(name = "usuarios") // Mapea esta entidad a la tabla 'usuarios'
public class Usuario {

    @Id // Marca esta propiedad como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática de ID (para PostgreSQL)
    private Long id;

    @Column(unique = true, nullable = false, length = 50) // Columna única, no nula, longitud máxima 50
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(unique = true, nullable = false, length = 100)
    private String email;
}
