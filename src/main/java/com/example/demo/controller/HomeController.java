package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.Service.MunicipioService;
import com.example.demo.model.Municipio;

@Controller
public class HomeController {

    @Autowired
    private MunicipioService municipioService;

    @GetMapping("/")
    public String home(Model model) {
        List<Municipio> municipios = municipioService.getAllMunicipios();
        model.addAttribute("municipios", municipios);
        return "home";
    }
}