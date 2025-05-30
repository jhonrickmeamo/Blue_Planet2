package com.example.orderdeliverytrackingsystem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ordertrackerAdapter extends RecyclerView.Adapter<ordertrackerAdapter.ViewHolder> {
    private List<ordertracker> ordertrackerList;
    private Context context;
    private FirebaseFirestore db;

    public ordertrackerAdapter(List<ordertracker> ordertrackerList, Context context) {
        this.ordertrackerList = ordertrackerList;
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ordertrackerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.ordertracking, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ordertrackerAdapter.ViewHolder holder, int position) {
        ordertracker order = ordertrackerList.get(position);
        holder.name.setText(order.getFirstname() + " " + order.getLastname());
        holder.number.setText(order.getNumber());
        holder.address.setText(order.getAddress() + ", " +
                order.getStreet() + ", " +
                order.getBarangay() + ", " +
                order.getCity());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        fetchOrderStatus(order.getDocID(), holder.status);
        holder.status.setText(order.getDocID());





    }
    private void fetchOrderStatus(String docID, TextView statusTextView) {
        db.collection("Customers")
                .document(docID)
                .collection("OrderItem")
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        String orderItemId = querySnapshot.getDocuments().get(0).getId();

                        db.collection("Customers")
                                .document(docID)
                                .collection("OrderItem")
                                .document(orderItemId)
                                .collection("status")
                                .document("current")
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        String status = documentSnapshot.getString("status");
                                        if (status != null) {
                                            statusTextView.setText("Status: " + status);
                                        } else {
                                            statusTextView.setText("Status: Pending");
                                        }
                                    } else {
                                        statusTextView.setText("Status: Pending");
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    statusTextView.setText("Status: Error");
                                });
                    }
                });
    }





    @Override
    public int getItemCount() {
        return ordertrackerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, number, address, status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.Tname);
            number = itemView.findViewById(R.id.Tnumber);
            address = itemView.findViewById(R.id.Taddress);
            status = itemView.findViewById(R.id.Tstatus);
        }
    }
}
