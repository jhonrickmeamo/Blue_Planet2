package com.example.orderdeliverytrackingsystem;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.white));

        TextView textView = findViewById(R.id.mytextView);
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

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, loginpage.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}