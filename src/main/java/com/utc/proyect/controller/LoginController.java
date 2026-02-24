package com.utc.proyect.controller;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        Set<String> roles = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority() == null ? "" : authority.getAuthority().trim().toUpperCase())
                .collect(Collectors.toSet());

        boolean isAdmin = roles.contains("ROLE_ADMIN") || roles.contains("ADMIN");
        if (isAdmin) {
            return "redirect:/admin/panel";
        }

        boolean isUser = roles.contains("ROLE_USER") || roles.contains("USER");
        if (isUser) {
            return "redirect:/user/panel";
        }

        return "home";
    }

    @GetMapping("/admin/panel")
    public String adminPanel() {
        return "admin";
    }

    @GetMapping("/user/panel")
    public String userPanel() {
        return "usuario";
    }
}
