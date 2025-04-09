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

public class OrdersView extends AppCompatActivity {

    String st;
    TextView statusorder;

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
        statusorder=findViewById(R.id.orderStatus);
    st=getIntent().getStringExtra("orderStatuss");
statusorder.setText(st);
    }

    public void orderviewDetails(View view) {
        Intent intent = new Intent(OrdersView.this, orderviewDetails.class);
        startActivity(intent);
    }
}