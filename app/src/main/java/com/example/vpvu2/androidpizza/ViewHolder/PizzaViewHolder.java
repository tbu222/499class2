package com.example.vpvu2.androidpizza.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vpvu2.androidpizza.Interface.ItemClickListener;
import com.example.vpvu2.androidpizza.R;

/**
 * Created by thinh on 10/19/2017.
 */

public class PizzaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView pizza_Name;
    public ImageView pizza_image;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public PizzaViewHolder(View itemView) {
        super(itemView);
        pizza_Name= (TextView)itemView.findViewById(R.id.pizza_name);
        pizza_image=(ImageView)itemView.findViewById(R.id.pizza_image);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
