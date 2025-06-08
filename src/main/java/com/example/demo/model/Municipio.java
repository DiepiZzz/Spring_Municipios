package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El departamento no puede estar vacío")
    @Size(max = 100, message = "El departamento no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String departamento;

    @NotBlank(message = "El país no puede estar vacío")
    @Size(max = 100, message = "El país no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String pais;

    @Size(max = 100, message = "El alcalde no puede exceder 100 caracteres")
    @Column(length = 100)
    private String alcalde;

    @Size(max = 100, message = "El gobernador no puede exceder 100 caracteres")
    @Column(length = 100)
    private String gobernador;

    @Size(max = 100, message = "El patrono religioso no puede exceder 100 caracteres")
    @Column(length = 100)
    private String patronoReligioso;

    @NotNull(message = "El número de habitantes no puede estar vacío")
    @Min(value = 0, message = "El número de habitantes no puede ser negativo")
    @Column
    private Integer numHabitantes;

    @NotNull(message = "El número de casas no puede estar vacío")
    @Min(value = 0, message = "El número de casas no puede ser negativo")
    @Column
    private Integer numCasas;

    @NotNull(message = "El número de parques no puede estar vacío")
    @Min(value = 0, message = "El número de parques no puede ser negativo")
    @Column
    private Integer numParques;

    @NotNull(message = "El número de colegios no puede estar vacío")
    @Min(value = 0, message = "El número de colegios no puede ser negativo")
    @Column
    private Integer numColegios;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
}