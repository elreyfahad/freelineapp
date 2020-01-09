package com.fahad.appnavigationdrawer.Caissier_UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fahad.appnavigationdrawer.Controlleurs.activites.LoginActivity;
import com.fahad.appnavigationdrawer.Controlleurs.activites.MainActivity;
import com.fahad.appnavigationdrawer.R;
import com.fahad.appnavigationdrawer.Utils.SQLiteHandler;
import com.fahad.appnavigationdrawer.Utils.SessionManager;

import java.util.HashMap;

public class MainActivity_Caissier extends AppCompatActivity {

    private Toolbar mToolbar;
    private CardView scanTicket;
    private CardView filAttente;
    private CardView enregistreProduit;
    private TextView num_caisse;
    public static int NUMERO_CAISSE;


    private SessionManager mSessionManager;//Variable qui contient la session
    private SQLiteHandler db;//Variable qui contient le base de donne

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__caissier);

        mToolbar=findViewById(R.id.toolbar_caisse);
        scanTicket=findViewById(R.id.card_view_scanner_ticket);
        filAttente=findViewById(R.id.card_view_voir_fil_attente);
        enregistreProduit=findViewById(R.id.card_view_enregistre_produit);
        num_caisse=findViewById(R.id.Caisse_num_caisse);


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext()); //Initialisation de la base de donne local

        // Verifiaction de la session
        mSessionManager = new SessionManager(getApplicationContext());

        if (!mSessionManager.isLoggedIn()) {
            finish();
        }

        HashMap<String,String> infoUser=db.getUserDetails();
        num_caisse.setText(infoUser.get("telephone"));
        NUMERO_CAISSE=Integer.parseInt(infoUser.get("telephone").toString());



        setSupportActionBar(mToolbar);


        //Lancement du scanner Qr Code du ticket pour confirmer le numero du Client
        scanTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity_Caissier.this,Scan_Activity.class);

                startActivity(intent);
            }
        });

        //Voir la liste des personne qui se trouve dans la fil d'attente
        filAttente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity_Caissier.this,Liste_Fil_Activity.class);
                startActivity(intent);
            }
        });

        //Enregistrer les produit acheter par le client
        enregistreProduit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });





    }

    /**
     * Fonction qui permet la deconnexion de l'utilisateur ,il efface aussi ses donne dans base donné local
     * */
    private void logoutUser() {
        mSessionManager.setLogin(false);
        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity_Caissier.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_caissier,menu);

        return true;
    }


    //Lorsqu'on clique sur deconnexion de Caissier

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.deconnexion_cassier:
                Toast.makeText(MainActivity_Caissier.this,"Fermeture de la caisse...",Toast.LENGTH_LONG).show();
                logoutUser();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
