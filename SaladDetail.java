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
import com.example.vpvu2.androidpizza.Model.Order;
import com.example.vpvu2.androidpizza.Model.Salad;
import com.example.vpvu2.androidpizza.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SaladDetail extends AppCompatActivity {

    TextView salad_name, salad_price, salad_description;
    ImageView salad_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton cartBtn;
    ElegantNumberButton numberButton;

    String saladId ="";
    FirebaseDatabase database;
    DatabaseReference salad;
    Salad currentSalad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salad_detail);

        database = FirebaseDatabase.getInstance();
        salad = database.getReference("Salad");

        numberButton = (ElegantNumberButton)findViewById(R.id.num_button);
        cartBtn = (FloatingActionButton)findViewById(R.id.cartBtn);

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(saladId,currentSalad.getName(),numberButton.getNumber(),currentSalad.getPrice()));
                Toast.makeText(SaladDetail.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });
        salad_description = (TextView)findViewById(R.id.salad_description);
        salad_name = (TextView)findViewById(R.id.salad_name);
        salad_price = (TextView)findViewById(R.id.salad_price);
        salad_image = (ImageView)findViewById(R.id.img_salad);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapse);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ABarExpand);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.ABarCollapse);
        //get SaladID from Intent
        if(getIntent() != null)
            saladId = getIntent().getStringExtra("SaladId");
        if(!saladId.isEmpty())
            getDetailSalad(saladId);
    }

    private void getDetailSalad(String saladId) {
        salad.child(saladId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentSalad = dataSnapshot.getValue(Salad.class);
                //Handling Image
                Picasso.with(getBaseContext()).load(currentSalad.getImage()).into(salad_image);

                collapsingToolbarLayout.setTitle(currentSalad.getName());
                salad_price.setText(currentSalad.getPrice());
                salad_name.setText(currentSalad.getName());
                salad_description.setText(currentSalad.getDescription());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
