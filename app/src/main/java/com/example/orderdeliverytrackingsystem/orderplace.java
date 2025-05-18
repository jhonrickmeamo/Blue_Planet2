package com.example.orderdeliverytrackingsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class orderplace extends AppCompatActivity {
    private TextView totalPrice;
    private ArrayList<products> filteredList;
    private productAdapter adapter;
    private FirebaseFirestore database;
    private DatabaseReference customersRef;

    private EditText fnameEditText, lnameEditText, numberEditText, streetEditText, barangayEditText, cityEditText, addressEditText;
    private TextView addressTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_orderplace);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        database = FirebaseFirestore.getInstance();


        fnameEditText = findViewById(R.id.cusFname);
        lnameEditText = findViewById(R.id.cusLname);
        numberEditText = findViewById(R.id.cusNumber);
        addressTextView = findViewById(R.id.cusAddress);


        totalPrice = findViewById(R.id.totalPrice);

        filteredList = getIntent().getParcelableArrayListExtra("filteredList");
        if (filteredList == null) {
            filteredList = new ArrayList<>();
        }
        ArrayList<products> filteredList = getIntent().getParcelableArrayListExtra("filteredList");


        RecyclerView recyclerView = findViewById(R.id.placeOrderList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter adapter = new productAdapter(filteredList, this);
        recyclerView.setAdapter(adapter);

        calculateTotalPrice();

    }

    private void calculateTotalPrice() {
        double total = 0;

        // Calculate total from filtered list
        for (products product : filteredList) {
            double price = product.getPackaging_price();
            int quantity = product.getItemCount();
            total += price * quantity;
    }
        String formattedPrice = String.format("₱%.2f", total);
        totalPrice.setText(formattedPrice);
    }


    public void orderPlaced(View view) {

        // Get input values
        String fname = fnameEditText.getText().toString().trim();
        String lname = lnameEditText.getText().toString().trim();
        String number = numberEditText.getText().toString().trim();
        String fullAddress = addressTextView.getText().toString().trim();

        // Validate inputs
        if (fname.isEmpty()) {
            fnameEditText.setError("First name is required");
            return;
        }
        if (lname.isEmpty()) {
            lnameEditText.setError("Last name is required");
            return;
        }
        if (number.isEmpty()) {
            numberEditText.setError("Contact number is required");
            return;
        }
        if (fullAddress.isEmpty()) {
            addressTextView.setError("Address is required");
            return;
        }
             // Split address
            String[] addressParts = fullAddress.split(", ");
            if (addressParts.length != 4) {
                Toast.makeText(this, "Invalid address format", Toast.LENGTH_SHORT).show();
                return;

            }

            // Create customer object
            Map<String, Object> customer = new HashMap<>();
            customer.put("firstName", fname);
            customer.put("lastName", lname);
            customer.put("number", number);
            customer.put("address", addressParts[0]);
            customer.put("street", addressParts[1]);
            customer.put("barangay", addressParts[2]);
            customer.put("city", addressParts[3]);

            // Save to Firestore
            database.collection("Customers")
                    .add(customer)
                    .addOnSuccessListener(documentReference -> {
                        String customerId = documentReference.getId();
                        // Update document with its ID
                        documentReference.update("customer_id", customerId);
                        // Create order items list
                        List<Map<String, Object>> orderItems = new ArrayList<>();
                        double totalAmount = 0;

                        for (products product : filteredList) {
                            Map<String, Object> item = new HashMap<>();
                            item.put("product_name", product.getProduct_name());
                            item.put("quantity", product.getItemCount());
                            item.put("price", product.getPackaging_price());
                            double itemTotal = product.getPackaging_price() * product.getItemCount();
                            item.put("total", itemTotal);
                            orderItems.add(item);
                            totalAmount += itemTotal;
                        }

                        // Create order document with minimal info
                        Map<String, Object> order = new HashMap<>();

                        order.put("orderItems", orderItems);
                        order.put("totalAmount", totalAmount);



                        // Save order to Firestore
                        database.collection("Customers").document(customerId)
                                .collection("OrderItem")
                                .add(order)
                                .addOnSuccessListener(orderRef -> {
                                    String orderId = orderRef.getId();
                                    orderRef.update("order_id", orderId)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(orderplace.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                                                clearFields();
                                                filteredList.clear();
                                                adapter.notifyDataSetChanged();
                                                calculateTotalPrice();
                                                finish();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(orderplace.this, "Error saving order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(orderplace.this, "Error saving customer: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }

        private void clearFields () {
            fnameEditText.setText("");
            lnameEditText.setText("");
            numberEditText.setText("");
            addressTextView.setText("");
        }




    public void inputAddress(View view) {
        TextView addressTextView = findViewById(R.id.cusAddress);

        // Inflate the custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.addressinput, null);

        // Find EditText fields in the custom layout
        EditText addressField = dialogView.findViewById(R.id.address);
        EditText streetField = dialogView.findViewById(R.id.street);
        EditText barangayField = dialogView.findViewById(R.id.barangay);
        EditText cityField = dialogView.findViewById(R.id.city);

        // Create an AlertDialog
        new AlertDialog.Builder(this)
                .setTitle("Enter your Address")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Get the entered values
                    String address = addressField.getText().toString().trim();
                    String street = streetField.getText().toString().trim();
                    String barangay = barangayField.getText().toString().trim();
                    String city = cityField.getText().toString().trim();

                    // Validate and update the TextView
                    if (!address.isEmpty() && !street.isEmpty() && !barangay.isEmpty() && !city.isEmpty()) {
                        String fullAddress = address + ", " + street + ", " + barangay + ", " + city;
                        addressTextView.setText(fullAddress);
                    } else {
                        Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

}
