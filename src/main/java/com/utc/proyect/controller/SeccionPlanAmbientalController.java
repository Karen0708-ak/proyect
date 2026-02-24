package com.utc.proyect.controller;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utc.proyect.entity.SeccionPlanAmbiental;
import com.utc.proyect.repository.SeccionPlanAmbientalRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/secciones")
public class SeccionPlanAmbientalController {

    private final SeccionPlanAmbientalRepository seccionRepository;

    public SeccionPlanAmbientalController(SeccionPlanAmbientalRepository seccionRepository) {
        this.seccionRepository = seccionRepository;
    }

    @GetMapping
    public String listar(
            @RequestParam(value = "editar", required = false) Long editarId,
            Model model,
            Authentication authentication) {

        List<SeccionPlanAmbiental> secciones = seccionRepository.findAll(Sort.by(Sort.Direction.DESC, "codigoSeccion"));

        SeccionPlanAmbiental seccionForm = new SeccionPlanAmbiental();
        if (editarId != null) {
            seccionForm = seccionRepository.findById(editarId).orElse(new SeccionPlanAmbiental());
        }

        prepararVista(model, authentication, secciones, seccionForm, false);
        return "secciones";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("seccionForm") SeccionPlanAmbiental payload,
            BindingResult bindingResult,
            Model model,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            List<SeccionPlanAmbiental> secciones = seccionRepository.findAll(Sort.by(Sort.Direction.DESC, "codigoSeccion"));
            prepararVista(model, authentication, secciones, payload, true);
            return "secciones";
        }

        boolean modoEdicion = payload.getCodigoSeccion() != null;
        SeccionPlanAmbiental seccion = modoEdicion
                ? seccionRepository.findById(payload.getCodigoSeccion()).orElse(new SeccionPlanAmbiental())
                : new SeccionPlanAmbiental();

        seccion.setNumeroSeccion(payload.getNumeroSeccion());
        seccion.setTituloSeccion(payload.getTituloSeccion());
        seccion.setObjetivoSeccion(payload.getObjetivoSeccion());
        seccion.setColorSeccion(payload.getColorSeccion());

        seccionRepository.save(seccion);

        redirectAttributes.addFlashAttribute("successMessage",
                modoEdicion ? "Seccion actualizada correctamente." : "Seccion creada correctamente.");
        return "redirect:/secciones";
    }

    private void prepararVista(
            Model model,
            Authentication authentication,
            List<SeccionPlanAmbiental> secciones,
            SeccionPlanAmbiental seccionForm,
            boolean abrirModal) {
        model.addAttribute("secciones", secciones);
        model.addAttribute("seccionForm", seccionForm);
        model.addAttribute("modoEdicion", seccionForm.getCodigoSeccion() != null);
        model.addAttribute("abrirModal", abrirModal);
        model.addAttribute("isAdmin", hasRole(authentication, "ROLE_ADMIN"));
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            seccionRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Seccion eliminada correctamente.");
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "No se puede eliminar la seccion porque tiene acciones asociadas.");
        }
        return "redirect:/secciones";
    }

    private boolean hasRole(Authentication authentication, String role) {
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }
}
