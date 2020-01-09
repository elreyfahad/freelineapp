package com.fahad.appnavigationdrawer.Utils;

import com.fahad.appnavigationdrawer.Caissier_UI.Caisse;
import com.fahad.appnavigationdrawer.Models.Client;
import com.fahad.appnavigationdrawer.Models.Reponse;
import com.fahad.appnavigationdrawer.Models.ReponseNbClient;
import com.fahad.appnavigationdrawer.Models.ReponseSupTicket;
import com.fahad.appnavigationdrawer.Models.Ticket;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Interface qui liste les differents requettes qui seront effectuer dans l'API
 *
 * Cette interface contiendra aussi une variable publique statique qui va nous permettre de creer
 * une instance configuré de retrofitqui sera utiliser par la suite pour effectuer les requettes reseaux.
 *
 * @GET(....) : indique a retrofit qu'on veut faire une requete de type GET dans l'url donne en parametre
 * @PATH(....) :indique une variable de type string representant le parametre que placera dans l'url de la methode GET
 *
 *
 *
 * Created by Admin on 20/04/2018.
 */

public interface APIService {



    /**
     * Objet retrofit qui sera utiliser pour les requetes
     */
    public final static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.7/pfe/v1/") //DIFINITION D'UNE URL DE BASE DE L'API POUR RETROFIT
            .addConverterFactory(GsonConverterFactory.create())  //DIFINITION DU SERIALISEUR/DESERIALISEUR UTILISER PAR DEFAUT PAR RETROFIT ICI C'EST "GSON"
            .build();



    /**
     * Requete pour faire la requete de l'inscription d'un utilisateur
     */
    @FormUrlEncoded
    @POST("register")
    Call<Reponse> inscriptionClient(@Field("nom_client") String nom, @Field("email_client") String email,
                                    @Field("telephone_client") Integer telephone, @Field("rue") String rue,
                                    @Field("ville") String ville, @Field("carte_bancaire_client") Integer carte, @Field("password")String password);



    /**
     * Requete qui permet la connexion d'un client existant envoyant son email et son mot de passe
     * Cette fonction retourne une variable de Type client qui va contenir tous les infos du client
     * @return
     */
    @FormUrlEncoded
    @POST("login")
    Call<Client> loginClient(@Field("email") String email,@Field("password") String password);



    /**
     * REQUETE QUI RETOURNE UN UTILISATEUR EN INDIQUANT SON NOM DANS L'URL
     */

    @GET("clients/{nom}")
    Call<Client> getClient(@Path("nom") String nom);


    //REQUETE QUI RETOURNE LES UTILISATEURS PRESENTS DANS UNE CAISSE
    @GET("{caisse}/clients")
    Call<List<Client>> getClientFilAttente(@Path("caisse") int caisse);



    //REQUETE QUI RETOURNE LE NOMBRE DE Client present dans une fil d'attente d'une supermarché

    @GET("nb_client_fil")
    Call<ReponseNbClient> getNb_Client_Fil();


    //REQUETE POUR DEMANDER UN TICKET

    @GET("ticket/Carrefour")
    Call<Ticket> getTicket(@Header("Authorization") String api_key);



    //REQUETE POUR SUPPRIMER UN TICKET DANS LA FIL D'ATTENTE
    @DELETE("delete_ticket/{num_caisse}/{num_ticket}")
    Call<ReponseSupTicket> deleteTicket(@Path("num_caisse") int num_caisse, @Path("num_ticket") int num_ticket);

    //REQUETE POUR RECUPERER LA LISTE DES CLIENTS QUI SE TROUVE DANS LA CAISSE
    @GET("caisse/{id_caisse}")
    Call<Caisse> liste_client(@Path("id_caisse") int id_caisse);

}
