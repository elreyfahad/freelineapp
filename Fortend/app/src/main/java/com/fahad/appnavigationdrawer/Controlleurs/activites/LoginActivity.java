package com.fahad.appnavigationdrawer.Controlleurs.activites;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fahad.appnavigationdrawer.Caissier_UI.MainActivity_Caissier;
import com.fahad.appnavigationdrawer.Models.Client;
import com.fahad.appnavigationdrawer.R;
import com.fahad.appnavigationdrawer.Utils.RequettesReseau;
import com.fahad.appnavigationdrawer.Utils.SQLiteHandler;
import com.fahad.appnavigationdrawer.Utils.SessionManager;

import butterknife.ButterKnife;
import butterknife.Bind;

public class LoginActivity extends AppCompatActivity implements RequettesReseau.CallbacksConnexion {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;



    private SessionManager mSessionManager;//Variable qui contient la session
    private SQLiteHandler db;//Variable qui contient le base de donne




    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;

   private ProgressDialog mProgressDialog;



     String email ;
     String password ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //Initialisation du base de donne local
        db = new SQLiteHandler(getApplicationContext());


        //Session Manager
        mSessionManager=new SessionManager(getApplicationContext());

        // Maintient de la session de l'utilisateur  si l'utilisateur s'est deja connecté
        if (mSessionManager.isLoggedIn()) {

            /**On verifie si l'utilisateur dont la session est enregistré est un utilisateur ou caissier pour
             lancer l'activité correspondant*/

           if (mSessionManager.isClient()) {
                //on lance directement le Main Activity sans passe par l'ecran dee connexion a chaque fois
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                this.finish();
            }
            else {
                //lancement de l'activité du Caissier
                Intent intent2=new Intent(LoginActivity.this,MainActivity_Caissier.class);
                startActivity(intent2);
                this.finish();
            }
        }

        //On fait appelle a la fonction login lorsque l'utilisateur clique sur le bouton connexion
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) { //Verifie si les champs sont valide
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);//Active le bouton Connexion

        //lancement du progresse dialogue
        mProgressDialog= new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Authentification...");
        mProgressDialog.show();


        //les variables email et password vont contenir les valuer des champs email et pasword du formulaire
        email = _emailText.getText().toString().trim();
        password = _passwordText.getText().toString().trim();

        //lancement de la requete dans la base de donne pour se conecté connexion
        RequettesReseau.loginUtlisateur(this,email,password);



    }



    //Si l'inscription a reuissie
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                Toast.makeText(this,"Inscription Reussie,connectez vous !! ",Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }



  //Si la connexion a echouer
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Echec Connexion", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }



    //Fonction qui fait la verification des champs
    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("entrer un email valide");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("doit contenir entre 4 et 10 caractere");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


    @Override
    public void connexionReussie(Client client) {

        _loginButton.setEnabled(true);
       if (!client.getError()){ //s'il ny'a pas d'erreur dans la reponse


           //Si le case connexion en tant que Caissier est activer,lancement de Main_caissier
           if (client.getStatus().toString().equals("caissier"))
           {

               // Creation d'une session de connexion
               mSessionManager.setLogin(true);
               mSessionManager.setCaissier(true);
               mSessionManager.setClient(false);
               //Stockage des infos de l'utlisateur renvoyé par la requete dans le base de donne local


               db.addUser(client.getNom(),client.getEmail(),client.getTelephone(),null,null,
                       null,null,null,null,null);




               Log.d("Connexion Caissier","Connexion Caissier");
               mProgressDialog.dismiss();
               Intent intent1=new Intent(LoginActivity.this, MainActivity_Caissier.class);
               startActivity(intent1);
               Log.d("Connexion Caissier","Lancement MainActivity_Caissier");
               this.finish();
           }
           else {

               // Creation d'une session de connexion
               mSessionManager.setLogin(true);
               mSessionManager.setClient(true);
               mSessionManager.setCaissier(false);
               //Stockage des infos de l'utlisateur renvoyé par la requete dans le base de donne local

               db.addUser(client.getNom(),client.getEmail(),client.getTelephone(),client.getRue(),
                       client.getVille(),client.getCarteBancaire(),client.getApiKey(),client.getRang(),
                       client.getUrlProfil(),client.getDepense());

               mProgressDialog.dismiss();
               Intent intent=new Intent(LoginActivity.this,MainActivity.class);
               startActivity(intent);
               this.finish();



           }
       }
       else {
           Toast.makeText(this,"Identifiant Incorrect",Toast.LENGTH_LONG).show();
           mProgressDialog.dismiss();
           _loginButton.setEnabled(true);
       }
    }

    @Override
    public void connexionEchouer() {


        onLoginFailed();
        mProgressDialog.dismiss();
    }
}

