package com.example.vpvu2.androidpizza.ViewHolder;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.vpvu2.androidpizza.Interface.ItemClickListener;
import com.example.vpvu2.androidpizza.Model.Order;
import com.example.vpvu2.androidpizza.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by thinh on 10/30/2017.
 */

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView cart_Name, price;
    public ImageView img_cart_count;
    private ItemClickListener itemClickListener;

    public void setCart_Name(TextView cart_Name) {
        this.cart_Name = cart_Name;
    }

    public CartViewHolder(View itemView) {
        super(itemView);
        cart_Name = (TextView)itemView.findViewById(R.id.cart_item_name);
        price = (TextView)itemView.findViewById(R.id.cart_item_price);
        img_cart_count = (ImageView)itemView.findViewById(R.id.cart_item_count);


    }

    @Override
    public void onClick(View view) {

    }
}
public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listdata = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        TextDrawable drawable = TextDrawable.builder().buildRound(""+listdata.get(position).getQuantity(), Color.RED);
        holder.img_cart_count.setImageDrawable(drawable);
        Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int xprice = (Integer.parseInt(listdata.get(position).getPrice()))*(Integer.parseInt(listdata.get(position).getQuantity()));
        holder.price.setText(fmt.format(xprice));
        holder.cart_Name.setText(listdata.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }
}
