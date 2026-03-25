package com.example.day_25_03;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.day_25_03.data.AppDatabase;
import com.example.day_25_03.data.entities.Order;
import com.example.day_25_03.data.entities.OrderDetail;
import com.example.day_25_03.data.entities.Product;
import com.example.day_25_03.utils.UserSession;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView tvName, tvPrice, tvDescription;
    private MaterialButton btnAddToCart;
    private AppDatabase db;
    private UserSession session;
    private int productId;
    private Product product;
    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        db = AppDatabase.getInstance(this);
        session = new UserSession(this);

        productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        product = db.shopDao().getProductById(productId);

        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        tvName = findViewById(R.id.tvDetailName);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvDescription = findViewById(R.id.tvDetailDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        if (product != null) {
            collapsingToolbar.setTitle(product.name);
            tvName.setText(product.name);
            tvPrice.setText(String.format(Locale.getDefault(), "$%.2f", product.price));
            tvDescription.setText(product.description);
        }

        btnAddToCart.setOnClickListener(v -> {
            if (!session.isLoggedIn()) {
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                addToInvoice();
            }
        });
    }

    private void addToInvoice() {
        int userId = session.getUserId();
        Order pendingOrder = db.shopDao().getPendingOrderByUser(userId);

        if (pendingOrder == null) {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            long orderId = db.shopDao().insertOrder(new Order(userId, currentDate, "Pending", 0));
            pendingOrder = db.shopDao().getOrderById((int) orderId);
        }

        OrderDetail detail = new OrderDetail(pendingOrder.id, product.id, 1, product.price);
        db.shopDao().insertOrderDetail(detail);

        pendingOrder.totalAmount += product.price;
        db.shopDao().updateOrder(pendingOrder);

        new AlertDialog.Builder(this)
                .setTitle("Added to Invoice")
                .setMessage("Item added successfully. What would you like to do?")
                .setPositiveButton("Continue Shopping", (dialog, which) -> finish())
                .setNegativeButton("View Invoice", (dialog, which) -> {
                    startActivity(new Intent(this, CheckoutActivity.class));
                    finish();
                })
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
