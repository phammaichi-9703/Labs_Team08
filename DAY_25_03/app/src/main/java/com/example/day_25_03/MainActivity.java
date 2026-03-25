package com.example.day_25_03;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.day_25_03.adapters.CategoryAdapter;
import com.example.day_25_03.adapters.ProductAdapter;
import com.example.day_25_03.data.AppDatabase;
import com.example.day_25_03.data.entities.Category;
import com.example.day_25_03.data.entities.Product;
import com.example.day_25_03.data.entities.User;
import com.example.day_25_03.utils.UserSession;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    private UserSession session;
    private RecyclerView recyclerView;
    private TextView tvWelcome;
    private Button btnLoginMain, btnShowProducts, btnShowCategories, btnCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        session = new UserSession(this);

        initDummyData();

        tvWelcome = findViewById(R.id.tvWelcome);
        btnLoginMain = findViewById(R.id.btnLoginMain);
        btnShowProducts = findViewById(R.id.btnShowProducts);
        btnShowCategories = findViewById(R.id.btnShowCategories);
        btnCart = findViewById(R.id.btnCart);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnLoginMain.setOnClickListener(v -> {
            if (session.isLoggedIn()) {
                session.logoutUser();
                updateUI();
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        btnCart.setOnClickListener(v -> {
            if (session.isLoggedIn()) {
                startActivity(new Intent(this, CheckoutActivity.class));
            } else {
                Toast.makeText(this, "Please login to view cart", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        btnShowProducts.setOnClickListener(v -> showProducts());
        btnShowCategories.setOnClickListener(v -> showCategories());

        showProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        if (session.isLoggedIn()) {
            User user = db.shopDao().getUserById(session.getUserId());
            tvWelcome.setText("Hello, " + (user != null ? user.fullName : "User"));
            btnLoginMain.setText("Logout");
        } else {
            tvWelcome.setText("Welcome!");
            btnLoginMain.setText("Login");
        }
    }

    private void showProducts() {
        List<Product> products = db.shopDao().getAllProducts();
        ProductAdapter adapter = new ProductAdapter(products, product -> {
            Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", product.id);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private void showCategories() {
        List<Category> categories = db.shopDao().getAllCategories();
        CategoryAdapter adapter = new CategoryAdapter(categories, category -> {
            List<Product> filteredProducts = db.shopDao().getProductsByCategory(category.id);
            ProductAdapter productAdapter = new ProductAdapter(filteredProducts, product -> {
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("PRODUCT_ID", product.id);
                startActivity(intent);
            });
            recyclerView.setAdapter(productAdapter);
        });
        recyclerView.setAdapter(adapter);
    }

    private void initDummyData() {
        if (db.shopDao().getAllCategories().isEmpty()) {
            db.shopDao().insertCategory(new Category("Electronics"), new Category("Clothing"));
            List<Category> cats = db.shopDao().getAllCategories();
            
            db.shopDao().insertProduct(
                new Product("Smartphone", 699.99, cats.get(0).id, "Latest model smartphone"),
                new Product("Laptop", 1200.00, cats.get(0).id, "Powerful laptop for work"),
                new Product("T-Shirt", 19.99, cats.get(1).id, "Comfortable cotton t-shirt")
            );

            db.shopDao().insertUser(new User("admin", "admin123", "Administrator"));
        }
    }
}
