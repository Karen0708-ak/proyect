package com.utc.proyect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;

import com.utc.proyect.entity.Usuario;
import com.utc.proyect.service.UsuarioService;

@Controller
public class RegisterController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.registrarUsuario(usuario);
            redirectAttributes.addFlashAttribute("success", "Usuario registrado correctamente. Inicia sesion.");
            return "redirect:/login";
        } catch (IllegalArgumentException | IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/registro";
        }
    }
}
