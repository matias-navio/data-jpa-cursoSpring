
/*
package com.bolsadeideas.springboot.app.view.json;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Map;

@Component("listar.json")
public class ClienteListJsonView extends MappingJackson2JsonView {

    @Override
    protected Object filterModel(Map<String, Object> model) {

        // esto elimina las entradas de titulo y page que vienen del mapeo de la vista
        model.remove("titulo");
        model.remove("page");

        // esto obtiene de la vista lo que esta mapeado con la clave clientes
        Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
        model.remove("clientes"); // elimina el mapeo con la clave clientes
        model.put("clientes", clientes.getContent()); // agrega la entrada clientes, pero contiene lo del Page

        return super.filterModel(model);
    }
}

 */
