package com.bolsadeideas.springboot.app.controllers;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.services.IClienteService;
import com.bolsadeideas.springboot.app.models.services.IUploadFileService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;
import jakarta.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

@Controller
// crea una sesión con los datos del objeto cliente y los pasas a la vista
@SessionAttributes("cliente")
public class ClienteController {


//    @Qualifier("clienteDaoJPA") // sirve en caso de que se inyecte IClienteDao en dos o mas componentes
    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IUploadFileService uploadFileService;

    @Autowired
    private MessageSource messageSource;

    protected final Log logger = LogFactory.getLog(this.getClass());

    public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

        Resource recurso = null;
        try{
             recurso =  uploadFileService.load(filename);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ recurso.getFilename() + "\"")
                .body(recurso);
    }

    @GetMapping(value = "/ver/{id}")
    public String detalles(@PathVariable(name = "id") Long id,
                           Map<String, Object> model,
                           RedirectAttributes flash,
                           Locale locale){

        Cliente cliente = clienteService.fetchByIdWithFacturas(id);
//      findOne(id);

        if(cliente == null){
            flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.detalles.error", null, locale));
            return "redirect:/listar";
        }

        model.put("cliente", cliente);
        model.put("titulo", messageSource.getMessage("text.cliente.ver.titulo" ,null, locale) + cliente.getNombre() + " " + cliente.getApellido());

        return "ver";
    }

    // primero se especifica la ruta, y despues el motodo que por defecto es GET
    @RequestMapping(value = {"/listar", "/"}, method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page,
                         Model model,
                         Authentication authentication,
                         Locale locale){

        Pageable pageRequest = PageRequest.of(page, 5);

        // de esta manera podemos usar authentication en distintas partes del programa
        // sin encesidad de pasarlo por parametro
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // athentication pasado por parametro
        if(authentication != null){
            logger.info("Hola usuario autenticado de manera estática, tu username es: ".concat(authentication.getName()));

            if(hasRole("ROLE_ADMIN")){
                logger.info("Hola ".concat(auth.getName()).concat(" tienes acceso!"));
            }else{
                logger.info("Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
            }
        }

        // authentication obtenido de manera estática
        if(auth != null){
            logger.info("Hola usuario autenticado, tu username es: ".concat(auth.getName()));
        }

        Page<Cliente> clientes = clienteService.findAll(pageRequest);

        // creamos el objeto del paginador con su url y despues lo pasamos a la vista
        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

        model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo", null, locale));
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);

        return "listar";
    }

    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model, Locale locale){

        Cliente cliente = new Cliente();
        model.put("cliente", cliente);

        model.put("titulo", messageSource.getMessage("text.cliente.crear.titulo", null, locale));

        return "form";
    }

    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id,
                         Map<String, Object> model,
                         RedirectAttributes flash,
                         Locale locale){

        Cliente cliente = null;
        if(id > 0){
            // si el id es mayor a 0 lo busca la clase Dao
            cliente = clienteService.findOne(id);

            if(cliente == null){
                flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.editar.error1", null, locale));
                return "redirect:/listar";
            }
        }else{
            flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.editar.error2", null, locale));
            return "redirect:/listar";
        }

        // pasa a la vista el cliente correspondiente al id
        model.put("cliente", cliente);
        model.put("titulo", messageSource.getMessage("text.cliente.editar.titulo", null, locale));

        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result,
                          Model model, @RequestParam("file") MultipartFile foto,
                          RedirectAttributes flash, SessionStatus sesion, Locale locale) {

        // verifica si hay errores, si los hay muestra un msj para que el usuario los corrija
        // sino guarda al cliente y retorna listar
        if(result.hasErrors()){
            model.addAttribute("titulo", "Formulario de cliente");
            return "form";
        }

        // verifica que el string de la foto no está vacío
        if(!foto.isEmpty()){

            // verificamos si el cliente existe,
            // en caso de que si se va a editar y cambiar la foto que tenia por la nueva
            if(cliente.getId() != null
                    && cliente.getId() > 0
                    && cliente.getFoto() != null
                    && cliente.getFoto().length() > 0){

               uploadFileService.delete(cliente.getFoto());
            }

            String uniqueFileName = null;

            try{
                uniqueFileName = uploadFileService.copy(foto);
            }catch (IOException e){
                e.printStackTrace();
            }

            flash.addFlashAttribute("foto", messageSource.getMessage("text.cliente.guardar.foto", null, locale) + "'" + uniqueFileName + "'");

            cliente.setFoto(uniqueFileName);

        }

        String mensajeFlash = (cliente.getId() != null)? messageSource.getMessage("text.cliente.guardar.editado", null, locale) : messageSource.getMessage("text.cliente.guardar.creado", null, locale);

        // recibe el objeto cliente y lo guarda
        clienteService.save(cliente);

        // elimina el objeto cliente de la sesion
        sesion.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:listar";
    }

    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Locale locale){

        if(id > 0){
            // busca el cliente y lo elimina
            Cliente cliente = clienteService.findOne(id);

            clienteService.delete(id);

            flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.eliminar.error", null, locale));

            if(uploadFileService.delete(cliente.getFoto())){
                flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.form.foto", null, locale) + cliente.getFoto() + messageSource.getMessage("text.cliente.eliminar.foto", null, locale));
            }

        }

        return "redirect:/listar";
    }

    private boolean hasRole(String role){

        SecurityContext context = SecurityContextHolder.getContext();

        if(context == null){
            return false;
        }

        Authentication auth = context.getAuthentication();

        if(auth == null){
            return false;
        }

        // cualuier clase que represente un role tiene que implementar la interface GrantedAuthority
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        for(GrantedAuthority authority : authorities){

            // comparamos si role es igual al role obtenido con getAuthority
            if(role.equals(authority.getAuthority())){
                logger.info("Hola usuario ".concat(auth.getName()).concat(" tu role es: ").concat(authority.getAuthority()));
                return true;
            }
        }

        return false;
    }
}
