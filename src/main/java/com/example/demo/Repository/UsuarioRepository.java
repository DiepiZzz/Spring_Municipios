package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Usuario;

@Repository // Marca esta interfaz como un componente de repositorio
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // JpaRepository ya proporciona métodos CRUD (save, findById, findAll, delete, etc.)

    // Puedes agregar métodos personalizados si necesitas consultas específicas:
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}