package com.example.day_25_03.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.day_25_03.R;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetail detail = details.get(position);
        Product product = db.shopDao().getProductById(detail.productId);
        
        holder.tvName.setText(product != null ? product.name : "Unknown Product");
        holder.tvQuantity.setText(String.format(Locale.getDefault(), "Quantity: %d", detail.quantity));
        holder.tvPrice.setText(String.format(Locale.getDefault(), "$%.2f", detail.unitPrice * detail.quantity));
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvQuantity = itemView.findViewById(R.id.tvItemQuantity);
            tvPrice = itemView.findViewById(R.id.tvItemPrice);
        }
    }
}
