package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class ProdutoImposto {
    private int ID  ;
    private int PRODUTO_ID ;
    private String EAN ;
    private String EANTRIB ;
    private String NCM ;
    private String CEST ;
    private String CFOP ;
    private String ICMS_CST ;
    private String ICMS_MODBC ;
    private Double ICMS_ALIQUOTA ;
    private String IPI_CST ;
    private String IPI_MODBC ;
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
    private String MOTDESICMS ;

    public ProdutoImposto(JSONObject j) throws JSONException {
/*
       if (j.isNull("id"))
             ID = j.getInt("ID");
         else ID = j.getInt("id");
*/
       ID            = j.getInt("PRODUTO_ID");
       PRODUTO_ID    = j.getInt("PRODUTO_ID");
       EAN           = j.getString("EAN") ;
       EANTRIB       = j.getString("EANTRIB") ;
       if (j.isNull("NCM"))
           NCM = "00000000" ;
         else  NCM           = j.getString("NCM") ;
       CEST          = j.getString("CEST") ;
       CFOP          = j.getString("CFOP");
       ICMS_CST      = j.getString("ICMS_CST");
       ICMS_MODBC    = j.getString("ICMS_MODBC");
       ICMS_ALIQUOTA = j.getDouble("ICMS_ALIQUOTA");
       IPI_CST       = j.getString("IPI_CST");
       IPI_MODBC     = j.getString("IPI_MODBC");
       IPI_ALIQUOTA  = j.getDouble("IPI_ALIQUOTA");
       PIS_CST       = j.getString("PIS_CST");
       PIS_ALIQUOTA  = j.getDouble("PIS_ALIQUOTA");
       COFINS_CST    = j.getString("COFINS_CST");
       COFINS_ALIQUOTA = j.getDouble("COFINS_ALIQUOTA");
       PFCP          = j.getDouble("PFCP");
       PFCPST        = j.getDouble("PFCPST");
       PFCPSTRET     = j.getDouble("PFCPSTRET");
       CBENEF        = j.getString("CBENEF");
       if (j.isNull("PREDBC"))
           PREDBC = 0.0 ;
         else   PREDBC        = j.getDouble("PREDBC");
       MOTDESICMS    = j.getString("MOTDESICMS");
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPRODUTO_ID() {
        return PRODUTO_ID;
    }

    public void setPRODUTO_ID(int PRODUTO_ID) {
        this.PRODUTO_ID = PRODUTO_ID;
    }

    public String getEAN() {
        return EAN;
    }

    public void setEAN(String EAN) {
        this.EAN = EAN;
    }

    public String getEANTRIB() {
        return EANTRIB;
    }

    public void setEANTRIB(String EANTRIB) {
        this.EANTRIB = EANTRIB;
    }

    public String getNCM() {
        if (NCM == null)
            return "" ;
        else return NCM;
    }

    public void setNCM(String NCM) {
        this.NCM = NCM;
    }

    public String getCEST() {
        if (CEST == null)
            return "" ;
         else return CEST;
    }

    public void setCEST(String CEST) {
        this.CEST = CEST;
    }

    public String getCFOP() {
        if (CFOP == null)
            return "" ;
        else return CFOP;
    }

    public void setCFOP(String CFOP) {
        this.CFOP = CFOP;
    }

    public String getICMS_CST() {
        if (ICMS_CST == null)
            return "" ;
         else return ICMS_CST;
    }

    public void setICMS_CST(String ICMS_CST) {
        this.ICMS_CST = ICMS_CST;
    }

    public String getICMS_MODBC() {
        if (ICMS_MODBC == null)
            return "" ;
           else  return ICMS_MODBC;
    }

    public void setICMS_MODBC(String ICMS_MODBC) {
        this.ICMS_MODBC = ICMS_MODBC;
    }

    public Double getICMS_ALIQUOTA() {
        if (ICMS_ALIQUOTA == null)
            return 0.0 ;
          else  return ICMS_ALIQUOTA;
    }

    public void setICMS_ALIQUOTA(Double ICMS_ALIQUOTA) {
        this.ICMS_ALIQUOTA = ICMS_ALIQUOTA;
    }

    public String getIPI_CST() {
        if (IPI_CST == null)
            return IPI_CST ;
        else return IPI_CST;
    }

    public void setIPI_CST(String IPI_CST) {
        this.IPI_CST = IPI_CST;
    }

    public String getIPI_MODBC() {
        if (IPI_MODBC == null)
            return "" ;
          else return IPI_MODBC;
    }

    public void setIPI_MODBC(String IPI_MODBC) {
        this.IPI_MODBC = IPI_MODBC;
    }

    public Double getIPI_ALIQUOTA() {
        if (IPI_ALIQUOTA == null)
          return 0.0 ;
         else return IPI_ALIQUOTA;
    }

    public void setIPI_ALIQUOTA(Double IPI_ALIQUOTA) {
        this.IPI_ALIQUOTA = IPI_ALIQUOTA;
    }

    public String getPIS_CST() {
        if (PIS_CST == null)
            return "" ;
          else  return PIS_CST;
    }

    public void setPIS_CST(String PIS_CST) {
        this.PIS_CST = PIS_CST;
    }

    public Double getPIS_ALIQUOTA() {
        if (PIS_ALIQUOTA == null)
            return  0.0 ;
        else return PIS_ALIQUOTA;    }

    public void setPIS_ALIQUOTA(Double PIS_ALIQUOTA) {
        this.PIS_ALIQUOTA = PIS_ALIQUOTA;
    }

    public String getCOFINS_CST() {
        if (COFINS_CST == null)
            return  "" ;
         else return COFINS_CST;
    }

    public void setCOFINS_CST(String COFINS_CST) {
        this.COFINS_CST = COFINS_CST;
    }

    public Double getCOFINS_ALIQUOTA() {
        if (COFINS_ALIQUOTA == null)
         return 0.0 ;
         else return COFINS_ALIQUOTA;
    }

    public void setCOFINS_ALIQUOTA(Double COFINS_ALIQUOTA) {
        this.COFINS_ALIQUOTA = COFINS_ALIQUOTA;
    }

    public Double getPFCP() {
        if (PFCP == null)
          return 0.0 ;
        else return PFCP;
    }

    public void setPFCP(Double PFCP) {
        this.PFCP = PFCP;
    }

    public Double getPFCPST() {
        if (PFCPST == null)
          return 0.0 ;
        else return PFCPST;
    }

    public void setPFCPST(Double PFCPST) {
        this.PFCPST = PFCPST;
    }

    public Double getPFCPSTRET() {
        if (PFCPSTRET == null)
             return 0.0 ;
            else return PFCPSTRET;
    }

    public void setPFCPSTRET(Double PFCPSTRET) {
        this.PFCPSTRET = PFCPSTRET;
    }

    public String getCBENEF() {
        if (CBENEF == null)
            return "";
          else return CBENEF;
    }

    public void setCBENEF(String CBENEF) {
        this.CBENEF = CBENEF;
    }

    public Double getPREDBC() {
        if (PREDBC == null)
            return 0.0 ;
         else  return PREDBC;
    }

    public void setPREDBC(Double PREDBC) {
        this.PREDBC = PREDBC;
    }

    public String getMOTDESICMS() {
        if (MOTDESICMS == null)
             return "" ;
         else return MOTDESICMS;
    }

    public void setMOTDESICMS(String MOTDESICMS) {
        this.MOTDESICMS = MOTDESICMS;
    }
}
