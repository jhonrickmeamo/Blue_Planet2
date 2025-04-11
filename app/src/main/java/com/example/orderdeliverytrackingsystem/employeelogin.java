package com.example.orderdeliverytrackingsystem;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class employeelogin extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeelogin);
        mAuth = FirebaseAuth.getInstance();

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginBtn);

        loginButton.setOnClickListener(v -> loginUser());

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

    public void orderManaging(View view) {
        Intent intent = new Intent(employeelogin.this, OrdersView.class);;
        startActivity(intent);

    }
    private void loginUser() {
        String email = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            usernameEditText.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        // Authenticate user with Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(employeelogin.this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(employeelogin.this, OrdersView.class));
                        finish();
                    } else {
                        Toast.makeText(employeelogin.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
