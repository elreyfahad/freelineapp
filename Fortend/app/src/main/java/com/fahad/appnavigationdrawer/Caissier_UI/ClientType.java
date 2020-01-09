package com.fahad.appnavigationdrawer.Caissier_UI;

/**
 * Type d'information d'un client vu par un caissier lorsqu'il se trouve dans une fil d'attente
 * Created by Admin on 08/05/2018.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientType {

    @SerializedName("num_ticket")
    @Expose
    private Integer numTicket;
    @SerializedName("id_client")
    @Expose
    private Integer idClient;
    @SerializedName("nom_client")
    @Expose
    private String nomClient;
    @SerializedName("url_photo_profil")
    @Expose
    private Object urlPhotoProfil;
    @SerializedName("rang_client")
    @Expose
    private String rangClient;
    @SerializedName("date_emission")
    @Expose
    private String dateEmission;

    public Integer getNumTicket() {
        return numTicket;
    }

    public void setNumTicket(Integer numTicket) {
        this.numTicket = numTicket;
    }

    public Integer getIdClient() {
        return idClient;
    }

    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public Object getUrlPhotoProfil() {
        return urlPhotoProfil;
    }

    public void setUrlPhotoProfil(Object urlPhotoProfil) {
        this.urlPhotoProfil = urlPhotoProfil;
    }

    public String getRangClient() {
        return rangClient;
    }

    public void setRangClient(String rangClient) {
        this.rangClient = rangClient;
    }

    public String getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(String dateEmission) {
        this.dateEmission = dateEmission;
    }

}
