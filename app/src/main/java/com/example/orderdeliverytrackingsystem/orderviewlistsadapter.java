package com.example.orderdeliverytrackingsystem;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class orderviewlistsadapter extends RecyclerView.Adapter<orderviewlistsadapter.ViewHolder> {
    private Context context;
    private ArrayList<orderviewlists> list;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public orderviewlistsadapter(Context context, ArrayList<orderviewlists> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public orderviewlistsadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.orderviewlist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull orderviewlistsadapter.ViewHolder holder, int position) {
        orderviewlists order = list.get(position);
        holder.number.setText(order.getNumber());
        holder.firstname.setText(order.getFirstname() + " " + order.getLastname());
        holder.address.setText(order.getAddress() + ", " +
                order.getStreet() + ", " +
                order.getBarangay() + ", " +
                order.getCity());
        holder.docID.setText(order.getDocID());

        fetchOrderStatus(order.getDocID(), holder.orderStatus);


        holder.orderviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if order is not null and has a valid docID
                if (order != null && order.getDocID() != null) {
                    // Log the docID for debugging
                    Log.d("Adapter", "DocID being passed: " + order.getDocID());
                    Intent intent = new Intent(context, orderviewDetails.class);
                    intent.putExtra("docID", order.getDocID());
                    intent.putExtra("number", order.getNumber());
                    intent.putExtra("firstname", order.getFirstname());
                    intent.putExtra("lastname", order.getLastname());
                    intent.putExtra("address", order.getAddress());
                    intent.putExtra("street", order.getStreet());
                    intent.putExtra("barangay", order.getBarangay());
                    intent.putExtra("city", order.getCity());
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Order details are not available.", Toast.LENGTH_SHORT).show();
                }
            }
        });



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
                                    Log.e("Status", "Error fetching status", e);
                                    statusTextView.setText("Status: Error");
                                });
                    }
                });
    }
    public interface OnItemClickListener {
        void onItemClick(orderviewlists order);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView number, firstname, address, docID, orderStatus;
        Button orderviewbtn;

        public ViewHolder(View v) {
            super(v);
            number = v.findViewById(R.id.number);
            firstname = v.findViewById(R.id.namee);  // matches your layout
            address = v.findViewById(R.id.addresss);
            orderviewbtn = v.findViewById(R.id.orderviewbtn);
            // Initialize docID TextView
            orderStatus = v.findViewById(R.id.orderStatus);

            docID = v.findViewById(R.id.docIDD);


        }
    }
}


