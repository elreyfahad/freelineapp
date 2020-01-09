package com.fahad.appnavigationdrawer.Caissier_UI;

/**
 * Modeles da la liste des client d'une caisse
 * Created by Admin on 08/05/2018.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Caisse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("clients")
    @Expose
    private List<ClientType> clients = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<ClientType> getClients() {
        return clients;
    }

    public void setClients(List<ClientType> clients) {
        this.clients = clients;
    }

}
