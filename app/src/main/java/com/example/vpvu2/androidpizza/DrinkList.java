package com.example.vpvu2.androidpizza;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.vpvu2.androidpizza.Interface.ItemClickListener;
import com.example.vpvu2.androidpizza.Model.Drink;
import com.example.vpvu2.androidpizza.ViewHolder.DrinkViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class DrinkList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference drinkList;
    String CategoryId= "";
    FirebaseRecyclerAdapter<Drink, DrinkViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_list);

        database = FirebaseDatabase.getInstance();  //get the default firebasedatabase instance
        drinkList = database.getReference("Drink"); //access location of Drink in Firebase for read data

        //handle recycler View
        recyclerView = (RecyclerView)findViewById(R.id.recycler_drink);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //get CategoryId from Intent
        if (getIntent()!= null)
            CategoryId = getIntent().getStringExtra("CategoryId");
        if (!CategoryId.isEmpty()&& CategoryId != null)
        {
            loadListDrink(CategoryId);
        }

    }

    private void loadListDrink(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Drink, DrinkViewHolder>(Drink.class,
                R.layout.drink_item,
                DrinkViewHolder.class,
                drinkList.orderByChild("MenuId").equalTo(CategoryId)) { //select Drink based on MenuId
            @Override
            protected void populateViewHolder(DrinkViewHolder viewHolder, Drink model, int position) {
                viewHolder.drink_Name.setText(model.getName());
                //Image Loading
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.drink_image);

                final Drink local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //get Drink Detail and start new activity.
                        Intent drinkDetail = new Intent(DrinkList.this,DrinkDetail.class);
                        drinkDetail.putExtra("DrinkId", adapter.getRef(position).getKey());
                        startActivity(drinkDetail);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
}
