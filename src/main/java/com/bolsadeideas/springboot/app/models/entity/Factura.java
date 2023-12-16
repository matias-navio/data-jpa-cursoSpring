package com.bolsadeideas.springboot.app.models.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "facturas")
public class Factura implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    private String observasion;

    @Temporal(TemporalType.DATE)
    @Column(name = "create_at")
    private Date createAt;

    @PrePersist
    public void prePersist() {
        createAt = new Date();
    }

    // relacion entre la factura y los clientes, en donde Many hace referencia a las facturas (muchas facturas)
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;

    // indica la llave foranea de la relacion (se hace asi porque la relacion es en un solo sentido)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "factura_id")
    private List<ItemFactura> items;

    public Factura() {

        this.items = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservasion() {
        return observasion;
    }

    public void setObservasion(String observasion) {
        this.observasion = observasion;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemFactura> getItems() {
        return items;
    }

    public void setItems(List<ItemFactura> items) {
        this.items = items;
    }

    public void addItemFactura(ItemFactura item){
        // agrega lso items a la factura pero uno a uno
        this.items.add(item);
    }

    public Double getTotal(){
        Double total = 0.0;
        // cantidad de elementos que tenemos
        int size = items.size();

        for(int i = 0; i < size; i++){
            total += items.get(i).calcularImporte();
        }

        return total;
    }

    private static final Long serialVersionUID = 1L;

}
