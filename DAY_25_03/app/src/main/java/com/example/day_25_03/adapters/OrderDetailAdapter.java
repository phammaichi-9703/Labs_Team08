package com.example.day_25_03.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.day_25_03.data.AppDatabase;
import com.example.day_25_03.data.entities.OrderDetail;
import com.example.day_25_03.data.entities.Product;

import java.util.List;
import java.util.Locale;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    private List<OrderDetail> details;
    private AppDatabase db;

    public OrderDetailAdapter(List<OrderDetail> details, AppDatabase db) {
        this.details = details;
        this.db = db;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetail detail = details.get(position);
        Product product = db.shopDao().getProductById(detail.productId);
        
        holder.text1.setText(product != null ? product.name : "Unknown Product");
        holder.text2.setText(String.format(Locale.getDefault(), "Qty: %d - $%.2f each", detail.quantity, detail.unitPrice));
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1, text2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}
