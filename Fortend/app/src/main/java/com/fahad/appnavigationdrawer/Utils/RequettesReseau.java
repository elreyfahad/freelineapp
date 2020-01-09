package com.fahad.appnavigationdrawer.Utils;

import android.support.annotation.Nullable;
import android.util.Log;

import com.fahad.appnavigationdrawer.Caissier_UI.Caisse;
import com.fahad.appnavigationdrawer.Controlleurs.activites.MainActivity;
import com.fahad.appnavigationdrawer.Models.Client;
import com.fahad.appnavigationdrawer.Models.Reponse;
import com.fahad.appnavigationdrawer.Models.ReponseNbClient;
import com.fahad.appnavigationdrawer.Models.ReponseSupTicket;
import com.fahad.appnavigationdrawer.Models.Ticket;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Classe dedié aux appels reseau de l'application
 * C'est ici qu'on va effectuer tous les appels reseau et decider du resultat au sein de l'application
 * Created by Admin on 20/04/2018.
 */

public class RequettesReseau {

    /**
     * : Une interface de callback a été créée afin que les contrôleurs puissent
     * récupérer certains moments de l'exécution de la requête les( onResponse...() et onFailure...() )
     * Chaque Classe qui veut faire une requetes reseaux doit imperativement implementer cette interface
     */
    public interface CallbacksConnexion{

        void connexionReussie(Client client);
        void connexionEchouer();
    }

    public interface CallbacksInscription{
        void inscriptionReussie(Reponse reponse);
        void inscriptionEchouer();
    }

    public interface CallbacksNb_Client_Fil{
        void nb_clientTrouver(ReponseNbClient nbClient);
        void nb_clientNonTrouver();

    }



    public interface CallbacksTicket{
        void ticketGenerer(Ticket ticket);
        void ticketNonGenerer();
    }

    public interface CallbacksSortirFil{
        void sortieEffectue(ReponseSupTicket reponseSupTicket);
        void sortieEchouer();
    }

    public interface CallbacksListeClientFil{
        void clientRecuperer(Caisse caisse);
        void clientNonRecuperer();
    }



    /**
     * Requete qui permet l'inscription d'un utilisateur dans la base de donné
     */
    public static void inscriptionUtlisateur(CallbacksInscription callbacks,String nom,String email,Integer telephone,String rue,
                                             String ville,Integer carte,String password){

        final WeakReference<CallbacksInscription> callbacksWeakReference=new WeakReference<CallbacksInscription>(callbacks);

        //Creation d'une instance Retrofit deja preconfigurer et creation d'un objet contenant la liste des appels reseaux
        APIService apiService=APIService.retrofit.create(APIService.class);

        Call<Reponse> call=apiService.inscriptionClient(nom,email,telephone,rue,ville,carte,password);

        call.enqueue(new Callback<Reponse>() {
            @Override
            public void onResponse(Call<Reponse> call, Response<Reponse> response) {


                if (callbacksWeakReference.get() != null){

                    //si la connexion est reussie on l'activité parent declenchera la fonction inscriptionReussie
                    callbacksWeakReference.get().inscriptionReussie(response.body());
                }

            }

            @Override
            public void onFailure(Call<Reponse> call, Throwable t) {

                if (callbacksWeakReference.get() != null){

                    //l'activité parent declenchera la fonction insciprtionEchouer
                    callbacksWeakReference.get().inscriptionEchouer();
                }

            }
        });



    }

