package com.fahad.appnavigationdrawer.Models;

/**
 * Classe POJO de le reponse du serveur lorsqu'on fait une requete pour avoir le nombre de client se trouvant dans la fil d'attente
 * Created by Admin on 06/05/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReponseNbClient {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("nb_client")
    @Expose
    private Integer nbClient;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Integer getNbClient() {
        return nbClient;
    }

    public void setNbClient(Integer nbClient) {
        this.nbClient = nbClient;
    }

}