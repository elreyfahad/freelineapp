package com.fahad.appnavigationdrawer.Caissier_UI;

/**
 * Created by Admin on 08/05/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fahad.appnavigationdrawer.R;

import java.util.List;

/**
 * ette classe d'adaptateur sera utilisée pour gonfler les dispositions avec
 * les données appropriées dans la vue recycleur. J'ajoute également deux méthodes supplémentaires
 * à cette classe removeItem () et restoreItem () , pour supprimer / ajouter des lignes à la vue recycleur.
 */

public class CarteListAdapter extends RecyclerView.Adapter<CarteListAdapter.MyViewHolder> {
    private Context context;
    private List<ClientType> cartList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, price;
        public ImageView thumbnail;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            price = view.findViewById(R.id.price);
            thumbnail = view.findViewById(R.id.thumbnail);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public CarteListAdapter(Context context, List<ClientType> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carte_item_fil, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ClientType item = cartList.get(position);
        holder.name.setText(item.getNomClient());
        holder.description.setText("Status Client:"+item.getRangClient().toUpperCase()+"\n"+"Date: "+item.getDateEmission());
        holder.price.setText("Ticket Numero: " + item.getNumTicket());

        Glide.with(context)
                .load(item.getUrlPhotoProfil())
                .apply(RequestOptions.circleCropTransform())
                .apply(new RequestOptions().error(R.drawable.ic_user).placeholder(R.drawable.ic_user))
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void removeItem(int position) {
        cartList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(ClientType item, int position) {
        cartList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
