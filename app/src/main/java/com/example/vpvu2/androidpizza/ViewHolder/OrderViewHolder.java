package com.example.vpvu2.androidpizza.ViewHolder;

import android.content.ClipData;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.vpvu2.androidpizza.Interface.ItemClickListener;
import com.example.vpvu2.androidpizza.R;

import org.w3c.dom.Text;


/**
 * Created by thinh on 10/31/2017.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView orderID,orderStatus, orderPhone, orderAddress;
    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);
        orderAddress = (TextView)itemView.findViewById(R.id.order_address);
        orderPhone = (TextView)itemView.findViewById(R.id.order_phone);
        orderStatus = (TextView)itemView.findViewById(R.id.order_status);
        orderID = (TextView)itemView.findViewById(R.id.order_id);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
