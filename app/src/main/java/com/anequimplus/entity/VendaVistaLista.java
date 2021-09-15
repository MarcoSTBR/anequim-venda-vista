package com.anequimplus.entity;

public class VendaVistaLista {

    private int id ;
    private VendaVista vendaVista ;
    private int status ;

    public VendaVistaLista(int id, VendaVista vendaVista, int status) {
        this.id = id ;
        this.vendaVista = vendaVista;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VendaVista getVendaVista() {
        return vendaVista;
    }

    public void setVendaVista(VendaVista vendaVista) {
        this.vendaVista = vendaVista;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
