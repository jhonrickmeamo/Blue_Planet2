package com.example.orderdeliverytrackingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

public class adminPage extends AppCompatActivity {

    private RecyclerView employeerclrv;
    private employeeIDadapter adapter;
    private List<employeeID> employeeIDList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        employeerclrv = findViewById(R.id.employeerclrv);
        employeerclrv.setLayoutManager(new LinearLayoutManager(this));

        employeeIDList = new ArrayList<>();
        adapter = new employeeIDadapter(employeeIDList, this);
        employeerclrv.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        // Fetch data from Firestore
        fetchemployeedata();

    }

    private void fetchemployeedata() {
        db.collection("Users")
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) {
                        System.out.println("Firestore Listener Error: " + (error != null ? error.getMessage() : "Value is null"));
                        return;
                    }
                    System.out.println("Firestore Listener Triggered");
                    employeeIDList.clear();
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        employeeID employeeIDs = snapshot.toObject(employeeID.class);
                        if (employeeIDs != null && !"admin@bp.com".equalsIgnoreCase(employeeIDs.getEmail())) {
                            employeeIDList.add(employeeIDs);
                        }
                    }
                    System.out.println("List size after update: " + employeeIDList.size());
                    runOnUiThread(() -> adapter.notifyDataSetChanged());
                });
    }

    public void employeeCreateAccount(View view) {
        Intent intent = new Intent(this, employeeCreateAccount.class);
        startActivity(intent);
    }
}