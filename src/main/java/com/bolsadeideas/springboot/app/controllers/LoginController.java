package com.bolsadeideas.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Locale;

@Controller
public class LoginController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model, Principal principal, RedirectAttributes flash, Locale locale){

        model.addAttribute("titulo", messageSource.getMessage("text.login.titulo", null, locale));

        // si es true, es porque ya habia iniciado sesion antes
        if(principal != null){

            flash.addFlashAttribute("error", messageSource.getMessage("text.login.error1", null, locale));
            return "redirect:/";
        }

        // en caso de que usuario o contraseña sean incorrectos
        if(error != null){
            model.addAttribute("error", messageSource.getMessage("text.login.error2", null, locale));
        }

        if(logout != null){
            model.addAttribute("success", messageSource.getMessage("text.login.success", null, locale));
        }

        return "login";
    }
}
