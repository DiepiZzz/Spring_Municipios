package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Municipio;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Long> {
    Optional<Municipio> findByNombre(String nombre);
    List<Municipio> findByDepartamento(String departamento);
    List<Municipio> findByPais(String pais);
    boolean existsByNombreAndDepartamentoAndPais(String nombre, String departamento, String pais);
}