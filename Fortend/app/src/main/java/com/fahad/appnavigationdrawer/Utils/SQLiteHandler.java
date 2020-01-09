package com.fahad.appnavigationdrawer.Utils;

/**
 * Cette classe prend soin de stocker les données de l'utilisateur dans la base de données SQLite
 * Chaque fois que nous avons besoin d'obtenir les informations d'utilisateur connecté,
 * nous récupérons SQLite au lieu de faire une demande au serveur
 * Created by Admin on 30/04/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "bd_local";

    // Login table name
    private static final String TABLE_USER = "user";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "nom";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_TELEPHONE = "telephone";
    private static final String KEY_RUE = "rue";
    private static final String KEY_VILLE = "ville";
    private static final String KEY_API_KEY = "api_key";
    private static final String KEY_RANG="rang";
    private static final String KEY_CARTE_BANCAIRE = "carte";
    private static final String KEY_URL_PROFIL = "url_profil";
    private static final String KEY_DEPENSE = "depense";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating de la table User
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_TELEPHONE +" INTEGER,"
                +KEY_RUE +" TEXT,"+KEY_VILLE+" TEXT,"
                +KEY_RANG+" TEXT,"+KEY_CARTE_BANCAIRE+" TEXT,"
                +KEY_DEPENSE+" INTEGER,"+ KEY_API_KEY + " TEXT UNIQUE,"+KEY_URL_PROFIL+" TEXT )";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email,Integer telephone,String rue,String ville,
                        Integer carte,String api_key,String rang,String urlProfil,Integer depense) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_TELEPHONE, telephone); //telephone
        values.put(KEY_RUE, rue); // rue
        values.put(KEY_VILLE,ville);//ville
        values.put(KEY_CARTE_BANCAIRE,carte);//carte_bancaire
        values.put(KEY_API_KEY,api_key);//api key
        values.put(KEY_RANG,rang);//rang
        values.put(KEY_URL_PROFIL,urlProfil);//url_profil
        values.put(KEY_DEPENSE,depense);//depense

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("telephone",cursor.getString(  3));
            user.put("rue",cursor.getString(4));
            user.put("ville",cursor.getString(5));
            user.put("carte",cursor.getString(7));
            user.put("api_key",cursor.getString(9));
            user.put("rang",cursor.getString(6));
            user.put("url_profil",cursor.getString(8));
            user.put("depense",cursor.getString(10));

        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}
