package com.fahad.appnavigationdrawer.Utils;

/**
 * Cette classe conserve les données de session dans l'application à l'aide des SharedPreferences.
 * Nous stockons un indicateur booléen isLoggedIn dans les préférences partagées pour vérifier l'état de connexion
 * Created by Admin on 30/04/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "AppLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_IS_CLIENT = "isClient";
    private static final String KEY_IS_CAISSIER = "isCaissier";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){

        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }


    public void setClient(boolean isClient){

        editor.putBoolean(KEY_IS_CLIENT,isClient);
        editor.commit();

        Log.d(TAG, "User login session modified!");

    }

    public boolean isClient(){

        return pref.getBoolean(KEY_IS_CLIENT,false);
    }



    public void setCaissier(boolean isCaissier){
        editor.putBoolean(KEY_IS_CAISSIER,isCaissier);
        editor.commit();

        Log.d(TAG, "User login session modified!");

    }

    public boolean isCaissier(){

        return pref.getBoolean(KEY_IS_CAISSIER,false);
    }
}