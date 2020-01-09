package com.fahad.appnavigationdrawer.Models;

/**
 * Classe qui contient la struicture d'un client
 * Created by Admin on 30/04/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class Client {


    @SerializedName("error")
    @Expose
    private Boolean error;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("nom")
    @Expose
    private String nom;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("telephone")
    @Expose
    private Integer telephone;
    @SerializedName("rue")
    @Expose
    private String rue;
    @SerializedName("ville")
    @Expose
    private String ville;
    @SerializedName("carte_bancaire")
    @Expose
    private Integer carteBancaire;
    @SerializedName("rang")
    @Expose
    private String rang;
    @SerializedName("url_profil")
    @Expose
    private String urlProfil;
    @SerializedName("apiKey")
    @Expose
    private String apiKey;


    @SerializedName("depense")
    @Expose
    private Integer depense;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getTelephone() {
        return telephone;
    }

    public void setTelephone(Integer telephone) {
        this.telephone = telephone;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Integer getCarteBancaire() {
        return carteBancaire;
    }

    public void setCarteBancaire(Integer carteBancaire) {
        this.carteBancaire = carteBancaire;
    }

    public String getRang() {
        return rang;
    }

    public void setRang(String rang) {
        this.rang = rang;
    }

    public String getUrlProfil() {
        return urlProfil;
    }

    public void setUrlProfil(String urlProfil) {
        this.urlProfil = urlProfil;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getStatus(){return status;}
    public void setStatus(String status){this.status=status;}


    public Integer getDepense() {
        return depense;
    }

    public void setDepense(Integer depense) {
        this.depense = depense;
    }

}
