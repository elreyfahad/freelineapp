package com.fahad.appnavigationdrawer.Controlleurs.activites;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.fahad.appnavigationdrawer.Models.Ticket;
import com.fahad.appnavigationdrawer.R;
import com.fahad.appnavigationdrawer.Utils.RequettesReseau;

public class Fil_AttenteActivity extends AppCompatActivity implements RequettesReseau.CallbacksTicket {

    private ImageView icone;
    private Button mButton1;//bouton pour rejoindre la fil d'attente en mode normal
    private Button mButton2;//bouton rejoindre la fil d'atttente en mode priorité
    private TextView mTextView;//text view qui contient le nombre de personne qui se trouve dans la fil d'attente
    private android.support.v7.widget.Toolbar mToolbar;


    public static final String MINUTES_ATTENTE="minutes_attente";//Chaine de caractere utiliser pour passer le nombre de minutes d'attente dans un extrat
    public  static final String URL_QR_CODE="url_qrcode";
    public  static final String NUM_CAISSE="num_caisse";
    public  static final String NUM_TICKET="num_ticket";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fil__attente);

        mButton1=findViewById(R.id.btn_rejoindre_la_fil);
        mButton2=findViewById(R.id.btn_passe_priorite);
        mTextView=findViewById(R.id.nb_personne);
        mToolbar=findViewById(R.id.toolbar_fil);


        //Affichage du nombre de personne dans la fil d'attente
        int nb=getIntent().getIntExtra("nb_client",0);


        mTextView.setText(Integer.toString(nb));


        icone=findViewById(R.id.icone_groupe);
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//activation du bouton retour dans le menu toolbar
        actionBar.setTitle("Fil d'attente");



        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Envoie de la requete pour generer un ticket

                RequettesReseau.generationTicket(Fil_AttenteActivity.this);

            }
        });
    }

    //Recuperation des resultat de la requete

    @Override
    public void ticketGenerer(Ticket ticket) {

        if (!ticket.getError())
        {
            Intent intent=new Intent(Fil_AttenteActivity.this,TicketActivity.class);

            //on met le temps d'attente du client dans l'extrat pour la recuperer dans l'activité TicketActivity par la suite
            intent.putExtra(MINUTES_ATTENTE,ticket.getTempsAttente());
            intent.putExtra(URL_QR_CODE,ticket.getUrlQrcode());
            intent.putExtra(NUM_CAISSE,ticket.getNumCaisse());
            intent.putExtra(NUM_TICKET,ticket.getNumTicket());

            startActivity(intent);//lancement de l'activité Ticket Activity
        }
        else {
            Toast.makeText(this,"Vous n'etes pas encore sortie de la fil d'atttente",Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    public void ticketNonGenerer() {

        Toast.makeText(this,"Erreur de connexion",Toast.LENGTH_LONG).show();
        finish();

    }
}
