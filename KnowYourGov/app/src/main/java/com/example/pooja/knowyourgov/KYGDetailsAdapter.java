package com.example.pooja.knowyourgov;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by pooja on 21,October,2018
 */
public class KYGDetailsAdapter extends RecyclerView.Adapter<KYGDetailsVH>{

    private ArrayList<KYGDetails> govDelArrayList;
    private MainActivity mainActivity;

    public KYGDetailsAdapter(MainActivity ma, ArrayList<KYGDetails> kyglist) {
        mainActivity = ma;
        govDelArrayList = kyglist;
    }

    @Override
    public KYGDetailsVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kygitemholder, parent, false);
        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);
        return new KYGDetailsVH(itemView);
    }
    @Override
    public int getItemCount() {
        return govDelArrayList.size();
    }

    @Override
    public void onBindViewHolder(KYGDetailsVH holder, int position) {
        KYGDetails gov=govDelArrayList.get(position);
        holder.name.setText(gov.getOfficename());
        //holder.name.setGravity(50);
        holder.title.setText(gov.getOfficial_name());
        holder.party.setText("(" + gov.getOffParty() + ")");

    }



}
