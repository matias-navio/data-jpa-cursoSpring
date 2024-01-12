package com.bolsadeideas.springboot.app.controllers;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import com.bolsadeideas.springboot.app.models.entity.Producto;
import com.bolsadeideas.springboot.app.models.services.IClienteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private MessageSource messageSource;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id,
                      Model model,
                      RedirectAttributes flash,
                      Locale locale){

        Factura factura = clienteService.findFacturaById(id);

        if(factura == null){
            flash.addFlashAttribute("error", messageSource.getMessage("text.factura.ver.error", null, locale));
            return "redirect:/listar";
        }

        model.addAttribute("factura", factura);
        model.addAttribute("titulo", messageSource.getMessage("text.factura.ver.titulo", null, locale).concat(factura.getDescripcion()));

        return "factura/ver";
    }

    @GetMapping("/form/{clienteId}")
    public String crear(@PathVariable(value = "clienteId") Long clienteId,
                        Map<String, Object> model,
                        RedirectAttributes flash,
                        Locale locale){

        Cliente cliente = clienteService.findOne(clienteId);

        if(cliente == null){
            flash.addFlashAttribute("error", messageSource.getMessage("text.factura.crear.error", null, locale));
            return "redirect:/listar";
        }

        Factura factura = new Factura();
        // creamos una factura y se la asignamos a un cliente,
        // de sesta manera se relacionan
        factura.setCliente(cliente);

        model.put("factura", factura);
        model.put("boton", messageSource.getMessage("text.btn.crearFactura", null, locale));
        model.put("titulo", messageSource.getMessage("text.factura.crear.titulo", null, locale));

        return "factura/form";
    }

    // busca el producto por la URL y convierte lo que devuelve a formato json
    @GetMapping(value = "/cargar-productos/{term}", produces = {"application/json"})
    public @ResponseBody List<Producto> cargarProductos(@PathVariable String term){

        return clienteService.findByNombre(term);
    }

    @PostMapping("/form")
    public String guardar(@Valid Factura factura,
                          BindingResult result,
                          Model model,
                          @RequestParam(name = "item_id[]", required = false) Long[] itemId,
                          @RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
                          RedirectAttributes flash, SessionStatus sesion, Locale locale){


            if(result.hasErrors()){
                model.addAttribute("error", "Crear factura");
                return "factura/form";
            }

            if(itemId == null || itemId.length == 0){
                model.addAttribute("error", "Crear factura");
                model.addAttribute("error", messageSource.getMessage("text.factura.guardar.error", null, locale));
                return "factura/form";
            }

            // este for sirve para guardar cada linea de la factura
            for(int i = 0; i < itemId.length; i++) {
                // busca el producto por su id que lo obtenemos de itemId[]
                Producto producto = clienteService.findProductoById(itemId[i]);

                // estas operaciones se encargan de guardar cada elemento de la las lineas de la factura
                ItemFactura linea = new ItemFactura();
                linea.setCantidad(cantidad[i]);
                linea.setProducto(producto);
                factura.addItemFactura(linea);

                log.info("ID: " + itemId[i].toString() + ", cantidad: " + cantidad[i].toString());
            }



        clienteService.saveFactura(factura);
        sesion.setComplete();
        flash.addFlashAttribute("succes", messageSource.getMessage("text.factura.guardar.success", null, locale));

        return "redirect:/ver/"+factura.getCliente().getId();
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id,
                           RedirectAttributes flash,
                           Locale locale){

        Factura factura = clienteService.findFacturaById(id);
        if(factura != null){
            clienteService.deleteFactura(id);
            flash.addFlashAttribute("success", messageSource.getMessage("text.factura.eliminar.success", null, locale));
            return "redirect:/ver/"+factura.getCliente().getId();
        }

        flash.addFlashAttribute("error", messageSource.getMessage("text.fatura.eliminar.error", null, locale));
        return "redirect:/listar";
    }
}
