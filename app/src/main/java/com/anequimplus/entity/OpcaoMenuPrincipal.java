package com.anequimplus.entity;

public class OpcaoMenuPrincipal {
    private int id ;
    private int imagem ;
    private String descricao ;
    private String obs ;

    public OpcaoMenuPrincipal(int id, int imagem, String descricao, String obs) {
        this.id = id;
        this.imagem = imagem;
        this.descricao = descricao;
        this.obs = obs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
}
