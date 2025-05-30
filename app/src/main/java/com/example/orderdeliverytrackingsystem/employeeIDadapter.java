package com.example.orderdeliverytrackingsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class employeeIDadapter extends RecyclerView.Adapter<employeeIDadapter.ViewHolder> {

    private List<employeeID> employeeIDList;
    private Context context;

    private FirebaseFirestore db;
    public employeeIDadapter(List<employeeID> employeeIDList, Context context) {
        this.employeeIDList = employeeIDList;
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.employeelistview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        employeeID employeeID = employeeIDList.get(position);
        holder.name.setText(employeeID.getFirstName());
        holder.email.setText(employeeID.getEmail());
        holder.password.setText(employeeID.getPassword());

        holder.btnDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Employee");
            builder.setMessage("Are you sure you want to delete this employee?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                String email = employeeID.getEmail();

                // Delete from Firestore first
                db.collection("Users")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnSuccessListener(querySnapshot -> {
                            if (!querySnapshot.isEmpty()) {
                                DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                // Delete the document from Firestore
                                document.getReference().delete()
                                        .addOnSuccessListener(aVoid -> {
                                            // Then delete from Authentication

                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(context,
                                                    "Error deleting from Firestore: " + e.getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        });
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(context,
                                    "Error finding employee: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        });
            });
            builder.setNegativeButton("No", null);
            builder.show();
        });

        holder.btnEdit.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.editprofile, null);
            builder.setView(dialogView);

            EditText editName = dialogView.findViewById(R.id.editFname);
            EditText editEmail = dialogView.findViewById(R.id.editEmail);
            EditText editPassword = dialogView.findViewById(R.id.editPassword);

            // Pre-fill current values
            editName.setText(employeeID.getFirstName());
            editEmail.setText(employeeID.getEmail());
            editPassword.setText(employeeID.getPassword());

            builder.setTitle("Edit Employee")
                    .setPositiveButton("Save", (dialog, which) -> {
                        String newName = editName.getText().toString().trim();
                        String newEmail = editEmail.getText().toString().trim();
                        String newPassword = editPassword.getText().toString().trim();

                        if (!newName.isEmpty() && !newEmail.isEmpty() && !newPassword.isEmpty()) {
                            // Update in Firestore
                            db.collection("Users")
                                    .whereEqualTo("email", employeeID.getEmail())
                                    .get()
                                    .addOnSuccessListener(querySnapshot -> {
                                        if (!querySnapshot.isEmpty()) {
                                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                            document.getReference().update(
                                                    "FirstName", newName,
                                                    "email", newEmail,
                                                    "password", newPassword
                                            ).addOnSuccessListener(unused -> {
                                                // Update local data
                                                employeeID.setFirstName(newName);
                                                employeeID.setEmail(newEmail);
                                                employeeID.setPassword(newPassword);
                                                notifyItemChanged(position);
                                                Toast.makeText(context, "Employee updated successfully",
                                                        Toast.LENGTH_SHORT).show();
                                            }).addOnFailureListener(e -> {
                                                Toast.makeText(context, "Update failed: " + e.getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            });
                                        }
                                    });
                        } else {
                            Toast.makeText(context, "All fields are required",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        });


    }
    private void deleteEmployee() {


    }


    @Override
    public int getItemCount() {

        return employeeIDList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, password;
        Button btnDelete, btnEdit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            password = itemView.findViewById(R.id.password);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}
