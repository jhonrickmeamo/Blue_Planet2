package com.example.orderdeliverytrackingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class employeeCreateAccount extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, passwordEditText, usernameEditText, emailEditText;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firstNameEditText = findViewById(R.id.FirstName);
        lastNameEditText = findViewById(R.id.LastName);
        passwordEditText = findViewById(R.id.Password);
        usernameEditText = findViewById(R.id.UserName);
        emailEditText = findViewById(R.id.emailAddress);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


    }

    public void employeeList(View view) {

    }


    public void CreateAccount(View view) {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            firstNameEditText.setError("First name is required");
            return;
        }
        if (TextUtils.isEmpty(lastName)) {
            lastNameEditText.setError("Last name is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }
        if (TextUtils.isEmpty(username)) {
            usernameEditText.setError("Username is required");
            return;
        }
        if (TextUtils.isEmpty(email)) { // Fixed validation for email
            emailEditText.setError("Email is required");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user_id = mAuth.getCurrentUser().getUid();
                DocumentReference documentReference = db.collection("Users").document(user_id);
                Map<String, Object> user = new HashMap<>();
                user.put("firstName", firstName);
                user.put("lastName", lastName);
                user.put("username", username);
                user.put("email", email);
                user.put("password", password);
                user.put("role", "Employee");

                documentReference.set(user).addOnSuccessListener(unused -> {
                    Toast.makeText(employeeCreateAccount.this, "User profile created", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after successful creation
                }).addOnFailureListener(e -> {
                    Toast.makeText(employeeCreateAccount.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(employeeCreateAccount.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
