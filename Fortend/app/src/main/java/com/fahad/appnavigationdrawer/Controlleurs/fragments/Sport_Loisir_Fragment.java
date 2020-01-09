package com.fahad.appnavigationdrawer.Controlleurs.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fahad.appnavigationdrawer.R;
import com.fahad.appnavigationdrawer.Vues.MaterialRecyclerViewAdapter;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Sport_Loisir_Fragment extends Fragment {


    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    public Sport_Loisir_Fragment() {
        // Required empty public constructor
    }

    public static Sport_Loisir_Fragment newInstance(){
        return new Sport_Loisir_Fragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sport__loisir, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recyclerview_categorie_sport_loisir);

        //permet un affichage sous forme liste verticale
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        //faire l'instruiction suivant pour afficher deux item par ligne comme GridLayout

        // mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        mRecyclerView.setHasFixedSize(true);

        //100 faux contenu
        List<Object> mContentItems = new ArrayList<>();
        for (int i = 0; i < 100; ++i)
            mContentItems.add(new Object());




        //penser à passer notre Adapter (ici : MaterialRecyclerViewAdapter) à un RecyclerViewMaterialAdapter
        mAdapter = new MaterialRecyclerViewAdapter(mContentItems);
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mRecyclerView.setAdapter(mAdapter);

        //notifier le MaterialViewPager qu'on va utiliser une RecyclerView
        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView);
    }


}
