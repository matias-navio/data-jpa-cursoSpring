package com.bolsadeideas.springboot.app.models.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
// que sea unique quiere decir que no pueden haber valores duplicados en las columnas
@Table(name = "authorities", uniqueConstraints = {@UniqueConstraint(columnNames = {"authority","user_id"})})
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    private static final Long serialVersionUID = 1L;

}
