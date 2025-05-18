package com.example.orderdeliverytrackingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.util.List;
import java.util.Map;

public class orderviewDetails extends AppCompatActivity {


     FirebaseFirestore db;
     TextView orderNumber, customerName, address,  totalPrice, quantity, productName;
    private orderviewlists list;





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

        db = FirebaseFirestore.getInstance();


        orderNumber = findViewById(R.id.orderNumber);
        customerName = findViewById(R.id.textCustomer);
        address = findViewById(R.id.textAddress);
        totalPrice = findViewById(R.id.textPayment);
        quantity = findViewById(R.id.textQuantity);
        productName = findViewById(R.id.textProduct);

        String firstName = getIntent().getStringExtra("firstName");
        String lastName = getIntent().getStringExtra("lastName");
        String number = getIntent().getStringExtra("number");
        String addresss = getIntent().getStringExtra("address");
        String street = getIntent().getStringExtra("street");
        String barangay = getIntent().getStringExtra("barangay");
        String city = getIntent().getStringExtra("city");
        String price = getIntent().getStringExtra("price");
        String product_name = getIntent().getStringExtra("product_name");
        String quantityy = getIntent().getStringExtra("quantity");
        String total_price = getIntent().getStringExtra("total_price");

        orderNumber.setText(number);
        customerName.setText("Customer: " + firstName + " " + lastName);
        address.setText("Address: " + addresss + ", " + street + ", " + barangay + ", " + city);



        fetchOrderDetails();




    }


    private void fetchOrderDetails() {

    }








    public void updateorderstatus(View view) {
        Intent intent = new Intent(orderviewDetails.this, OrdersView.class);

        startActivity(intent);


    }


}

