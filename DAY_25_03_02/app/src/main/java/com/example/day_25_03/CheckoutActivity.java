package com.example.day_25_03;

import android.os.Bundle;
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

        initViews();
        initData();
        setupToolbar();
        setupRecyclerView();
        loadInvoice();
        setupActions();
    }

    private void initViews() {
        rvCartItems = findViewById(R.id.rvCartItems);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnPay = findViewById(R.id.btnPay);
    }

    private void initData() {
        db = AppDatabase.getInstance(this);
        session = new UserSession(this);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_checkout);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecyclerView() {
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupActions() {
        btnPay.setOnClickListener(v -> handlePayment());
    }

    private void loadInvoice() {
        int userId = session.getUserId();
        pendingOrder = db.shopDao().getPendingOrderByUser(userId);

        if (pendingOrder == null) {
            showEmptyInvoice();
            return;
        }

        List<OrderDetail> details = db.shopDao().getOrderDetailsByOrder(pendingOrder.id);

        if (details == null || details.isEmpty()) {
            showEmptyInvoice();
            return;
        }

        displayInvoice(details);
    }

    private void displayInvoice(List<OrderDetail> details) {
        OrderDetailAdapter adapter = new OrderDetailAdapter(details, db);
        rvCartItems.setAdapter(adapter);

        tvTotalAmount.setText(formatPrice(pendingOrder.totalAmount));
        setPayButtonState(true);
    }

    private void handlePayment() {
        if (pendingOrder == null) return;

        pendingOrder.status = "Paid";
        db.shopDao().updateOrder(pendingOrder);

        showToast("Payment Successful! Items removed from current invoice.");
        finish();
    }

    private void showEmptyInvoice() {
        tvTotalAmount.setText(formatPrice(0));
        setPayButtonState(false);
        showToast("Your current invoice is empty");
    }

    private void setPayButtonState(boolean enabled) {
        btnPay.setEnabled(enabled);
        btnPay.setAlpha(enabled ? 1.0f : 0.5f);
    }

    private String formatPrice(double amount) {
        return String.format(Locale.getDefault(), "$%.2f", amount);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
