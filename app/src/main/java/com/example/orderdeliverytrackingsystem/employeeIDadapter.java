package com.example.orderdeliverytrackingsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
                                            FirebaseAuth.getInstance().getCurrentUser().delete()
                                                    .addOnSuccessListener(unused -> {
                                                        employeeIDList.remove(position);
                                                        notifyItemRemoved(position);
                                                        notifyItemRangeChanged(position, employeeIDList.size());
                                                        Toast.makeText(context,
                                                                "Employee deleted successfully",
                                                                Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(context,
                                                                "Error deleting authentication: " + e.getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                                    });
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
    }
    private void deleteEmployee() {


    }


    @Override
    public int getItemCount() {

        return employeeIDList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, password;
        Button btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            password = itemView.findViewById(R.id.password);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
