package com.fahad.appnavigationdrawer.Models;

/**
 * Created by Admin on 21/04/2018.
 */

public class Supermarche {

    private String nomSupermarche;
    private String addresseSupermarche;

    public Supermarche(String nomSupermarche, String addresseSupermarche) {
        this.nomSupermarche = nomSupermarche;
        this.addresseSupermarche = addresseSupermarche;
    }

    public String getNomSupermarche() {
        return nomSupermarche;
    }

    public void setNomSupermarche(String nomSupermarche) {
        this.nomSupermarche = nomSupermarche;
    }

    public String getAddresseSupermarche() {
        return addresseSupermarche;
    }

    public void setAddresseSupermarche(String addresseSupermarche) {
        this.addresseSupermarche = addresseSupermarche;
    }
}
