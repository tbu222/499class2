package com.example.vpvu2.androidpizza.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vpvu2.androidpizza.Interface.ItemClickListener;
import com.example.vpvu2.androidpizza.R;

/**
 * Created by thinh on 10/9/2017.
 */

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView menuName;
    public ImageView imageView;
    private ItemClickListener itemClickListener;


    public MenuViewHolder(View itemView){
        super(itemView);

        menuName= (TextView)itemView.findViewById(R.id.menu_name);
        imageView=(ImageView)itemView.findViewById(R.id.menu_image);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View view){
    itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
