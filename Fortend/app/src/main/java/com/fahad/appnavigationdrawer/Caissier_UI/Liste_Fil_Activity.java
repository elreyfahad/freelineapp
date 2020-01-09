package com.fahad.appnavigationdrawer.Caissier_UI;

import android.graphics.Color;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fahad.appnavigationdrawer.Models.ReponseSupTicket;
import com.fahad.appnavigationdrawer.R;
import com.fahad.appnavigationdrawer.Utils.RequettesReseau;

import java.util.ArrayList;
import java.util.List;

public class Liste_Fil_Activity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
        RequettesReseau.CallbacksListeClientFil,RequettesReseau.CallbacksSortirFil{

    private static final String TAG = Liste_Fil_Activity.class.getSimpleName();
    private RecyclerView recyclerView;
   // private List<Item> cartList;
    private CarteListAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;
    private List<ClientType> mContentItems;
    public int TicketSupprime;


    public BaseTransientBottomBar.BaseCallback<Snackbar> callback;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste__fil_);

        //100 faux contenu
       /**  mContentItems= new ArrayList<>();
        for (int i = 0; i < 100; ++i)
            mContentItems.add(new ClientType());*/

        RequettesReseau.afficherListeClientEnAttente(this,MainActivity_Caissier.NUMERO_CAISSE);

        TicketSupprime=1;


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Clients en attente");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view_liste_fil);
        coordinatorLayout = findViewById(R.id.coordinator_layout);



        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        /**
         * Callbacks qui sera appellé lorsque le snackbar va disaparaitre
         * On en profitera a ce moment la pour envoyer une requete de suppression du client dans la fil d'attente
         */

        callback=new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);

                RequettesReseau.sortirFil(Liste_Fil_Activity.this,MainActivity_Caissier.NUMERO_CAISSE,TicketSupprime);


            }
        };




    }




    /**
     * Méthode qui sera appelée lorsque le glissement est effectué. Ici, l'étape importante de la
     * suppression de l'élément de ligne est effectuée. mAdapter.removeItem () est appelé pour supprimer la ligne de RecyclerView.
     * @param viewHolder
     * @param direction
     * @param position
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof CarteListAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = mContentItems.get(viewHolder.getAdapterPosition()).getNomClient();

            TicketSupprime=mContentItems.get(viewHolder.getAdapterPosition()).getNumTicket();
            Log.d("Numero_ticket","numero ticket est "+TicketSupprime);


            // backup of removed item for undo purpose
            final ClientType deletedItem =   mContentItems.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());


          //  TicketSupprime=mContentItems.get(position).getNumTicket();


            // showing snack bar with Undo option
            final Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name.toUpperCase() + " a etait supprimé de la fil d'attente", Snackbar.LENGTH_LONG);

            snackbar.setAction("Annuler", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);


                    //Lorque on appuye sur le bouton annuller le callbacks de la disparution du snackbar sera desactivé
                    //En effet on annullera la requete de suppresion du client dans la fil d'attente.

                    snackbar.removeCallback(callback);



                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();





            //Appelle fait la requette de suppression du client dans la  file d'attente base de donne
            snackbar.addCallback(callback);
        }

    }

    @Override
    public void clientRecuperer(Caisse caisse) {

        // cartList = new ArrayList<>();
        mContentItems=caisse.getClients();
        mAdapter = new CarteListAdapter(this,caisse.getClients());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);




    }

    @Override
    public void clientNonRecuperer() {

    }


    @Override
    public void sortieEffectue(ReponseSupTicket reponseSupTicket) {

        if (!reponseSupTicket.getError()){

            Toast.makeText(Liste_Fil_Activity.this,"Suppression reuissie",Toast.LENGTH_LONG).show();
        }
        else {
            sortieEchouer();
        }

    }

    @Override
    public void sortieEchouer() {
        Toast.makeText(Liste_Fil_Activity.this,"Echec Suppresssion",Toast.LENGTH_LONG).show();

    }
}
