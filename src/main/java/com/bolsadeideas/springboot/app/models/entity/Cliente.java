package com.bolsadeideas.springboot.app.models.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// sirva para declarar uan clase como POJO, es decir clase con atributos, getter and setter
// y que está mapeada a una tabla
@Entity
@Table(name = "clientes")
// serializable se usa para transformar un objeto en una secuencia de bytes y almacenarlo en una BD
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id // indica que es la llave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincremental
    private Long id;

    @NotEmpty
    private String nombre;

    @NotEmpty
    private String apellido;

    @NotEmpty
    @Email
    private String email;

    @Column(name = "create_at")
    @Temporal(TemporalType.DATE) // formato en que se va a guardar esta fecha
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @Past
    @JsonFormat(pattern = "yyyy-MM-dd") // para que lo muestre de esa manera en el json
    private Date createAt;

    // relacion del cliente con las facturas, One hace referencia al cliente (un cliente, muchas facturas)
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference // parte delantera de la relacion que si queremos mostrar
    private List<Factura> facturas;

    public Cliente() {
        facturas = new ArrayList<>();
    }

    private String foto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

    public void addFactura(Factura factura){

        facturas.add(factura);
    }


}
