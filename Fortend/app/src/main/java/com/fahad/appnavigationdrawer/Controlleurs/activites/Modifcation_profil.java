package com.fahad.appnavigationdrawer.Controlleurs.activites;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fahad.appnavigationdrawer.R;

public class Modifcation_profil extends AppCompatActivity {

    //Declaration d'une variable de type Toolbar qui va contenir le toolbar de l'activité
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifcation_profil);

        mToolbar=findViewById(R.id.toolbar_modification_profil);//initialisation du variable mToolbar avec le toolbar cree a part (layout/toolbar.xml)

        setSupportActionBar(mToolbar);//Attachement du toolbar dans l'en tete de l'activités

        /**
         * on va declarer une variable de type ActionBar,et l'initialisé par "getSupportActionBar()" pour que nous puission la modifier
         * et l'affecté un bouton de retour grace a la methode "setDisplayHomeAsUpEnable()"
         */
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("Modification profil");//Modification du titre
    }

    /**
     * Creation du menu du toolbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_actionbar_modifcation_profil,menu);
        return true;
    }

    /**
     * Evenement de la touche d'un item dans le menu du toolbar
     * On va valider la
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            /**Lorsque l'utilisateur va cliquer sur valider les modifications
             * on l'application va envoyer une requete au base de donne pour mettre a jours
             * les informations de l'utlisateurs
             */
            case R.id.valide_modification_profil:
                Toast.makeText(this,"Modification enregistré !! ",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
}
