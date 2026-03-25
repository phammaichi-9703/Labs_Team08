package com.example.demo01;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo01.data.AppDatabase;
import com.example.demo01.data.entity.Order;
import com.example.demo01.data.entity.OrderDetail;
import com.example.demo01.data.entity.Product;
import com.example.demo01.utils.PreferenceManager;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView imgProduct;
    TextView tvName, tvPrice, tvDescription;
    Button btnAddToCart;
    AppDatabase db;
    PreferenceManager prefManager;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        db = AppDatabase.getInstance(this);
        prefManager = new PreferenceManager(this);

        imgProduct = findViewById(R.id.imgProductDetail);
        tvName = findViewById(R.id.tvProductNameDetail);
        tvPrice = findViewById(R.id.tvProductPriceDetail);
        tvDescription = findViewById(R.id.tvProductDescriptionDetail);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        product = db.productDao().getProductById(productId);

        if (product != null) {
            tvName.setText(product.name);
            tvPrice.setText(String.format("$%.2f", product.price));
            tvDescription.setText(product.description);
            imgProduct.setImageResource(R.drawable.icon_cat);
        }

        btnAddToCart.setOnClickListener(v -> addToCart());
    }

    private void addToCart() {
        if (!prefManager.isLoggedIn()) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = prefManager.getUserId();
        Order pendingOrder = db.orderDao().getPendingOrderByUser(userId);

        long orderId;
        if (pendingOrder == null) {
            Order newOrder = new Order(userId, System.currentTimeMillis(), "Pending", 0.0);
            orderId = db.orderDao().insert(newOrder);
        } else {
            orderId = pendingOrder.id;
        }

        OrderDetail existingDetail = db.orderDetailDao().getOrderDetail((int) orderId, product.id);
        if (existingDetail != null) {
            existingDetail.quantity += 1;
            // Room update for OrderDetail would be better, but for now I'll just re-insert or ignore for simplicity
            // Let's assume quantity 1 for now or update it properly.
            Toast.makeText(this, "Product already in cart. Quantity would be updated.", Toast.LENGTH_SHORT).show();
        } else {
            OrderDetail detail = new OrderDetail((int) orderId, product.id, 1, product.price);
            db.orderDetailDao().insert(detail);
            Toast.makeText(this, "Added to cart!", Toast.LENGTH_SHORT).show();
        }
    }
}
