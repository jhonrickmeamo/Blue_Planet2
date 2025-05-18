package com.example.orderdeliverytrackingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class OrdersView extends AppCompatActivity {
    private RecyclerView recyclerView;
    private orderviewlistsadapter adapter;
    private ArrayList<orderviewlists> list;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_orders_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        adapter = new orderviewlistsadapter(this, list);
        recyclerView.setAdapter(adapter);
        loadOrders();
    }
    public void loadOrders(){
        db.collection("Customers").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        list.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            orderviewlists order = new orderviewlists();

                            // Get order details
                            order.setNumber(document.getString("number"));
                            order.setFirstname(document.getString("firstName"));
                            order.setLastname(document.getString("lastName"));
                            order.setAddress(document.getString("address"));
                            order.setStreet(document.getString("street"));
                            order.setBarangay(document.getString("barangay"));
                            order.setCity(document.getString("city"));
                            order.setCustomerId(document.getId());  
                            list.add(order);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    public void orderviewDetails(View view) {
        Intent intent = new Intent(OrdersView.this, orderviewDetails.class);
        startActivity(intent);
    }
}