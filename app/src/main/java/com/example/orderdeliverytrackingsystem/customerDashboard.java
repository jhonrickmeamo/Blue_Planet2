package com.example.orderdeliverytrackingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class customerDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.rand));

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating(0.0f);
    }

    public void shop(View view) {
        // Code to handle shop button click
        // This could involve navigating to a different activity or updating the UI
        Intent intent = new Intent(this, shopePage.class);
        startActivity(intent);
    }

    public void ordertrackactivity(View view) {
        // Code to handle order tracking button click
        // This could involve navigating to a different activity or updating the UI
        Intent intent = new Intent(this, orderTrack.class);
        startActivity(intent);
    }
}