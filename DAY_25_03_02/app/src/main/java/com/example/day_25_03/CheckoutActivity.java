package com.example.day_25_03;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.day_25_03.adapters.OrderDetailAdapter;
import com.example.day_25_03.data.AppDatabase;
import com.example.day_25_03.data.entities.Order;
import com.example.day_25_03.data.entities.OrderDetail;
import com.example.day_25_03.utils.UserSession;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {
    private RecyclerView rvCartItems;
    private TextView tvTotalAmount;
    private MaterialButton btnPay;
    private AppDatabase db;
    private UserSession session;
    private Order pendingOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        db = AppDatabase.getInstance(this);
        session = new UserSession(this);

        Toolbar toolbar = findViewById(R.id.toolbar_checkout);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rvCartItems = findViewById(R.id.rvCartItems);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnPay = findViewById(R.id.btnPay);

        rvCartItems.setLayoutManager(new LinearLayoutManager(this));

        loadInvoice();

        btnPay.setOnClickListener(v -> {
            if (pendingOrder != null) {
                // Change status to Paid
                pendingOrder.status = "Paid";
                db.shopDao().updateOrder(pendingOrder);
                
                Toast.makeText(this, "Payment Successful! Items removed from current invoice.", Toast.LENGTH_LONG).show();
                
                // Finish and go back
                finish();
            }
        });
    }

    private void loadInvoice() {
        int userId = session.getUserId();
        pendingOrder = db.shopDao().getPendingOrderByUser(userId);

        if (pendingOrder != null) {
            List<OrderDetail> details = db.shopDao().getOrderDetailsByOrder(pendingOrder.id);
            if (details.isEmpty()) {
                showEmptyInvoice();
            } else {
                OrderDetailAdapter adapter = new OrderDetailAdapter(details, db);
                rvCartItems.setAdapter(adapter);
                tvTotalAmount.setText(String.format(Locale.getDefault(), "$%.2f", pendingOrder.totalAmount));
                btnPay.setEnabled(true);
                btnPay.setAlpha(1.0f);
            }
        } else {
            showEmptyInvoice();
        }
    }

    private void showEmptyInvoice() {
        tvTotalAmount.setText("$0.00");
        btnPay.setEnabled(false);
        btnPay.setAlpha(0.5f);
        Toast.makeText(this, "Your current invoice is empty", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
