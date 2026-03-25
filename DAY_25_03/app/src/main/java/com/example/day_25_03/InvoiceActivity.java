package com.example.day_25_03;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.day_25_03.adapters.OrderDetailAdapter;
import com.example.day_25_03.data.AppDatabase;
import com.example.day_25_03.data.entities.Order;
import com.example.day_25_03.data.entities.OrderDetail;
import com.example.day_25_03.data.entities.User;

import java.util.List;
import java.util.Locale;

public class InvoiceActivity extends AppCompatActivity {
    private TextView tvInvoiceId, tvInvoiceDate, tvCustomerName, tvInvoiceTotal;
    private RecyclerView rvInvoiceItems;
    private Button btnBackToHome;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        db = AppDatabase.getInstance(this);

        tvInvoiceId = findViewById(R.id.tvInvoiceId);
        tvInvoiceDate = findViewById(R.id.tvInvoiceDate);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvInvoiceTotal = findViewById(R.id.tvInvoiceTotal);
        rvInvoiceItems = findViewById(R.id.rvInvoiceItems);
        btnBackToHome = findViewById(R.id.btnBackToHome);

        rvInvoiceItems.setLayoutManager(new LinearLayoutManager(this));

        int orderId = getIntent().getIntExtra("ORDER_ID", -1);
        if (orderId != -1) {
            displayInvoice(orderId);
        }

        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void displayInvoice(int orderId) {
        Order order = db.shopDao().getOrderById(orderId);
        if (order != null) {
            User user = db.shopDao().getUserById(order.userId);
            List<OrderDetail> details = db.shopDao().getOrderDetailsByOrder(orderId);

            tvInvoiceId.setText("Order ID: #" + order.id);
            tvInvoiceDate.setText("Date: " + order.orderDate);
            tvCustomerName.setText("Customer: " + (user != null ? user.fullName : "N/A"));
            tvInvoiceTotal.setText(String.format(Locale.getDefault(), "Total: $%.2f", order.totalAmount));

            OrderDetailAdapter adapter = new OrderDetailAdapter(details, db);
            rvInvoiceItems.setAdapter(adapter);
        }
    }
}
