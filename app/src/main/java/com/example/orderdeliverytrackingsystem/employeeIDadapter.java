package com.example.orderdeliverytrackingsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class employeeIDadapter extends RecyclerView.Adapter<employeeIDadapter.ViewHolder> {

    private List<employeeID> employeeIDList;
    private Context context;
    public employeeIDadapter(List<employeeID> employeeIDList, Context context) {
        this.employeeIDList = employeeIDList;
        this.context = context;
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
    }

    @Override
    public int getItemCount() {
        return employeeIDList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, password;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            password = itemView.findViewById(R.id.password);
        }
    }
}
