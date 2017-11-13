package com.example.vpvu2.androidpizza;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.vpvu2.androidpizza.Interface.ItemClickListener;
import com.example.vpvu2.androidpizza.Model.Salad;
import com.example.vpvu2.androidpizza.ViewHolder.SaladViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SaladList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference saladList;
    String categoryId = "";
    FirebaseRecyclerAdapter<Salad,SaladViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salad_list);
        //firebase
        database = FirebaseDatabase.getInstance();
        saladList = database.getReference("Salad");
        //handling Recycler View
        recyclerView = (RecyclerView)findViewById(R.id.recycler_salad);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //get categoryID from Intent
        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty() && categoryId != null)
        {
            loadListSalad(categoryId);
        }
    }

    private void loadListSalad(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Salad, SaladViewHolder>(Salad.class,
                R.layout.salad_item,
                SaladViewHolder.class,
                saladList.orderByChild("MenuId").equalTo(categoryId)) {//Select Salad based on MenuID
            @Override
            protected void populateViewHolder(SaladViewHolder viewHolder, Salad model, int position) {
                viewHolder.salad_Name.setText(model.getName());
                //loading Image
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.salad_image);

                final Salad local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //get saladDetail and start next activity
                        Intent saladDetail = new Intent(SaladList.this,SaladDetail.class);
                        saladDetail.putExtra("SaladId", adapter.getRef(position).getKey());
                        startActivity(saladDetail);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
}
