package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.MunicipioRepository;
import com.example.demo.model.Municipio;

@Service
public class MunicipioService {

    @Autowired
    private MunicipioRepository municipioRepository;

    public Municipio saveMunicipio(Municipio municipio) {
        // Puedes añadir validaciones o lógica de negocio antes de guardar
        return municipioRepository.save(municipio);
    }

    public List<Municipio> getAllMunicipios() {
        return municipioRepository.findAll();
    }

    public Optional<Municipio> getMunicipioById(Long id) {
        return municipioRepository.findById(id);
    }

    public Optional<Municipio> getMunicipioByNombre(String nombre) {
        return municipioRepository.findByNombre(nombre);
    }

    public List<Municipio> getMunicipiosByDepartamento(String departamento) {
        return municipioRepository.findByDepartamento(departamento);
    }

    public List<Municipio> getMunicipiosByPais(String pais) {
        return municipioRepository.findByPais(pais);
    }

    public Municipio updateMunicipio(Long id, Municipio municipioDetails) {
        Optional<Municipio> optionalMunicipio = municipioRepository.findById(id);
        if (optionalMunicipio.isPresent()) {
            Municipio existingMunicipio = optionalMunicipio.get();
            existingMunicipio.setNombre(municipioDetails.getNombre());
            existingMunicipio.setDepartamento(municipioDetails.getDepartamento());
            existingMunicipio.setPais(municipioDetails.getPais());
            existingMunicipio.setAlcalde(municipioDetails.getAlcalde());
            existingMunicipio.setGobernador(municipioDetails.getGobernador());
            existingMunicipio.setPatronoReligioso(municipioDetails.getPatronoReligioso());
            existingMunicipio.setNumHabitantes(municipioDetails.getNumHabitantes());
            existingMunicipio.setNumCasas(municipioDetails.getNumCasas());
            existingMunicipio.setNumParques(municipioDetails.getNumParques());
            existingMunicipio.setNumColegios(municipioDetails.getNumColegios());
            existingMunicipio.setDescripcion(municipioDetails.getDescripcion());
            return municipioRepository.save(existingMunicipio);
        } else {
            return null; // O lanzar una excepción
        }
    }

    public boolean deleteMunicipio(Long id) {
        if (municipioRepository.existsById(id)) {
            municipioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean existsByNombreDepartamentoPais(String nombre, String departamento, String pais) {
        return municipioRepository.existsByNombreAndDepartamentoAndPais(nombre, departamento, pais);
    }
}