package com.example.demo01;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo01.adapter.OrderDetailAdapter;
import com.example.demo01.data.AppDatabase;
import com.example.demo01.data.entity.Order;
import com.example.demo01.data.entity.OrderDetail;
import com.example.demo01.utils.PreferenceManager;

import java.util.List;

public class OrderActivity extends AppCompatActivity {

    RecyclerView rvOrderDetails;
    TextView tvTotalAmount;
    Button btnCheckout;
    AppDatabase db;
    PreferenceManager prefManager;
    Order currentOrder;
    List<OrderDetail> detailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        db = AppDatabase.getInstance(this);
        prefManager = new PreferenceManager(this);

        rvOrderDetails = findViewById(R.id.rvOrderDetails);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnCheckout = findViewById(R.id.btnCheckout);

        loadCart();

        btnCheckout.setOnClickListener(v -> {
            if (currentOrder != null && detailList != null && !detailList.isEmpty()) {
                currentOrder.status = "Paid";
                db.orderDao().update(currentOrder);
                Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_LONG).show();
                finish(); // Close cart, in real app show Invoice
            } else {
                Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCart() {
        int userId = prefManager.getUserId();
        currentOrder = db.orderDao().getPendingOrderByUser(userId);

        if (currentOrder != null) {
            detailList = db.orderDetailDao().getOrderDetailsByOrder(currentOrder.id);
            double total = 0;
            for (OrderDetail detail : detailList) {
                total += detail.unitPrice * detail.quantity;
            }
            currentOrder.totalAmount = total;
            db.orderDao().update(currentOrder);
            
            tvTotalAmount.setText(String.format("$%.2f", total));
            rvOrderDetails.setLayoutManager(new LinearLayoutManager(this));
            rvOrderDetails.setAdapter(new OrderDetailAdapter(detailList, db));
        } else {
            tvTotalAmount.setText("$0.00");
        }
    }
}
