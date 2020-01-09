package com.fahad.appnavigationdrawer.Controlleurs.activites;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fahad.appnavigationdrawer.Models.ReponseNbClient;
import com.fahad.appnavigationdrawer.Models.Supermarche;
import com.fahad.appnavigationdrawer.R;
import com.fahad.appnavigationdrawer.Utils.RecyclerTouchListener;
import com.fahad.appnavigationdrawer.Utils.RequettesReseau;
import com.fahad.appnavigationdrawer.Vues.SupermarcheAdaptateur;

import java.util.ArrayList;
import java.util.List;

public class ListeSupermarche_Activity extends AppCompatActivity implements RequettesReseau.CallbacksNb_Client_Fil {

    private RecyclerView mRecyclerView;//variable qui va contenir le recycler view
    private List<Supermarche> mSupermarcheList= new ArrayList<>();
    private SearchView mSearchView;
    private Toolbar mToolbar;
    private SupermarcheAdaptateur  mSupermarcheAdaptateur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_supermarche_);

        mRecyclerView = findViewById(R.id.recycler_view);
        mToolbar=findViewById(R.id.mtoolbar);
        mSupermarcheAdaptateur=new SupermarcheAdaptateur(mSupermarcheList);

        setSupportActionBar(mToolbar);//attachement du toolbar dans l'activité
        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//activation du bouton de retour dans le toolbar
        getSupportActionBar().setTitle("Supermarché");

        ajouterSupermarche();



        //définit l'agencement des cellules, ici de façon verticale, comme une ListView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ListeSupermarche_Activity.this));

        //pour adapter en grille comme une RecyclerView, avec 2 cellules par ligne
        //recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        //Animations recycler view
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //Ajout d'une ligne de separation entre les items du Recycler View
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        //puis créer un MyAdapter, lui fournir notre liste de villes.
        //cet adapter servira à remplir notre recyclerview

        mRecyclerView.setAdapter(mSupermarcheAdaptateur);
        //actualisation du recycler_view
        mSupermarcheAdaptateur.notifyDataSetChanged();

        // white background notification bar
        whiteNotificationBar(mSearchView);


        // lorqu'on clique sur un element du RecyclerView
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Supermarche supermarche = mSupermarcheList.get(position);

                //Intent intent=new Intent(ListeSupermarche_Activity.this,Fil_AttenteActivity.class);
                //startActivity(intent);


                //requette pour voir le nombre de client present dans la fil d'attente

                RequettesReseau.nb_clientFil(ListeSupermarche_Activity.this);
            }

            @Override
            public void onLongClick(View view, int position) {


                //Envoie du requete pour recupere le nombre de client dans un supermarché
                RequettesReseau.nb_clientFil(ListeSupermarche_Activity.this);

            }
        }));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        mSearchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mSupermarcheAdaptateur.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mSupermarcheAdaptateur.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!mSearchView.isIconified()) {
            mSearchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }



    private void ajouterSupermarche(){

        mSupermarcheList.add(new Supermarche("Borj","Centre Ville"));
        mSupermarcheList.add(new Supermarche("Carrefour","Medina,Rabat"));
        mSupermarcheList.add(new Supermarche("Borj","Casablanca"));
        mSupermarcheList.add(new Supermarche("Mall","kenitra,la ville haute"));
        mSupermarcheList.add(new Supermarche("Borj","Rabat,kamra"));
        mSupermarcheList.add(new Supermarche("Sawaprix","Moroni,Comores"));
        mSupermarcheList.add(new Supermarche("SaraMarket","Hamraba"));
        mSupermarcheList.add(new Supermarche("MacDonald","Centre Ville"));
        mSupermarcheList.add(new Supermarche("Borj","Atlas,Fes"));
        mSupermarcheList.add(new Supermarche("Galaxy","Lido,Fes"));



    }


    @Override
    public void nb_clientTrouver(ReponseNbClient nbClient) {
        //s'il n'y pas d'erreur dans la reponse
        if(!nbClient.getError()){
            Intent intent=new Intent(ListeSupermarche_Activity.this,Fil_AttenteActivity.class);
            intent.putExtra("nb_client",nbClient.getNbClient());

            startActivity(intent);
        }
        else {
            Toast.makeText(this,"Aucun Supermarché trouver ",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void nb_clientNonTrouver() {


        Toast.makeText(this,"Erreur de connexion",Toast.LENGTH_LONG).show();

    }
}
