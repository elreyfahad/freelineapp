package com.fahad.appnavigationdrawer.Controlleurs.activites;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fahad.appnavigationdrawer.Models.Client;
import com.fahad.appnavigationdrawer.Models.Reponse;
import com.fahad.appnavigationdrawer.R;
import com.fahad.appnavigationdrawer.Utils.RequettesReseau;

import butterknife.ButterKnife;
import butterknife.Bind;

public class SignupActivity extends AppCompatActivity implements RequettesReseau.CallbacksInscription {
    private static final String TAG = "SignupActivity";
    private ProgressDialog progressDialog;

    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_address) EditText _addressText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_mobile) EditText _mobileText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_credit_cards) EditText _creditCards;
    @Bind(R.id.input_ville) EditText _ville;
    @Bind(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

       if (!validate()) {
            onSignupFailed();
            return;
        }


        _signupButton.setEnabled(true);

         progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        String ville=_ville.getText().toString();
        Integer creditCardText=Integer.parseInt(_creditCards.getText().toString());
        Integer telephone=Integer.parseInt(_mobileText.getText().toString());


        //Envoie de la requete d'inscription
       RequettesReseau.inscriptionUtlisateur(this,name,email,telephone,address,ville,creditCardText,password);


    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }


    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        String ville=_ville.getText().toString();
        String creditCardText=_creditCards.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("ne doit pas contenir moins de 3 caractere");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (address.isEmpty()) {
            _addressText.setError("Entrer une adresse valide");
            valid = false;
        } else {
            _addressText.setError(null);
        }
        if (ville.isEmpty()) {
            _addressText.setError("Entrer une Ville");
            valid = false;
        } else {
            _addressText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Entrer un email valide");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10 ) {
            _mobileText.setError("Entrer un numero valide");
            valid = false;
        } else {
            _mobileText.setError(null);
        }
        if (creditCardText.isEmpty() || creditCardText.length()!=8 ) {
            _creditCards.setError("Entrer le numero de Carte de credit");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("doit contre entre 4 et 10 caractere");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

    //Callback appelle lorsque l'inscription a reussi
    @Override
    public void inscriptionReussie(Reponse reponse) {

       if (!reponse.getError()){
           onSignupSuccess();
           progressDialog.dismiss();

       }
       else{
           Toast.makeText(this,reponse.getMessage(),Toast.LENGTH_LONG).show();
           progressDialog.dismiss();

       }

    }

    //Callback lorsque la connexion a echouer
    @Override
    public void inscriptionEchouer() {

        Log.e("incription","inscription echouer");
        onSignupFailed();
        progressDialog.dismiss();
    }
}