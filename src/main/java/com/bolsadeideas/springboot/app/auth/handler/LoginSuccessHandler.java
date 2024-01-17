package com.bolsadeideas.springboot.app.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import java.io.IOException;
import java.util.Locale;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        SessionFlashMapManager flashMapManager = new SessionFlashMapManager();
        FlashMap flashMap = new FlashMap();

        Locale locale = localeResolver.resolveLocale(request);

        String mensaje = String.format(messageSource.getMessage("text.login.alerta.success", null, locale) + authentication.getName());

        // mensaje de inicio de sesion exitoso obtenienso el nombre del usuario
        flashMap.put("success", mensaje);

        flashMapManager.saveOutputFlashMap(flashMap, request, response);

        // authentication representa la informacion de autenticacion del usuario
        if(authentication != null){
            logger.info("El usuario '"+authentication.getName()+"' ha iniciado sesion con exito");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
