/*package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Repository.MunicipioRepository;
import com.example.demo.Service.MunicipioService;
import com.example.demo.model.Municipio;

/**
 * Clase de pruebas para el servicio de Municipio.
 * Utiliza @SpringBootTest y @Transactional para un entorno de pruebas robusto.

@SpringBootTest
@Transactional
public class MunicipioServiceTest {

    @Autowired
    private MunicipioService municipioService;

    @Autowired
    private MunicipioRepository municipioRepository;
   

    @Test
    void testSaveMunicipio() {
        // Dado
        Municipio municipio = new Municipio(null, "Cartagena", "Bolívar", "Colombia", "Alcalde C.", "Gobernador B.", "Virgen de la Candelaria", 1000000, 200000, 50, 100, "Ciudad histórica y turística.");

        // Cuando
        Municipio savedMunicipio = municipioService.saveMunicipio(municipio);

        // Entonces
        assertNotNull(savedMunicipio.getId(), "El ID del municipio no debería ser nulo después de guardar");
        assertEquals("Cartagena", savedMunicipio.getNombre(), "El nombre del municipio guardado no coincide");
        assertTrue(municipioRepository.findByNombre("Cartagena").isPresent(), "El municipio debería encontrarse por nombre");
    }

    @Test
    void testGetAllMunicipios() {
        // Dado
        municipioService.saveMunicipio(new Municipio(null, "Cartagena", "Bolívar", "Colombia", null, null, null, null, null, null, null, null));
        municipioService.saveMunicipio(new Municipio(null, "Medellín", "Antioquia", "Colombia", null, null, null, null, null, null, null, null));

        // Cuando
        List<Municipio> municipios = municipioService.getAllMunicipios();

        // Entonces
        assertEquals(2, municipios.size(), "Debería haber dos municipios en la base de datos");
    }

    @Test
    void testGetMunicipioById() {
        // Dado
        Municipio municipio = municipioService.saveMunicipio(new Municipio(null, "Cartagena", "Bolívar", "Colombia", null, null, null, null, null, null, null, null));

        // Cuando
        Optional<Municipio> foundMunicipio = municipioService.getMunicipioById(municipio.getId());

        // Entonces
        assertTrue(foundMunicipio.isPresent(), "El municipio debería encontrarse por ID");
        assertEquals("Cartagena", foundMunicipio.get().getNombre(), "El nombre del municipio recuperado no coincide");
    }

    @Test
    void testGetMunicipioByIdNotFound() {
        // Cuando
        Optional<Municipio> foundMunicipio = municipioService.getMunicipioById(99L); // ID que no existe

        // Entonces
        assertFalse(foundMunicipio.isPresent(), "No debería encontrarse un municipio con un ID inexistente");
    }

    @Test
    void testGetMunicipioByNombre() {
        // Dado
        municipioService.saveMunicipio(new Municipio(null, "Cartagena", "Bolívar", "Colombia", null, null, null, null, null, null, null, null));

        // Cuando
        Optional<Municipio> foundMunicipio = municipioService.getMunicipioByNombre("Cartagena");

        // Entonces
        assertTrue(foundMunicipio.isPresent(), "El municipio debería encontrarse por nombre");
        assertEquals("Bolívar", foundMunicipio.get().getDepartamento(), "El departamento del municipio recuperado no coincide");
    }

    @Test
    void testGetMunicipiosByDepartamento() {
        // Dado
        municipioService.saveMunicipio(new Municipio(null, "Cartagena", "Bolívar", "Colombia", null, null, null, null, null, null, null, null));
        municipioService.saveMunicipio(new Municipio(null, "Magangué", "Bolívar", "Colombia", null, null, null, null, null, null, null, null));
        municipioService.saveMunicipio(new Municipio(null, "Medellín", "Antioquia", "Colombia", null, null, null, null, null, null, null, null));

        // Cuando
        List<Municipio> municipiosBolivar = municipioService.getMunicipiosByDepartamento("Bolívar");

        // Entonces
        assertEquals(2, municipiosBolivar.size(), "Debería haber dos municipios en Bolívar");
        assertTrue(municipiosBolivar.stream().anyMatch(m -> m.getNombre().equals("Cartagena")), "Cartagena debería estar en la lista");
        assertTrue(municipiosBolivar.stream().anyMatch(m -> m.getNombre().equals("Magangué")), "Magangué debería estar en la lista");
    }

    @Test
    void testGetMunicipiosByPais() {
        // Dado
        municipioService.saveMunicipio(new Municipio(null, "Cartagena", "Bolívar", "Colombia", null, null, null, null, null, null, null, null));
        municipioService.saveMunicipio(new Municipio(null, "Medellín", "Antioquia", "Colombia", null, null, null, null, null, null, null, null));
        municipioService.saveMunicipio(new Municipio(null, "Quito", "Pichincha", "Ecuador", null, null, null, null, null, null, null, null));

        // Cuando
        List<Municipio> municipiosColombia = municipioService.getMunicipiosByPais("Colombia");

        // Entonces
        assertEquals(2, municipiosColombia.size(), "Debería haber dos municipios en Colombia");
        assertTrue(municipiosColombia.stream().anyMatch(m -> m.getNombre().equals("Cartagena")), "Cartagena debería estar en la lista");
        assertTrue(municipiosColombia.stream().anyMatch(m -> m.getNombre().equals("Medellín")), "Medellín debería estar en la lista");
    }

    @Test
    void testUpdateMunicipio() {
        // Dado
        Municipio municipio = municipioService.saveMunicipio(new Municipio(null, "Cartagena", "Bolívar", "Colombia", "Alcalde A", "Gobernador X", null, 100, 10, 1, 1, "Desc original"));
        Municipio updatedDetails = new Municipio(null, "Cartagena de Indias", "Bolívar", "Colombia", "Alcalde B", "Gobernador Y", "Virgen", 120, 12, 2, 2, "Desc actualizada");

        // Cuando
        Municipio updatedMunicipio = municipioService.updateMunicipio(municipio.getId(), updatedDetails);

        // Entonces
        assertNotNull(updatedMunicipio, "El municipio actualizado no debería ser nulo");
        assertEquals("Cartagena de Indias", updatedMunicipio.getNombre(), "El nombre del municipio debería haberse actualizado");
        assertEquals("Alcalde B", updatedMunicipio.getAlcalde(), "El alcalde debería haberse actualizado");
        assertEquals(120, updatedMunicipio.getNumHabitantes(), "El número de habitantes debería haberse actualizado");
    }

    @Test
    void testUpdateMunicipioNotFound() {
        // Dado
        Municipio updatedDetails = new Municipio(null, "Ciudad Falsa", "Departamento Falso", "País Falso", null, null, null, null, null, null, null, null);

        // Cuando
        Municipio updatedMunicipio = municipioService.updateMunicipio(99L, updatedDetails); // ID que no existe

        // Entonces
        assertNull(updatedMunicipio, "Debería retornar null si el municipio a actualizar no se encuentra");
    }

    @Test
    void testDeleteMunicipio() {
        // Dado
        Municipio municipio = municipioService.saveMunicipio(new Municipio(null, "Cartagena", "Bolívar", "Colombia", null, null, null, null, null, null, null, null));

        // Cuando
        boolean isDeleted = municipioService.deleteMunicipio(municipio.getId());

        // Entonces
        assertTrue(isDeleted, "El municipio debería haber sido eliminado exitosamente");
        assertFalse(municipioRepository.findById(municipio.getId()).isPresent(), "El municipio no debería encontrarse después de la eliminación");
    }

    @Test
    void testDeleteMunicipioNotFound() {
        // Cuando
        boolean isDeleted = municipioService.deleteMunicipio(99L); // ID que no existe

        // Entonces
        assertFalse(isDeleted, "No debería reportar éxito al intentar eliminar un municipio inexistente");
    }

    @Test
    void testExistsByNombreDepartamentoPais() {
        // Dado
        municipioService.saveMunicipio(new Municipio(null, "Cartagena", "Bolívar", "Colombia", null, null, null, null, null, null, null, null));

        // Cuando & Entonces
        assertTrue(municipioService.existsByNombreDepartamentoPais("Cartagena", "Bolívar", "Colombia"), "Debería retornar true para un municipio existente");
        assertFalse(municipioService.existsByNombreDepartamentoPais("Barranquilla", "Atlántico", "Colombia"), "Debería retornar false para un municipio inexistente");
    }
}*/