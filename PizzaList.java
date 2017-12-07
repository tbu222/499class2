package com.example.vpvu2.androidpizza;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;

import com.example.vpvu2.androidpizza.Interface.ItemClickListener;
import com.example.vpvu2.androidpizza.Model.Pizza;
import com.example.vpvu2.androidpizza.ViewHolder.PizzaViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class PizzaList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference pizzaList;
    String categoryId = "";
    FirebaseRecyclerAdapter<Pizza,PizzaViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_list);
        //firebase
        database = FirebaseDatabase.getInstance();
        pizzaList = database.getReference("Pizza");

        //handling recycler view
        recyclerView = (RecyclerView)findViewById(R.id.recycler_pizza);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //get CategoryID from Intent
        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty() && categoryId != null)
        {
            loadListPizza(categoryId);
        }
    }

    private void loadListPizza(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Pizza, PizzaViewHolder>(Pizza.class,
                R.layout.pizza_item,
                PizzaViewHolder.class,
                pizzaList.orderByChild("MenuId").equalTo(categoryId)) { //select number of food using MenuId
            @Override
            protected void populateViewHolder(PizzaViewHolder viewHolder, Pizza model, int position) {
                viewHolder.pizza_Name.setText(model.getName());
                //Loading Image
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.pizza_image);

                final Pizza local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //get PizzaDetail and send to next activity.
                        Intent pizzaDetail = new Intent(PizzaList.this,PizzaDetail.class);
                        pizzaDetail.putExtra("PizzaId", adapter.getRef(position).getKey());
                        startActivity(pizzaDetail);

                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
}
