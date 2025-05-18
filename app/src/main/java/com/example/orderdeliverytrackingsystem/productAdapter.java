package com.example.orderdeliverytrackingsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class productAdapter extends RecyclerView.Adapter<productAdapter.productViewHolder>{
    List<products> productList;
    private Context context;

    public productAdapter(List<products> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public productAdapter.productViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.productlisting, parent, false);
        return new productViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull productAdapter.productViewHolder holder, int position) {
        products product = productList.get(position);
        holder.prodName.setText(product.getProduct_name());
        holder.prodPackage.setText(product.getProduct_packaging());
        holder.prodPrice.setText(String.valueOf(product.getPackaging_price()));

        holder.itemCount.setText(String.valueOf(product.getItemCount()));


        if (context instanceof shopePage) {
            holder.addItem.setVisibility(View.VISIBLE);
            holder.minusItem.setVisibility(View.VISIBLE);

            holder.addItem.setOnClickListener(v -> {
                int count = product.getItemCount() + 1;
                product.setItemCount(count);
                holder.itemCount.setText(String.valueOf(count));
            });

            holder.minusItem.setOnClickListener(v -> {
                int count = Math.max(0, product.getItemCount() - 1);
                product.setItemCount(count);
                holder.itemCount.setText(String.valueOf(count));
            });
        } else {

            holder.addItem.setVisibility(View.GONE);
            holder.minusItem.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class productViewHolder extends RecyclerView.ViewHolder {
        TextView prodName, prodPackage, prodPrice , itemCount;
        View addItem, minusItem ;
        public productViewHolder(@NonNull View itemView) {
            super(itemView);
            prodName = itemView.findViewById(R.id.prodName);
            prodPackage = itemView.findViewById(R.id.prodPackage);
            prodPrice = itemView.findViewById(R.id.prodPrice);
            itemCount = itemView.findViewById(R.id.itemCount); // Initialize itemCount
            addItem = itemView.findViewById(R.id.addItem); // Initialize addItem
            minusItem = itemView.findViewById(R.id.minusItem); // Initialize minusItem
        }
    }
}
