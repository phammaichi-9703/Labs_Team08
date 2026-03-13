package com.example.demo01.model;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.demo01.R;
import com.example.demo01.adapter.OnItemListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView imgcat1;
    public TextView textcat1;

    public MyViewHolder(@NonNull View itemView, OnItemListener listener) {
        super(itemView);

        imgcat1 = itemView.findViewById(R.id.imgcat1);
        textcat1 = itemView.findViewById(R.id.textcat1);

        itemView.setOnClickListener(v -> {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onClick(position);
                }
            }
        });
    }
}