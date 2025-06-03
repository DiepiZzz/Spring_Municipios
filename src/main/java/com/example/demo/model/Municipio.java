package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "municipios")
public class Municipio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String departamento;

    @Column(nullable = false, length = 100)
    private String pais;

    @Column(length = 100)
    private String alcalde;

    @Column(length = 100)
    private String gobernador; // Asumiendo que el gobernador es del departamento/región

    @Column(length = 100)
    private String patronoReligioso;

    @Column
    private Integer numHabitantes;

    @Column
    private Integer numCasas;

    @Column
    private Integer numParques;

    @Column
    private Integer numColegios;

    @Column(columnDefinition = "TEXT") // Para descripciones más largas
    private String descripcion;
}