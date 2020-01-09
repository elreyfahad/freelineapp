package com.fahad.appnavigationdrawer.Vues;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.fahad.appnavigationdrawer.Models.Supermarche;
import com.fahad.appnavigationdrawer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 21/04/2018.
 */

public class SupermarcheAdaptateur extends RecyclerView.Adapter<ViewHolder_Supermarche> implements Filterable {

    private List<Supermarche> mSupermarcheList;//liste complet des supermarché
    private List<Supermarche> supermarchetListFiltered;//liste filtré des supermarché

    //Contruicteur prenant en entre une liste
    public SupermarcheAdaptateur(List<Supermarche> supermarcheList) {
        mSupermarcheList = supermarcheList;
        supermarchetListFiltered=supermarcheList;
    }

    //cette fonction permet de créer les viewHolder et par la même indiquer la vue à inflater (à partir des layout xml)
    @Override
    public ViewHolder_Supermarche onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.supermarche_view_item_row,parent,false);

        return new ViewHolder_Supermarche(view);
    }

    //c'est ici que nous allons remplir notre cellule avec le texte de chaque Objects Supermarche
    @Override
    public void onBindViewHolder(ViewHolder_Supermarche holder, int position) {

        Supermarche supermarche=mSupermarcheList.get(position);
        holder.bind(supermarche);

    }

    /**
     * Retourne la taille de la liste a afficher
     * Cette taille va varier en fonction du recherche effectuer dans la barre de recherche
     * C'est pour cela on l'affecte a la taille du Liste Filtré des supermarché
     * @return
     */
    @Override
    public int getItemCount() {
        return supermarchetListFiltered.size();
    }


    /**
     * Dans la méthode getFilter () , la chaîne de recherche est transmise à la méthode performFiltering () .
     * La recherche d'un supermarche par nom ou addresse est effectuée à l'aide de la chaîne de requête
     * @return
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                /**
                 * Si la barre de recherche est vide on affiche liste complet des supermarché.
                 * Sinon on affiche la liste des supermarché correspondant au recherhce
                 */

                if (charString.isEmpty()) {
                    supermarchetListFiltered = mSupermarcheList;
                } else {
                    List<Supermarche> filteredList = new ArrayList<>();
                    for (Supermarche row : mSupermarcheList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getNomSupermarche().toLowerCase().contains(charString.toLowerCase()) || row.getAddresseSupermarche().toLowerCase().contains(charString.toLowerCase())) {

                            filteredList.add(row);
                        }
                    }

                    supermarchetListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = supermarchetListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                supermarchetListFiltered = (ArrayList<Supermarche>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
