package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class ProdutoNfce {

    private int id ;
    private int PRODUTO_ID ;
    private String CFOP ;
    private String ICMS_CST ;
    private Double ICMS_MODBC ;
    private Double ICMS_ALIQUOTA ;

    private String IPI_CST ;
    private Double IPI_MODBC ;
    private Double IPI_ALIQUOTA ;

    private String PIS_CST ;
    private Double PIS_ALIQUOTA ;

    private String COFINS_CST ;
    private Double COFINS_ALIQUOTA ;

    private Double PFCP ;
    private Double PFCPST ;
    private Double PFCPSTRET ;
    private String CBENEF ;
    private Double PREDBC ;
    private Double MOTDESICMS ;

    public ProdutoNfce(JSONObject j) throws JSONException {
       id              = j.getInt("id");
       PRODUTO_ID      = j.getInt("PRODUTO_ID") ;
       CFOP            = j.getString("CFOP");
       ICMS_CST        = j.getString("ICMS_CST") ;
       ICMS_MODBC      = j.getDouble("ICMS_MODBC")  ;
       ICMS_ALIQUOTA   = j.getDouble("ICMS_ALIQUOTA")  ;
       IPI_CST         = j.getString("IPI_CST") ;
       IPI_MODBC       = j.getDouble("IPI_MODBC")  ;
       IPI_ALIQUOTA    = j.getDouble("IPI_ALIQUOTA")  ;
       PIS_CST         = j.getString("PIS_CST")  ;
       PIS_ALIQUOTA    = j.getDouble("PIS_ALIQUOTA")  ;
       COFINS_CST      = j.getString("COFINS_CST")  ;
       COFINS_ALIQUOTA = j.getDouble("COFINS_ALIQUOTA")  ;
       PFCP            = j.getDouble("PFCP")  ;
       PFCPST          = j.getDouble("PFCPST")  ;
       PFCPSTRET       = j.getDouble("PFCPSTRET")  ;
       CBENEF          = j.getString("CBENEF")  ;
       PREDBC          = j.getDouble("PREDBC")  ;
       MOTDESICMS      = j.getDouble("MOTDESICMS")  ;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPRODUTO_ID() {
        return PRODUTO_ID;
    }

    public void setPRODUTO_ID(int PRODUTO_ID) {
        this.PRODUTO_ID = PRODUTO_ID;
    }

    public String getCFOP() {
        return CFOP;
    }

    public void setCFOP(String CFOP) {
        this.CFOP = CFOP;
    }

    public String getICMS_CST() {
        return ICMS_CST;
    }

    public void setICMS_CST(String ICMS_CST) {
        this.ICMS_CST = ICMS_CST;
    }

    public Double getICMS_MODBC() {
        return ICMS_MODBC;
    }

    public void setICMS_MODBC(Double ICMS_MODBC) {
        this.ICMS_MODBC = ICMS_MODBC;
    }

    public Double getICMS_ALIQUOTA() {
        return ICMS_ALIQUOTA;
    }

    public void setICMS_ALIQUOTA(Double ICMS_ALIQUOTA) {
        this.ICMS_ALIQUOTA = ICMS_ALIQUOTA;
    }

    public String getIPI_CST() {
        return IPI_CST;
    }

    public void setIPI_CST(String IPI_CST) {
        this.IPI_CST = IPI_CST;
    }

    public Double getIPI_MODBC() {
        return IPI_MODBC;
    }

    public void setIPI_MODBC(Double IPI_MODBC) {
        this.IPI_MODBC = IPI_MODBC;
    }

    public Double getIPI_ALIQUOTA() {
        return IPI_ALIQUOTA;
    }

    public void setIPI_ALIQUOTA(Double IPI_ALIQUOTA) {
        this.IPI_ALIQUOTA = IPI_ALIQUOTA;
    }

    public String getPIS_CST() {
        return PIS_CST;
    }

    public void setPIS_CST(String PIS_CST) {
        this.PIS_CST = PIS_CST;
    }

    public Double getPIS_ALIQUOTA() {
        return PIS_ALIQUOTA;
    }

    public void setPIS_ALIQUOTA(Double PIS_ALIQUOTA) {
        this.PIS_ALIQUOTA = PIS_ALIQUOTA;
    }

    public String getCOFINS_CST() {
        return COFINS_CST;
    }

    public void setCOFINS_CST(String COFINS_CST) {
        this.COFINS_CST = COFINS_CST;
    }

    public Double getCOFINS_ALIQUOTA() {
        return COFINS_ALIQUOTA;
    }

    public void setCOFINS_ALIQUOTA(Double COFINS_ALIQUOTA) {
        this.COFINS_ALIQUOTA = COFINS_ALIQUOTA;
    }

    public Double getPFCP() {
        return PFCP;
    }

    public void setPFCP(Double PFCP) {
        this.PFCP = PFCP;
    }

    public Double getPFCPST() {
        return PFCPST;
    }

    public void setPFCPST(Double PFCPST) {
        this.PFCPST = PFCPST;
    }

    public Double getPFCPSTRET() {
        return PFCPSTRET;
    }

    public void setPFCPSTRET(Double PFCPSTRET) {
        this.PFCPSTRET = PFCPSTRET;
    }

    public String getCBENEF() {
        return CBENEF;
    }

    public void setCBENEF(String CBENEF) {
        this.CBENEF = CBENEF;
    }

    public Double getPREDBC() {
        return PREDBC;
    }

    public void setPREDBC(Double PREDBC) {
        this.PREDBC = PREDBC;
    }

    public Double getMOTDESICMS() {
        return MOTDESICMS;
    }

    public void setMOTDESICMS(Double MOTDESICMS) {
        this.MOTDESICMS = MOTDESICMS;
    }
}
