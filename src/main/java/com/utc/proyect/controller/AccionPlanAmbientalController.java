package com.utc.proyect.controller;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
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

import com.utc.proyect.entity.AccionPlanAmbiental;
import com.utc.proyect.entity.SeccionPlanAmbiental;
import com.utc.proyect.repository.AccionPlanAmbientalRepository;
import com.utc.proyect.repository.SeccionPlanAmbientalRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/acciones")
public class AccionPlanAmbientalController {

    private final AccionPlanAmbientalRepository accionRepository;
    private final SeccionPlanAmbientalRepository seccionRepository;

    public AccionPlanAmbientalController(
            AccionPlanAmbientalRepository accionRepository,
            SeccionPlanAmbientalRepository seccionRepository) {
        this.accionRepository = accionRepository;
        this.seccionRepository = seccionRepository;
    }

    @GetMapping
    public String listar(
            @RequestParam(value = "editar", required = false) Long editarId,
            Model model,
            Authentication authentication) {

        List<AccionPlanAmbiental> acciones = accionRepository.findAll(Sort.by(Sort.Direction.DESC, "codigoAccion"));
        List<SeccionPlanAmbiental> secciones = seccionRepository.findAll(Sort.by(Sort.Direction.ASC, "numeroSeccion"));

        AccionPlanAmbiental accionForm = new AccionPlanAmbiental();
        Long seccionIdSeleccionada = null;

        if (editarId != null) {
            accionForm = accionRepository.findById(editarId).orElse(new AccionPlanAmbiental());
            if (accionForm.getSeccion() != null) {
                seccionIdSeleccionada = accionForm.getSeccion().getCodigoSeccion();
            }
        }

        prepararVista(model, authentication, acciones, secciones, accionForm, seccionIdSeleccionada, false, null);

        return "acciones";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("accionForm") AccionPlanAmbiental payload,
            BindingResult bindingResult,
            @RequestParam(value = "seccionId", required = false) Long seccionId,
            Model model,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        SeccionPlanAmbiental seccion = null;
        if (seccionId == null) {
            bindingResult.reject("seccionId", "La seccion es obligatoria.");
        } else {
            seccion = seccionRepository.findById(seccionId).orElse(null);
            if (seccion == null) {
                bindingResult.reject("seccionId", "La seccion seleccionada no existe.");
            }
        }

        if (bindingResult.hasErrors()) {
            List<AccionPlanAmbiental> acciones = accionRepository.findAll(Sort.by(Sort.Direction.DESC, "codigoAccion"));
            List<SeccionPlanAmbiental> secciones = seccionRepository.findAll(Sort.by(Sort.Direction.ASC, "numeroSeccion"));
            prepararVista(model, authentication, acciones, secciones, payload, seccionId, true, "Corrige los errores del formulario.");
            return "acciones";
        }

        boolean modoEdicion = payload.getCodigoAccion() != null;
        AccionPlanAmbiental accion = modoEdicion
                ? accionRepository.findById(payload.getCodigoAccion()).orElse(new AccionPlanAmbiental())
                : new AccionPlanAmbiental();

        accion.setSeccion(seccion);
        accion.setAspectoAmbientalAccion(payload.getAspectoAmbientalAccion());
        accion.setImpactoAmbientalAccion(payload.getImpactoAmbientalAccion());
        accion.setMedidasPropuestasAccion(payload.getMedidasPropuestasAccion());
        accion.setNumeradorValorAccion(payload.getNumeradorValorAccion());
        accion.setDenominadorValorAccion(payload.getDenominadorValorAccion());
        accion.setEstadoAplica(payload.getEstadoAplica());
        accion.setColorAccion(payload.getColorAccion());
        accion.setValorAccion(payload.getValorAccion());
        accion.setFrecuenciaAccion(payload.getFrecuenciaAccion());
        accion.setPeriodoAccion(payload.getPeriodoAccion());

        accionRepository.save(accion);

        redirectAttributes.addFlashAttribute("successMessage",
                modoEdicion ? "Accion actualizada correctamente." : "Accion creada correctamente.");
        return "redirect:/acciones";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            accionRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Accion eliminada correctamente.");
        } catch (EmptyResultDataAccessException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "La accion ya no existe o fue eliminada.");
        }
        return "redirect:/acciones";
    }

    private void prepararVista(
            Model model,
            Authentication authentication,
            List<AccionPlanAmbiental> acciones,
            List<SeccionPlanAmbiental> secciones,
            AccionPlanAmbiental accionForm,
            Long seccionIdSeleccionada,
            boolean abrirModal,
            String errorFormulario) {
        model.addAttribute("acciones", acciones);
        model.addAttribute("secciones", secciones);
        model.addAttribute("accionForm", accionForm);
        model.addAttribute("seccionIdSeleccionada", seccionIdSeleccionada);
        model.addAttribute("modoEdicion", accionForm.getCodigoAccion() != null);
        model.addAttribute("abrirModal", abrirModal);
        model.addAttribute("errorFormulario", errorFormulario);
        model.addAttribute("isAdmin", hasRole(authentication, "ROLE_ADMIN"));
    }

    private boolean hasRole(Authentication authentication, String role) {
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }
}
