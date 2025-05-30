package com.example.orderdeliverytrackingsystem;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class orderviewDetails extends AppCompatActivity {
    private RecyclerView recyclerView;
    FirebaseFirestore db;
    TextView totalPrice, quantity, productName, orderNumber, textCustomer, textAddress,
            textProduct, textQuantity, textTotal;
    private orderviewlists list;
    String docID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_orderview_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        docID = getIntent().getStringExtra("docID");


        db = FirebaseFirestore.getInstance();
        fetchOrderDetails();
        checkOrderStatus();

        textProduct = findViewById(R.id.textProduct);
        textQuantity = findViewById(R.id.textQuantity);
        textTotal = findViewById(R.id.textTotal);


    }


    private void initViews() {

        quantity = findViewById(R.id.textQuantity);
        productName = findViewById(R.id.textProduct);
        orderNumber = findViewById(R.id.orderNumber);

    }


    private void fetchOrderDetails() {
        db.collection("Customers").document(docID)
                .collection("OrderItem")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        List<Map<String, Object>> orderItems = (List<Map<String, Object>>) document.get("orderItems");
                        double totalAmount = document.getDouble("totalAmount");

                        StringBuilder products = new StringBuilder();
                        StringBuilder quantities = new StringBuilder();

                        if (orderItems != null) {
                            for (Map<String, Object> item : orderItems) {
                                products.append(item.get("product_name")).append("\n");
                                quantities.append(item.get("quantity")).append("\n");
                            }
                        }

                        // Set the text for each TextView
                        textProduct.setText("Products:\n" + products.toString());
                        textQuantity.setText("Quantities:\n" + quantities.toString());
                        textTotal.setText("Total Amount: ₱" + String.format("%.2f", totalAmount));
                    }
                });
    }


    private void checkOrderStatus() {
        Button acceptButton = findViewById(R.id.updateStatus);
        Button completeButton = findViewById(R.id.btnComplete);
        Button deliverButton = findViewById(R.id.btnforDelivery);
        TextView statusText = findViewById(R.id.Status);

        db.collection("Customers")
                .document(docID)
                .collection("OrderItem")
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        String orderItemId = querySnapshot.getDocuments().get(0).getId();

                        db.collection("Customers")
                                .document(docID)
                                .collection("OrderItem")
                                .document(orderItemId)
                                .collection("status")
                                .document("current")
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    String status = "pending"; // default status
                                    if (documentSnapshot.exists() && documentSnapshot.getString("status") != null) {
                                        status = documentSnapshot.getString("status");
                                    }

                                    // Update UI based on status
                                    switch (status) {
                                        case "pending":
                                            statusText.setText("Status: Pending");
                                            acceptButton.setVisibility(View.VISIBLE);
                                            completeButton.setVisibility(View.INVISIBLE);
                                            deliverButton.setVisibility(View.INVISIBLE);
                                            break;
                                        case "in progress":
                                            statusText.setText("Status: In Progress");
                                            acceptButton.setVisibility(View.INVISIBLE);
                                            completeButton.setVisibility(View.INVISIBLE);
                                            deliverButton.setVisibility(View.VISIBLE);
                                            break;
                                        case "for delivery":
                                            statusText.setText("Status: For Delivery");
                                            acceptButton.setVisibility(View.INVISIBLE);
                                            completeButton.setVisibility(View.VISIBLE);
                                            deliverButton.setVisibility(View.INVISIBLE);
                                            break;
                                        case "completed":
                                            statusText.setText("Status: Completed");
                                            acceptButton.setVisibility(View.INVISIBLE);
                                            completeButton.setVisibility(View.INVISIBLE);
                                            deliverButton.setVisibility(View.INVISIBLE);
                                            break;
                                    }
                                });
                    }
                });
    }

    public void updateorderstatus(View view) {
        db.collection("Customers")
                .document(docID)
                .collection("OrderItem")
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        String orderItemId = querySnapshot.getDocuments().get(0).getId();

                        Map<String, Object> statusData = new HashMap<>();
                        statusData.put("status", "in progress");

                        db.collection("Customers")
                                .document(docID)
                                .collection("OrderItem")
                                .document(orderItemId)
                                .collection("status")
                                .document("current")
                                .set(statusData)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Order status updated to in progress", Toast.LENGTH_SHORT).show();
                                    checkOrderStatus(); // Refresh the UI
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show();
                                    Log.e("OrderStatus", "Error updating status: " + e.getMessage());
                                });
                    }
                });
    }

    public void handleCompleteOrder(View view) {
        db.collection("Customers")
                .document(docID)
                .collection("OrderItem")
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        String orderItemId = querySnapshot.getDocuments().get(0).getId();

                        Map<String, Object> statusData = new HashMap<>();
                        statusData.put("status", "completed");

                        db.collection("Customers")
                                .document(docID)
                                .collection("OrderItem")
                                .document(orderItemId)
                                .collection("status")
                                .document("current")
                                .set(statusData)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Order completed", Toast.LENGTH_SHORT).show();
                                    checkOrderStatus(); // Refresh the UI

                                    // Navigate back after a short delay
                                    new Handler().postDelayed(() -> {
                                        Intent intent = new Intent(orderviewDetails.this, OrdersView.class);
                                        startActivity(intent);
                                        finish();
                                    }, 1000);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to complete order", Toast.LENGTH_SHORT).show();
                                    Log.e("OrderStatus", "Error completing order: " + e.getMessage());
                                });
                    }
                });
    }

    public void handleforDelivery(View view) {
        db.collection("Customers")
                .document(docID)
                .collection("OrderItem")
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        String orderItemId = querySnapshot.getDocuments().get(0).getId();

                        Map<String, Object> statusData = new HashMap<>();
                        statusData.put("status", "for delivery");

                        db.collection("Customers")
                                .document(docID)
                                .collection("OrderItem")
                                .document(orderItemId)
                                .collection("status")
                                .document("current")
                                .set(statusData)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Order status updated to for delivery", Toast.LENGTH_SHORT).show();
                                    checkOrderStatus(); // Refresh the UI
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show();
                                    Log.e("OrderStatus", "Error updating status: " + e.getMessage());
                                });
                    }
                });
    }
}



