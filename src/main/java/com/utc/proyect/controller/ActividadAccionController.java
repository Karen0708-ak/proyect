package com.utc.proyect.controller;

import java.util.NoSuchElementException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utc.proyect.entity.AccionPlanAmbiental;
import com.utc.proyect.entity.ActividadAccion;
import com.utc.proyect.service.AccionPlanAmbientalService;
import com.utc.proyect.service.ActividadAccionService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/actividades")
public class ActividadAccionController {

    private final ActividadAccionService actividadAccionService;
    private final AccionPlanAmbientalService accionPlanAmbientalService;

    public ActividadAccionController(
            ActividadAccionService actividadAccionService,
            AccionPlanAmbientalService accionPlanAmbientalService) {
        this.actividadAccionService = actividadAccionService;
        this.accionPlanAmbientalService = accionPlanAmbientalService;
    }

    @GetMapping
    public String listarTodas(Model model, Authentication authentication) {
        model.addAttribute("actividades", actividadAccionService.listarTodas());
        model.addAttribute("isAdmin", hasRole(authentication, "ROLE_ADMIN"));
        return "actividades/index";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        ActividadAccion actividadAccion = new ActividadAccion();
        actividadAccion.setAccionPlanAmbiental(new AccionPlanAmbiental());
        model.addAttribute("actividadAccion", actividadAccion);
        cargarAcciones(model);
        return "actividades/form";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("actividadAccion") ActividadAccion actividadAccion,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        validarFechas(actividadAccion, bindingResult);
        validarAccionSeleccionada(actividadAccion, bindingResult);

        if (bindingResult.hasErrors()) {
            cargarAcciones(model);
            return "actividades/form";
        }

        actividadAccionService.guardar(actividadAccion);
        redirectAttributes.addFlashAttribute("successMessage", "Actividad creada correctamente.");
        return "redirect:/actividades";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        ActividadAccion actividadAccion = actividadAccionService.buscarPorId(id).orElse(null);
        if (actividadAccion == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "La actividad solicitada no existe.");
            return "redirect:/actividades";
        }
        if (actividadAccion.getAccionPlanAmbiental() == null) {
            actividadAccion.setAccionPlanAmbiental(new AccionPlanAmbiental());
        }

        model.addAttribute("actividadAccion", actividadAccion);
        cargarAcciones(model);
        return "actividades/form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("actividadAccion") ActividadAccion actividadAccion,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        validarFechas(actividadAccion, bindingResult);
        validarAccionSeleccionada(actividadAccion, bindingResult);

        if (bindingResult.hasErrors()) {
            actividadAccion.setCodigoActividad(id);
            cargarAcciones(model);
            return "actividades/form";
        }

        try {
            actividadAccionService.actualizar(id, actividadAccion);
            redirectAttributes.addFlashAttribute("successMessage", "Actividad actualizada correctamente.");
            return "redirect:/actividades";
        } catch (NoSuchElementException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "La actividad ya no existe.");
            return "redirect:/actividades";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            actividadAccionService.eliminar(id);
            redirectAttributes.addFlashAttribute("successMessage", "Actividad eliminada correctamente.");
        } catch (EmptyResultDataAccessException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "La actividad ya no existe o fue eliminada.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "No fue posible eliminar la actividad.");
        }
        return "redirect:/actividades";
    }

    private void validarFechas(ActividadAccion actividadAccion, BindingResult bindingResult) {
        if (actividadAccion.getFechaInicialActividad() != null
                && actividadAccion.getFechaMaxActividad() != null
                && actividadAccion.getFechaMaxActividad().isBefore(actividadAccion.getFechaInicialActividad())) {
            bindingResult.addError(new FieldError(
                    "actividadAccion",
                    "fechaMaxActividad",
                    "La fecha maxima debe ser posterior o igual a la fecha inicial."));
        }
    }

    private void validarAccionSeleccionada(ActividadAccion actividadAccion, BindingResult bindingResult) {
        if (actividadAccion.getAccionPlanAmbiental() == null
                || actividadAccion.getAccionPlanAmbiental().getCodigoAccion() == null) {
            bindingResult.rejectValue("accionPlanAmbiental.codigoAccion", "accion.invalida",
                    "Debe seleccionar una accion valida.");
            return;
        }

        Long codigoAccion = actividadAccion.getAccionPlanAmbiental().getCodigoAccion();
        AccionPlanAmbiental accionValida = accionPlanAmbientalService.buscarPorId(codigoAccion).orElse(null);
        if (accionValida == null) {
            bindingResult.rejectValue("accionPlanAmbiental.codigoAccion", "accion.noExiste",
                    "La accion seleccionada no existe.");
            return;
        }

        actividadAccion.setAccionPlanAmbiental(accionValida);
    }

    private void cargarAcciones(Model model) {
        model.addAttribute("accionesDisponibles", accionPlanAmbientalService.listarTodas());
    }

    private boolean hasRole(Authentication authentication, String role) {
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }
}
