package com.example.orderdeliverytrackingsystem;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
public class UserRoleManager {

    private static final String TAG = "UserRoleManager";
    private FirebaseFirestore db;
    private Context context;

    public UserRoleManager(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    public void checkUserRole(String userId) {
        db.collection("Users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String role = documentSnapshot.getString("role");
                        if ("admin".equals(role)) {
                            Intent intent = new Intent(context, adminPage.class);
                            context.startActivity(intent);
                        } else if ("employee".equals(role)) {
                            Intent intent = new Intent(context, employeelogin.class);
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "Unknown role", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error fetching user role", Toast.LENGTH_SHORT).show();
                });

    }


}