    /**
     * Requete envoyé lors de la connexion de l'utilisateur
     */
    public static void loginUtlisateur(CallbacksConnexion callbacks,String email,String password){

        final WeakReference<CallbacksConnexion> callbacksWeakReference=new WeakReference<CallbacksConnexion>(callbacks);

        //Creation d'une instance Retrofit deja preconfigurer et creation d'un objet contenant la liste des appels reseaux
        APIService apiService=APIService.retrofit.create(APIService.class);

        Call<Client> call=apiService.loginClient(email,password);

        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {

                if (response.isSuccessful()){
                    Log.d("connexion","connexion");
                }
                if (callbacksWeakReference.get() != null){

                    Log.d("Reussie","Connexion Reussie");
                    callbacksWeakReference.get().connexionReussie(response.body());
                }

            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {

               Log.e("Erreur","Echec connexion",t);
                if (callbacksWeakReference.get() != null){

                    callbacksWeakReference.get().connexionEchouer();
                }


            }
        });


    }





    /**
     * Requete envoyé pour recupere le nombre de client dans la fil d'attente
     */


    public static void nb_clientFil(CallbacksNb_Client_Fil callbacks) {


        final WeakReference<CallbacksNb_Client_Fil> callbacksWeakReference = new WeakReference<CallbacksNb_Client_Fil>(callbacks);

        //Creation d'une instance Retrofit deja preconfigurer et creation d'un objet contenant la liste des appels reseaux
        APIService apiService = APIService.retrofit.create(APIService.class);

        Call<ReponseNbClient> call=apiService.getNb_Client_Fil();


        call.enqueue(new Callback<ReponseNbClient>() {
            @Override
            public void onResponse(Call<ReponseNbClient> call, Response<ReponseNbClient> response) {

                if (callbacksWeakReference.get() != null){

                    Log.d("Reussie","Nb_client trouver");
                    callbacksWeakReference.get().nb_clientTrouver(response.body());
                }
            }

            @Override
            public void onFailure(Call<ReponseNbClient> call, Throwable t) {


                Log.e("Erreur","Echec connexion",t);
                if (callbacksWeakReference.get() != null){

                    callbacksWeakReference.get().nb_clientNonTrouver();
                }

            }
        });


    }



    /**
     * Requete pour la generation d'un ticket
     */

    public static void generationTicket(CallbacksTicket callbacks){

        final WeakReference<CallbacksTicket> callbacksWeakReference = new WeakReference<CallbacksTicket>(callbacks);

        //Creation d'une instance Retrofit deja preconfigurer et creation d'un objet contenant la liste des appels reseaux
        APIService apiService = APIService.retrofit.create(APIService.class);

        Call<Ticket> call=apiService.getTicket(MainActivity.API_KEY);
        Log.d("key_api",MainActivity.API_KEY);
        call.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                if (response.isSuccessful()){
                    Log.d("Ticket","Ticket Generer");

                }

                if (callbacksWeakReference.get() != null){

                    Log.d("Reussie","Nb_client trouver");
                    callbacksWeakReference.get().ticketGenerer(response.body());
                }
            }

            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {

                if (callbacksWeakReference.get() != null){

                    Log.e("Reussie","Nb_client trouver",t);

                    callbacksWeakReference.get().ticketNonGenerer();
                }

            }
        });

    }


    /**
     * Requete pour la generation d'un ticket
     */

    public static void sortirFil(CallbacksSortirFil callbacks,int num_caisse,int num_ticket){

        final WeakReference<CallbacksSortirFil> callbacksWeakReference = new WeakReference<CallbacksSortirFil>(callbacks);

        //Creation d'une instance Retrofit deja preconfigurer et creation d'un objet contenant la liste des appels reseaux
        APIService apiService = APIService.retrofit.create(APIService.class);

        Call<ReponseSupTicket> call=apiService.deleteTicket(num_caisse,num_ticket);

        call.enqueue(new Callback<ReponseSupTicket>() {
            @Override
            public void onResponse(Call<ReponseSupTicket> call, Response<ReponseSupTicket> response) {

                if (callbacksWeakReference.get() != null){

                    Log.d("Reussie","Nb_client trouver");
                    callbacksWeakReference.get().sortieEffectue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ReponseSupTicket> call, Throwable t) {

                if (callbacksWeakReference.get() != null){

                    Log.e("SortieFil","Sortie Echouer",t);
                    callbacksWeakReference.get().sortieEchouer();
                }

            }
        });



    }

    /**
     * Requette pour afficher le nombre de client dans une caisse
     */
    public static void afficherListeClientEnAttente(CallbacksListeClientFil callbacks,int num_caisse){

        final WeakReference<CallbacksListeClientFil> callbacksWeakReference = new WeakReference<CallbacksListeClientFil>(callbacks);

        //Creation d'une instance Retrofit deja preconfigurer et creation d'un objet contenant la liste des appels reseaux
        APIService apiService = APIService.retrofit.create(APIService.class);

        Call<Caisse> call=apiService.liste_client(num_caisse);

        call.enqueue(new Callback<Caisse>() {
            @Override
            public void onResponse(Call<Caisse> call, Response<Caisse> response) {
                if (callbacksWeakReference.get() != null){

                    Log.d("Reussie","Liste de client recupere");
                    callbacksWeakReference.get().clientRecuperer(response.body());
                }
            }

            @Override
            public void onFailure(Call<Caisse> call, Throwable t) {
                if (callbacksWeakReference.get() != null){

                    Log.d("Echec","Echec recuperation liste client",t);
                    callbacksWeakReference.get().clientNonRecuperer();
                }

            }
        });

    }



}
