package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Service.MunicipioService;
import com.example.demo.model.Municipio;

import jakarta.validation.Valid; 

@Controller
@RequestMapping("/municipios")
public class MunicipioFormController {

    @Autowired
    private MunicipioService municipioService;

    
    @GetMapping("/new")
    public String showAddMunicipioForm(Model model) {
        model.addAttribute("municipio", new Municipio());
        return "municipio_form";
    }

    
    @GetMapping("/edit/{id}") 
    public String showEditMunicipioForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Municipio> optionalMunicipio = municipioService.getMunicipioById(id);
        if (optionalMunicipio.isPresent()) {
            model.addAttribute("municipio", optionalMunicipio.get());
            return "municipio_form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Municipio no encontrado para editar.");
            return "redirect:/"; 
        }
    }

    
    @PostMapping("/save")
    public String saveMunicipio(@Valid @ModelAttribute("municipio") Municipio municipio,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "municipio_form"; 
        }

        
        
        if (municipio.getId() == null &&
            municipioService.existsByNombreDepartamentoPais(
                municipio.getNombre(), municipio.getDepartamento(), municipio.getPais())) {
            result.rejectValue("nombre", "duplicate", "Ya existe un municipio con este nombre, departamento y país.");
            result.rejectValue("departamento", "duplicate");
            result.rejectValue("pais", "duplicate");
            return "municipio_form";
        }

        
        
        
        if (municipio.getId() != null) {
            Optional<Municipio> existing = municipioService.getMunicipioByNombre(municipio.getNombre());
            if (existing.isPresent() && !existing.get().getId().equals(municipio.getId())) {
                
                result.rejectValue("nombre", "duplicate", "Ya existe otro municipio con este nombre.");
                return "municipio_form";
            }
            
        }


        municipioService.saveMunicipio(municipio); 
        redirectAttributes.addFlashAttribute("successMessage", "Municipio guardado exitosamente!");
        return "redirect:/"; 
    }

     @PostMapping("/delete/{id}") 
    public String deleteMunicipio(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (municipioService.deleteMunicipio(id)) { 
            redirectAttributes.addFlashAttribute("successMessage", "Municipio eliminado exitosamente!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "No se pudo eliminar el municipio o no fue encontrado.");
        }
        return "redirect:/"; 
    }
}