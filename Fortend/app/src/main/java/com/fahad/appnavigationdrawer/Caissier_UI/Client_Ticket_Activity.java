package com.fahad.appnavigationdrawer.Caissier_UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fahad.appnavigationdrawer.Models.ReponseSupTicket;
import com.fahad.appnavigationdrawer.R;
import com.fahad.appnavigationdrawer.Utils.RequettesReseau;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Client_Ticket_Activity extends AppCompatActivity implements RequettesReseau.CallbacksSortirFil {

   @Bind(R.id.numero_ticket) TextView numeroTicket;
   @Bind(R.id.toolbar_ticket)Toolbar mToolbar;
   @Bind(R.id.btnEnleveClient) Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket__actuel_);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
       // getActionBar().setDisplayHomeAsUpEnabled(true);

        numeroTicket.setText(getIntent().getStringExtra("ticket"));
        final int numero_ticket=Integer.parseInt(numeroTicket.getText().toString());

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequettesReseau.sortirFil(Client_Ticket_Activity.this,1,numero_ticket);
            }
        });


    }

    @Override
    public void sortieEffectue(ReponseSupTicket reponseSupTicket) {

        if (!reponseSupTicket.getError()){
            Toast.makeText(this,"Client supprimer de la fil d'attente",Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            sortieEchouer();


        }
    }

    @Override
    public void sortieEchouer() {

        Toast.makeText(this,"Echec suppression du client dans la fil d'attente",Toast.LENGTH_LONG).show();
        finish();

    }
}
