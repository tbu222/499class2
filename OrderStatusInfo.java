package com.example.vpvu2.androidpizza;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.vpvu2.androidpizza.Common.Common;
import com.example.vpvu2.androidpizza.Model.Request;
import com.example.vpvu2.androidpizza.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatusInfo extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference requests;
    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status_info);
        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
        //init view
        recyclerView = (RecyclerView)findViewById(R.id.orderList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadOrders(Common.currentUser.getPhone_num());
    }
    //load the orders
    private void loadOrders(String phone_num) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(Request.class, R.layout.order_layout,OrderViewHolder.class,requests.orderByChild("phone").equalTo(phone_num)) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                viewHolder.orderID.setText(adapter.getRef(position).getKey());
                viewHolder.orderStatus.setText(codeToStatus(model.getStatus_info()));
                viewHolder.orderAddress.setText(model.getAddress());
                viewHolder.orderPhone.setText(model.getPhone());
            }
        };
        recyclerView.setAdapter(adapter);
    }

    //convert code to status
    private String codeToStatus(String status_info) {
        if (status_info.equals("0"))
            return "Placed";
        else if (status_info.equals("1"))
            return "On the way";
        else
            return "Shipped";
    }

}
