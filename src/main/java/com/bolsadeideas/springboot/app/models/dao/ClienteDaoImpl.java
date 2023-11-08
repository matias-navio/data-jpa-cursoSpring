package com.bolsadeideas.springboot.app.models.dao;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("clienteDaoJPA")
public class ClienteDaoImpl implements IClienteDao{
    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<Cliente> findAll() {
        return em.createQuery("from Cliente").getResultList();
    }

    @Override
    @Transactional
    public void save(Cliente cliente) {

        if(cliente.getId() != null && cliente.getId() > 0){

            // actializa los datos que ya existen en la base de datos
            em.merge(cliente);
        }else{

            // almacena un objeto entity en un contexto de persistencia en una base de datos
            em.persist(cliente);
        }
    }

    @Override
    public Cliente findOne(Long id) {

        // Busca al cliente por su id
        return em.find(Cliente.class, id);
    }
}
