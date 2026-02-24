package com.utc.proyect.controller;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        model.addAttribute("loginError", error != null);
        model.addAttribute("logoutSuccess", logout != null);
        return "login";
    }

    @GetMapping("/home")
    public String home(
            Authentication authentication,
            @RequestParam(value = "loginSuccess", defaultValue = "false") boolean loginSuccess) {
        if (authentication == null) {
            return "redirect:/login";
        }

        Set<String> roles = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority() == null ? "" : authority.getAuthority().trim().toUpperCase())
                .collect(Collectors.toSet());

        boolean isAdmin = roles.contains("ROLE_ADMIN") || roles.contains("ADMIN");
        if (isAdmin) {
            return loginSuccess ? "redirect:/admin/panel?loginSuccess=true" : "redirect:/admin/panel";
        }

        boolean isUser = roles.contains("ROLE_USER") || roles.contains("USER");
        if (isUser) {
            return loginSuccess ? "redirect:/user/panel?loginSuccess=true" : "redirect:/user/panel";
        }

        return "home";
    }

    @GetMapping("/admin/panel")
    public String adminPanel(
            @RequestParam(value = "loginSuccess", required = false) String loginSuccess,
            Model model) {
        model.addAttribute("loginSuccess", loginSuccess != null);
        return "admin";
    }

    @GetMapping("/user/panel")
    public String userPanel(
            @RequestParam(value = "loginSuccess", required = false) String loginSuccess,
            Model model) {
        model.addAttribute("loginSuccess", loginSuccess != null);
        return "usuario";
    }
}
