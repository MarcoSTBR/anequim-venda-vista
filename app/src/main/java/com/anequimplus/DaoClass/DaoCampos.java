package com.anequimplus.DaoClass;

public class DaoCampos {
    private String nome ;
    private DaoCampoTipo tipoCampo ;
    private boolean notNull ;

    public DaoCampos(String nome, DaoCampoTipo tipoCampo, boolean notNull) {
        this.nome = nome;
        this.tipoCampo = tipoCampo;
        this.notNull = notNull;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public DaoCampoTipo getTipoCampo() {
        return tipoCampo;
    }

    public void setTipoCampo(DaoCampoTipo tipoCampo) {
        this.tipoCampo = tipoCampo;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

}
