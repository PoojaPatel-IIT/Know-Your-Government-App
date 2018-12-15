package com.example.pooja.knowyourgov;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * Created by pooja on 21,October,2018
 */
public class KYGDetailsVH extends RecyclerView.ViewHolder{
// declaration
    public TextView name;
    public TextView title;
    public TextView party;

    public KYGDetailsVH(View itemView) {
        super(itemView);
        name=(TextView) itemView.findViewById(R.id.title);
        title=(TextView)itemView.findViewById(R.id .name);
        party=(TextView)itemView.findViewById(R.id.party);

    }
}
