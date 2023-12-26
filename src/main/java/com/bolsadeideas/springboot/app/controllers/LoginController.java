package com.bolsadeideas.springboot.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model, Principal principal, RedirectAttributes flash){

        model.addAttribute("titulo", "Iniciar Sesion");

        // si es true, es porque ya habia iniciado sesion antes
        if(principal != null){

            flash.addFlashAttribute("error", "Ya ha iniciado sesion anteriormente");
            return "redirect:/";
        }

        // en caso de que usuario o contraseña sean incorrectos
        if(error != null){
            model.addAttribute("error", "Error: nombre de usuario o contraseña incorrectos");
        }

        if(logout != null){
            model.addAttribute("success", "Cerro sesion con exito!");
        }

        return "login";
    }
}
