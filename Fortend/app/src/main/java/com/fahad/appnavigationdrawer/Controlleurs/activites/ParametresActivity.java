package com.fahad.appnavigationdrawer.Controlleurs.activites;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fahad.appnavigationdrawer.R;

public class ParametresActivity extends AppCompatActivity {


    //Declaration d'une variable de type Toolbar qui va contenir le toolbar de l'activité
    private android.support.v7.widget.Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        mToolbar=findViewById(R.id.toolbar);//initialisation du variable mToolbar avec le toolbar cree a part (layout/toolbar.xml)

        setSupportActionBar(mToolbar);//Attachement du toolbar dans l'en tete de l'activités

        /**
         * on va declarer une variable de type ActionBar,et l'initialisé par "getSupportActionBar()" pour que nous puission la modifier
         * et l'affecté un bouton de retour grace a la methode "setDisplayHomeAsUpEnable()"
         */
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Parametre");//Modification du titre

        /**
         * Il faut aussi modifier le manifest pour indique le parent de cette activité comme ça lorsqu'on appuyera
         * sur le bouton d=retour ,android va nous ramener directement vers l'activité parent
         * Avec l'attribut "parentActivityName" de cette activité
         */
    }
}
