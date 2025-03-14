package com.example.orderdeliverytrackingsystem;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class employeelogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeelogin);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.rand));

        TextView textView = findViewById(R.id.txtViewThird);
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

        ImageButton backBttn = findViewById(R.id.backBtn);
        backBttn.setOnClickListener(v -> finish());
    }
}