package com.example.orderdeliverytrackingsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

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

public class orderTrack extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ordertrackerAdapter adapter;

    private ArrayList<ordertracker> orderList;
    private ArrayList<ordertracker> filteredList;
    private FirebaseFirestore db;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_track);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView2);
        searchView = findViewById(R.id.searchView);

        recyclerView.setVisibility(View.INVISIBLE);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        orderList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new ordertrackerAdapter(filteredList, this);
        recyclerView.setAdapter(adapter);

        setupSearchView();
        loadOrders();



    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterOrders(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterOrders(newText);
                return true;
            }
        });

        // Clear button listener
        searchView.setOnCloseListener(() -> {
            recyclerView.setVisibility(View.INVISIBLE);
            filteredList.clear();
            adapter.notifyDataSetChanged();
            return false;
        });
    }

    private void filterOrders(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            for (ordertracker order : orderList) {
                // Search by order number or customer name
                if (order.getFirstname().toLowerCase().contains(query.toLowerCase()) ||
                        order.getLastname().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(order);
                }
            }

            recyclerView.setVisibility(filteredList.isEmpty() ? View.INVISIBLE : View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }
    private void loadOrders() {
        db.collection("Customers").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        orderList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ordertracker tracker = new ordertracker();

                            // Get order details
                            tracker.setNumber(document.getString("number"));
                            tracker.setFirstname(document.getString("firstName"));
                            tracker.setLastname(document.getString("lastName"));
                            tracker.setAddress(document.getString("address"));
                            tracker.setStreet(document.getString("street"));
                            tracker.setBarangay(document.getString("barangay"));
                            tracker.setCity(document.getString("city"));
                            tracker.setDocID(document.getId());


                            orderList.add(tracker);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}