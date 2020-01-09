package com.fahad.appnavigationdrawer.Controlleurs.activites;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fahad.appnavigationdrawer.R;
import com.fahad.appnavigationdrawer.Utils.SQLiteHandler;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.internal.connection.StreamAllocation;

public class ProfilActivity extends AppCompatActivity {



    //Declaration d'une variable de type Toolbar qui va contenir le toolbar de l'activité
     @Bind(R.id.toolbar) Toolbar mToolbar; //initialisation du variable mToolbar avec le toolbar cree a part (layout/toolbar.xml)
     @Bind(R.id.photo_de_profil) ImageView photoProfil;//initilaisation du photo de profil

    @Bind(R.id.modifier_utilisateur) FloatingActionButton fab;//initialisation du bouton
    @Bind(R.id.adresse_utilisateur) TextView adresse;
    @Bind(R.id.email_utilisateur) TextView email;
    @Bind(R.id.telephone_utilisateur) TextView telephone;
    @Bind(R.id.num_carte_bancaire_utilisateur) TextView carte_cb;
    @Bind(R.id.utilisateur_rang)  TextView rang_utilisateur;


    public static final String urlImageProfil=" ";
    private SQLiteHandler db;//Variable qui contient le base de donne




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        ButterKnife.bind(this);


        setSupportActionBar(mToolbar);//Attachement du toolbar dans l'en tete de l'activités

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext()); //Initialisation de la base de donne local

        HashMap<String,String> infoUser=db.getUserDetails();

        telephone.setText("+212 0"+infoUser.get("telephone"));
        email.setText(infoUser.get("email"));

        adresse.setText(infoUser.get("rue")+", "+infoUser.get("ville"));
        rang_utilisateur.setText(infoUser.get("rang"));
        carte_cb.setText(infoUser.get("carte"));








        //Lorsque l'utlisateur cliquera sur le bouton flottant l'activité Modification Profil se declenchera
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lancement de l'activité Modification du profil
                Intent intent=new Intent(ProfilActivity.this,Modifcation_profil.class);
                startActivity(intent);

            }
        });


        /**
         * on va declarer une variable de type ActionBar,et l'initialisé par "getSupportActionBar()" pour que nous puission la modifier
         * et l'affecté un bouton de retour grace a la methode "setDisplayHomeAsUpEnable()"
         */
       ActionBar actionBar=getSupportActionBar() ;
       actionBar.setDisplayHomeAsUpEnabled(true);
       actionBar.setTitle(infoUser.get("name"));


        // Loading profile image
        Glide.with(this).load(urlImageProfil)
                .apply(RequestOptions.circleCropTransform())
                .apply(new RequestOptions().error(R.drawable.ic_user).placeholder(R.drawable.ic_user))
                .into(photoProfil);


    }



}
