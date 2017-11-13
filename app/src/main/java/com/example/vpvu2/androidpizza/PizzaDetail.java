package com.example.vpvu2.androidpizza;

import android.media.Image;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vpvu2.androidpizza.Database.Database;
import com.example.vpvu2.androidpizza.Model.Order;
import com.squareup.picasso.Picasso;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.vpvu2.androidpizza.Model.Pizza;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PizzaDetail extends AppCompatActivity {
    TextView pizza_name, pizza_price, pizza_description;
    ImageView pizza_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton cartBtn;
    ElegantNumberButton numberButton;

    String pizzaId ="";
    FirebaseDatabase database;
    DatabaseReference pizza;
    Pizza currentPizza;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_detail);

        database = FirebaseDatabase.getInstance();
        pizza = database.getReference("Pizza");

        numberButton = (ElegantNumberButton)findViewById(R.id.num_button);
        cartBtn = (FloatingActionButton)findViewById(R.id.cartBtn);

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(pizzaId,currentPizza.getName(),numberButton.getNumber(),currentPizza.getPrice()));
                Toast.makeText(PizzaDetail.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });

        pizza_description = (TextView)findViewById(R.id.pizza_description);
        pizza_name = (TextView)findViewById(R.id.pizza_name);
        pizza_price = (TextView)findViewById(R.id.pizza_price);
        pizza_image = (ImageView)findViewById(R.id.img_pizza);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapse);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ABarExpand);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.ABarCollapse);
        //get PizzaId from Intent
        if(getIntent() != null)
            pizzaId = getIntent().getStringExtra("PizzaId");
        if(!pizzaId.isEmpty())
            getDetailPizza(pizzaId);
    }

    private void getDetailPizza(String pizzaId) {
        pizza.child(pizzaId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentPizza = dataSnapshot.getValue(Pizza.class);
                //Handling Image
                Picasso.with(getBaseContext()).load(currentPizza.getImage()).into(pizza_image);

                collapsingToolbarLayout.setTitle(currentPizza.getName());
                pizza_price.setText(currentPizza.getPrice());
                pizza_name.setText(currentPizza.getName());
                pizza_description.setText(currentPizza.getDescription());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
