package com.example.orderdeliverytrackingsystem;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class loginpage extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.white));

        TextView textView = findViewById(R.id.secondTextView);
        TextPaint paint = textView.getPaint();

        float width = paint.measureText(textView.getText().toString());

        Shader textShader = new LinearGradient(
                0, 0, width, textView.getTextSize(),
                new int[]{
                        Color.parseColor("#113C74"),
                        Color.parseColor("#04608D"),
                        Color.parseColor("#0793D9")},
                null, Shader.TileMode.CLAMP
        );
        textView.getPaint().setShader(textShader);
        textView.invalidate();

        CardView cardView = findViewById(R.id.cardView);

        cardView.setCardBackgroundColor(Color.TRANSPARENT);

        cardView.getBackground().setAlpha(200);

        ImageView imageView = findViewById(R.id.image);

        imageView.setRenderEffect(RenderEffect.createBlurEffect(10, 10, Shader.TileMode.CLAMP));

        Button button = findViewById(R.id.btnEmployee);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(this, employeelogin.class);
            startActivity(intent);
        });
        Button button2 = findViewById(R.id.btnCustomer);
        button2.setOnClickListener(view -> {
            Intent intent = new Intent(this, customerDashboard.class);
            startActivity(intent);
        });
    }
}