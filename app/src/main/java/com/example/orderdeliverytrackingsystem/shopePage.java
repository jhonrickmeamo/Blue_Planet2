package com.example.orderdeliverytrackingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class shopePage extends AppCompatActivity {

    private FirebaseFirestore db;

    private RecyclerView prodlistrecycler;

    private productAdapter adapter;
    private List<products> productList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shope_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = FirebaseFirestore.getInstance();
        prodlistrecycler = findViewById(R.id.prodlistrecycler);
        prodlistrecycler.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        adapter = new productAdapter(productList, this);
        prodlistrecycler.setAdapter(adapter);
        fetchProducts();
    }

    private void fetchProducts(){
        db.collection("Product")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        productList.clear();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            products product = doc.toObject(products.class);
                            productList.add(product);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

    }



    public void homePage(View view) {
        Intent intent = new Intent(this, customerDashboard.class);
        startActivity(intent);
    }


    public void summary(View view) {
        // Filter the productList to include only items with itemCount > 0
        ArrayList<products> filteredList = new ArrayList<>();
        for (products product : productList) {
            if (product.getItemCount() > 0) {
                filteredList.add(product);
            }
        }

        // Pass the filtered list to the orderPlace activity
        Intent intent = new Intent(this, orderplace.class);
        intent.putExtra("filteredList", filteredList); // Pass the list as a Parcelable
        startActivity(intent);
}



   
}