package com.fahad.appnavigationdrawer.Vues;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fahad.appnavigationdrawer.Models.Supermarche;
import com.fahad.appnavigationdrawer.R;

/**
 * Created by Admin on 21/04/2018.
 */

public class ViewHolder_Supermarche extends RecyclerView.ViewHolder {

    private TextView nomSupermarche;
    private TextView addresseSupermarche;

    //itemView est la vue correspondante à 1 cellule
    public ViewHolder_Supermarche(View itemView) {
        super(itemView);

        //c'est ici que l'on fait nos findView
        nomSupermarche =itemView.findViewById(R.id.nom_supermarche);
        addresseSupermarche = itemView.findViewById(R.id.addresse_supermarche);
    }

    //puis ajouter une fonction pour remplir la cellule en fonction d'un MyObject
    public void bind(Supermarche supermarche){
        nomSupermarche.setText(supermarche.getNomSupermarche());
        addresseSupermarche.setText(supermarche.getAddresseSupermarche());
    }
}


