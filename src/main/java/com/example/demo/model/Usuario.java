package com.example.demo.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor; // Importa UserDetails
import lombok.Data;
import lombok.NoArgsConstructor; // O java.util.Set si manejas roles como Set

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails { // ¡Ahora implementa UserDetails!

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false) // Nuevo campo para el rol
    private String role; // Por ejemplo, "ROLE_USER", "ROLE_ADMIN"

    // --- Métodos de la interfaz UserDetails ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Asigna el rol al usuario. Si tuvieras múltiples roles, los gestionarías aquí.
        // Por ejemplo, si 'role' fuera una cadena como "USER,ADMIN", podrías dividirla.
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username; // Ya tienes un campo 'username', úsalo como nombre de usuario para Spring Security
    }

    // Estos métodos devuelven 'true' por defecto. Puedes añadir lógica personalizada
    // si quieres implementar cuentas expiradas, bloqueadas, etc.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}