package com.fahad.appnavigationdrawer.Models;

/**
 * Classe Ticket
 * Created by Admin on 06/05/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ticket {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("num_ticket")
    @Expose
    private Integer numTicket;
    @SerializedName("num_caisse")
    @Expose
    private Integer numCaisse;
    @SerializedName("url_qrcode")
    @Expose
    private String urlQrcode;
    @SerializedName("id_supermarche")
    @Expose
    private Integer idSupermarche;
    @SerializedName("temps_attente")
    @Expose
    private Integer tempsAttente;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Integer getNumTicket() {
        return numTicket;
    }

    public void setNumTicket(Integer numTicket) {
        this.numTicket = numTicket;
    }

    public Integer getNumCaisse() {
        return numCaisse;
    }

    public void setNumCaisse(Integer numCaisse) {
        this.numCaisse = numCaisse;
    }

    public String getUrlQrcode() {
        return urlQrcode;
    }

    public void setUrlQrcode(String urlQrcode) {
        this.urlQrcode = urlQrcode;
    }

    public Integer getIdSupermarche() {
        return idSupermarche;
    }

    public void setIdSupermarche(Integer idSupermarche) {
        this.idSupermarche = idSupermarche;
    }

    public Integer getTempsAttente() {
        return tempsAttente;
    }

    public void setTempsAttente(Integer tempsAttente) {
        this.tempsAttente = tempsAttente;
    }

}

