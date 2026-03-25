package com.example.demo01.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo01.R;
import com.example.demo01.data.AppDatabase;
import com.example.demo01.data.entity.OrderDetail;
import com.example.demo01.data.entity.Product;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.DetailViewHolder> {

    private List<OrderDetail> detailList;
    private AppDatabase db;

    public OrderDetailAdapter(List<OrderDetail> detailList, AppDatabase db) {
        this.detailList = detailList;
        this.db = db;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        OrderDetail detail = detailList.get(position);
        Product product = db.productDao().getProductById(detail.productId);
        
        if (product != null) {
            holder.tvItemName.setText(product.name);
        }
        holder.tvItemQty.setText("Qty: " + detail.quantity);
        holder.tvItemPrice.setText(String.format("$%.2f", detail.unitPrice * detail.quantity));
    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

    static class DetailViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvItemQty, tvItemPrice;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemQty = itemView.findViewById(R.id.tvItemQty);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
        }
    }
}
