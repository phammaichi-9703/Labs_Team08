package com.example.demo01.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo01.R;
import com.example.demo01.model.Cat;
import com.example.demo01.model.MyViewHolder;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<Cat> nList;
    private OnItemListener onItemListener;

    public MyAdapter(List<Cat> nList) {
        this.nList = nList;
    }

    public void setOnItemListener(OnItemListener listener) {
        this.onItemListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new MyViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cat cat = nList.get(position);
        holder.imgcat1.setImageResource(cat.getImageResId());
        holder.textcat1.setText(cat.getName());
    }

    @Override
    public int getItemCount() {
        return nList.size();
    }
}