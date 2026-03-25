package com.example.day_25_03;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.day_25_03.adapters.OrderDetailAdapter;
import com.example.day_25_03.data.AppDatabase;
import com.example.day_25_03.data.entities.Order;
import com.example.day_25_03.data.entities.OrderDetail;
import com.example.day_25_03.utils.UserSession;

import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {
    private RecyclerView rvCartItems;
    private TextView tvTotalAmount;
    private Button btnPay;
    private AppDatabase db;
    private UserSession session;
    private Order pendingOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        db = AppDatabase.getInstance(this);
        session = new UserSession(this);

        rvCartItems = findViewById(R.id.rvCartItems);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnPay = findViewById(R.id.btnPay);

        rvCartItems.setLayoutManager(new LinearLayoutManager(this));

        loadCart();

        btnPay.setOnClickListener(v -> {
            if (pendingOrder != null) {
                pendingOrder.status = "Paid";
                db.shopDao().updateOrder(pendingOrder);
                Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show();
                
                Intent intent = new Intent(this, InvoiceActivity.class);
                intent.putExtra("ORDER_ID", pendingOrder.id);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadCart() {
        int userId = session.getUserId();
        pendingOrder = db.shopDao().getPendingOrderByUser(userId);

        if (pendingOrder != null) {
            List<OrderDetail> details = db.shopDao().getOrderDetailsByOrder(pendingOrder.id);
            if (details.isEmpty()) {
                tvTotalAmount.setText("Your cart is empty");
                btnPay.setEnabled(false);
            } else {
                OrderDetailAdapter adapter = new OrderDetailAdapter(details, db);
                rvCartItems.setAdapter(adapter);
                tvTotalAmount.setText(String.format(Locale.getDefault(), "Total: $%.2f", pendingOrder.totalAmount));
                btnPay.setEnabled(true);
            }
        } else {
            tvTotalAmount.setText("Your cart is empty");
            btnPay.setEnabled(false);
        }
    }
}
