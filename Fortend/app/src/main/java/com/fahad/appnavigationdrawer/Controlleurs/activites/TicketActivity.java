package com.fahad.appnavigationdrawer.Controlleurs.activites;

import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fahad.appnavigationdrawer.Models.ReponseSupTicket;
import com.fahad.appnavigationdrawer.Models.Ticket;
import com.fahad.appnavigationdrawer.R;
import com.fahad.appnavigationdrawer.Utils.RequettesReseau;
import com.fahad.appnavigationdrawer.Vues.Ticket_Views;

import java.util.Locale;

/**
 * Activités qui geres la vue d'un ticket
 * Lorsque l'utilisateur va avoir un ticket et c'est dans cette classe qu'on va gerer l'affichage et le controlle
 */

public class TicketActivity extends AppCompatActivity implements RequettesReseau.CallbacksSortirFil{
    private TextView numeroCaisse;//attribut qui affichera le numero de guichet
    private TextView tempsAttente;//attributs qui affichera le temps d'attente
    private Ticket_Views ticketView;
    private ImageView imageQrCode;

    private Toolbar mToolbar;


    private String urlGlide;


    private static long START_TIME_IN_MILLIS ;//temps en millisceonde du compte a rebours (10minutes)
    private CountDownTimer mCountDownTimer;//Compte a rebours
    private boolean mTimerRunning;//booleen pour savoir si le compte a rebours est en marche
    private long mTimeLeftInMillis;//Les nombre de millisecondes restants avant l'arret du compte a rebours
    private long mEndTime;//le temps d'arrets du compte a rebours


    private int num_caisse;
    private int num_ticket;
    private String urlCode;





    private MaterialDialog boiteDialogue;//boite de dialogue qui permet l'affichage des minutes a prolonger
    private static int KEY_MINUTES;//clé qui indiquera quels sont les minutes choisie pour prolonger

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);



        tempsAttente = findViewById(R.id.temps_attente);
        numeroCaisse = findViewById(R.id.numero_caisse);
        ticketView = findViewById(R.id.layout_ticket);
        mToolbar=findViewById(R.id.toolbar_ticket_activity);
        imageQrCode=findViewById(R.id.imageQrCode);





        setSupportActionBar(mToolbar);

        num_caisse=getIntent().getIntExtra(Fil_AttenteActivity.NUM_CAISSE,0);
        num_ticket=getIntent().getIntExtra(Fil_AttenteActivity.NUM_TICKET,0);
        urlCode=getIntent().getStringExtra(Fil_AttenteActivity.URL_QR_CODE);

        urlGlide="http://192.168.1.7/pfe/".concat(urlCode);


        //Affichage du QrCode depuis le serveur
        Glide.with(this).load(urlGlide)//application d'une image par defaut
                .into(imageQrCode);





        //initialisation du nombre de minutes en attente en milliseconde,envoyer depuis l'activité fil d'attente via l'extrat de l'intent
        START_TIME_IN_MILLIS=getIntent().getLongExtra(Fil_AttenteActivity.MINUTES_ATTENTE,1)*60000;
        numeroCaisse.setText(Integer.toString(getIntent().getIntExtra(Fil_AttenteActivity.NUM_CAISSE,1)));


        this.startTimer();//demarrage du compte a rebours au lancement de l'activité
    }

    //Creation du menu du toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_ticket_activity,menu);

        return true;
    }

    //les actions a faire lorqu'on click sur un item du menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.annuler_ticket:
                //Requette reseau pour sortir le client dans la fil d'attente
                RequettesReseau.sortirFil(TicketActivity.this,num_caisse,num_ticket);

                return true;
            case R.id.prolonger_attente: //Lorque l'utilisateur clicke sur l'item Prolonger attente

                boiteDialogue=new MaterialDialog.Builder(TicketActivity.this)
                        .title("Combien de minutes voules vous pronlonger ? ")//Titre du boite de dialogue
                        .items(R.array.minutes) //implementation du tableau des villes dans la listes
                        .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {

                                /**
                                 * On prends le nombre de minutes selectionner dans la boite de dialogue et
                                 * on enregistre dans une variable KEY_MINUTES pour plutard
                                 */

                                KEY_MINUTES=Integer.parseInt(text.toString());
                                return true;
                            }
                        })
                        .positiveText(R.string.valider)
                        .negativeText(R.string.Annuler)
                        .onPositive(new MaterialDialog.SingleButtonCallback() { //Quand on appuye sur le bouton "Valider"
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                START_TIME_IN_MILLIS=KEY_MINUTES*60000;
                                pauseTimer();
                               resetTimer();
                                startTimer();//puis  on demarre le compte a rebours avec les nouvelles minutes indiquer

                            }
                        })
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    /**
     * Fonction qui demarre le compte a rebours
     */
    private void startTimer() {

        mTimeLeftInMillis=START_TIME_IN_MILLIS;
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                //updateButtons();
            }
        }.start();

        mTimerRunning = true;
        // updateButtons();
    }

    /**
     * Fonctions pour mettre en pause le compte a rebours
     */
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        //updateButtons();
    }

    /**
     * Fonction de reinitialisation du compte a rebours
     */
    private void resetTimer() {
        mTimeLeftInMillis =0;
        updateCountDownText();
        mTimerRunning=false;
        //updateButtons();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * Fonctions qui met a jours le Compte a rebours
     */
    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60; //division du temps restant  en milliseconde pour avoir les minutes restants
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;//division du temps restant en milliseconde pour avoir les secondes restants

        //Difinition d'un format du textView qui va afficher le temps d'attente
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        tempsAttente.setText(timeLeftFormatted);//Modification du TextView sous le format definie
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        //updateButtons();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                // updateButtons();
            } else {
                startTimer();
            }
        }
    }

    //Resultat du requette lorsque l'utilisateur decide de sortir de la fil d'attente


    @Override
    public void sortieEffectue(ReponseSupTicket reponseSupTicket) {
        if(!reponseSupTicket.getError()){

            Toast.makeText(TicketActivity.this,"Vous etes sortie de la fil d'attente",Toast.LENGTH_LONG).show();
            finish();
            pauseTimer();
            resetTimer();
        }
        else {
            sortieEchouer();
        }
    }

    @Override
    public void sortieEchouer() {

        Toast.makeText(this,"Echec lors du sortie de la fil d'attente,Reesseyer !!",Toast.LENGTH_LONG).show();

    }
}
