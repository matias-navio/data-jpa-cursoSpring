package com.bolsadeideas.springboot.app.controllers;

import com.bolsadeideas.springboot.app.models.dao.IClienteDao;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class ClienteController {

    @Autowired
    @Qualifier("clienteDaoJPA") // sirve en caso de que se inyecte IClienteDao en dos o mas componentes
    private IClienteDao clienteDao;

    // primero se especifica la ruta, y despues el motodo que por defecto es GET
    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public String listar(Model model){

        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clienteDao.findAll());

        return "listar";
    }

    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model){

        Cliente cliente = new Cliente();
        model.put("cliente", cliente);

        model.put("titulo", "Formulario de cliente");

        return "form";
    }

    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model){

        Cliente cliente = null;
        if(id > 0){
            // si el id es mayor a 0 lo busca la clase Dao
            cliente = clienteDao.findOne(id);
        }else{
            return "redirect:listar";
        }

        // pasa a la vista el cliente correspondiente al id
        model.put("cliente", cliente);
        model.put("titulo", "Editar cliente");

        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model){

        // verifica si hay errores, si los hay muestra un msj para que el usuario los corrija
        // sino guarda al cliente y retorna listar
        if(result.hasErrors()){
            model.addAttribute("titulo", "Formulario de cliente");
            return "form";
        }

        // recibe el objeto cliente y lo guarda
        clienteDao.save(cliente);

        return "redirect:listar";
    }
}
