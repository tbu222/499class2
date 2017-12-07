package com.example.vpvu2.androidpizza;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.vpvu2.androidpizza.Database.Database;
import com.example.vpvu2.androidpizza.Model.Drink;
import com.example.vpvu2.androidpizza.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DrinkDetail extends AppCompatActivity {
    TextView drink_name, drink_price, drink_description;
    ImageView drink_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton cartBtn;
    ElegantNumberButton numberButton;

    String drinkId ="";
    FirebaseDatabase database;
    DatabaseReference drink;
    Drink currentDrink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_detail);
        //Firebase
        database = FirebaseDatabase.getInstance();
        drink = database.getReference("Drink");
        //Initialize View
        numberButton = (ElegantNumberButton)findViewById(R.id.num_button);
        cartBtn = (FloatingActionButton)findViewById(R.id.cartBtn);
        //for add to cart button.
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(drinkId,currentDrink.getName(),numberButton.getNumber(),currentDrink.getPrice()));
                Toast.makeText(DrinkDetail.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });
        drink_description = (TextView)findViewById(R.id.drink_description);
        drink_name = (TextView)findViewById(R.id.drink_name);
        drink_price = (TextView)findViewById(R.id.drink_price);
        drink_image = (ImageView)findViewById(R.id.img_drink);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapse);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ABarExpand);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.ABarCollapse);

        //getDrinkId from Intent
        if(getIntent() != null)
            drinkId = getIntent().getStringExtra("DrinkId");
        if(!drinkId.isEmpty())
            getDetailDrink(drinkId);
    }

    private void getDetailDrink(String drinkId) {
        drink.child(drinkId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentDrink = dataSnapshot.getValue(Drink.class);
                //Handling Image
                Picasso.with(getBaseContext()).load(currentDrink.getImage()).into(drink_image);

                collapsingToolbarLayout.setTitle(currentDrink.getName());
                drink_price.setText(currentDrink.getPrice());
                drink_name.setText(currentDrink.getName());
                drink_description.setText(currentDrink.getDescription());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
