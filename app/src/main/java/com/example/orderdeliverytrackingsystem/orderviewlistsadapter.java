package com.example.orderdeliverytrackingsystem;

import android.content.Context;
import android.content.Intent;
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

    public orderviewlistsadapter(Context context, ArrayList<orderviewlists> list) {
        this.context = context;
        this.list = list;
    }
        @NonNull
        @Override
        public orderviewlistsadapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent,
        int viewType){
            View v = LayoutInflater.from(context).inflate(R.layout.orderviewlist, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder (@NonNull orderviewlistsadapter.ViewHolder holder,int position)
        {
            orderviewlists order = list.get(position);
            holder.number.setText(order.getNumber());
            holder.firstname.setText(order.getFirstname() + " " + order.getLastname());
            holder.address.setText(order.getAddress() + ", " +
                    order.getStreet() + ", " +
                    order.getBarangay() + ", " +
                    order.getCity());

            holder.orderviewbtn.setOnClickListener(v -> {
                Intent intent = new Intent(context, orderviewDetails.class);
                // Pass all order details
                intent.putExtra("firstName", order.getFirstname());
                intent.putExtra("lastName", order.getLastname());
                intent.putExtra("number", order.getNumber());
                intent.putExtra("address", order.getAddress());
                intent.putExtra("street", order.getStreet());
                intent.putExtra("barangay", order.getBarangay());
                intent.putExtra("city", order.getCity());
                intent.putExtra("price", order.getPrice());
                intent.putExtra("product_name", order.getProduct_name());
                intent.putExtra("quantity", order.getQuantity());
                intent.putExtra("total_price", order.getTotal_price());
                context.startActivity(intent);
            });

        }


        @Override
        public int getItemCount () {
            return list.size();
        }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView number, firstname, address;
        Button orderviewbtn;

        public ViewHolder(View v) {
            super(v);
            number = v.findViewById(R.id.number);
            firstname = v.findViewById(R.id.namee);  // matches your layout
            address = v.findViewById(R.id.addresss);
            orderviewbtn = v.findViewById(R.id.orderviewbtn);





                }
        }
    }


