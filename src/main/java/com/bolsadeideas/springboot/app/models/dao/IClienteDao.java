package com.bolsadeideas.springboot.app.models.dao;


import com.bolsadeideas.springboot.app.models.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;


/*
 CrudRepository nos provee todos los metos necesarios para un CRUD
 sin necesidad de implementarlos nosotros
 */
public interface IClienteDao extends JpaRepository<Cliente, Long> {

}
