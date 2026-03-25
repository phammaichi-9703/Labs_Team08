package com.example.day_25_03;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.day_25_03.adapters.ProductAdapter;
import com.example.day_25_03.data.AppDatabase;
import com.example.day_25_03.data.entities.Category;
import com.example.day_25_03.data.entities.Product;
import com.example.day_25_03.data.entities.User;
import com.example.day_25_03.utils.UserSession;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    private UserSession session;
    private RecyclerView recyclerView;
    private MaterialButton btnLoginMain;
    private ChipGroup chipGroupCategories;
    private ExtendedFloatingActionButton btnViewInvoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        session = new UserSession(this);

        initDummyData();

        btnLoginMain = findViewById(R.id.btnLoginMain);
        chipGroupCategories = findViewById(R.id.chipGroupCategories);
        btnViewInvoice = findViewById(R.id.btnViewInvoice);
        recyclerView = findViewById(R.id.recyclerView);
        
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        btnLoginMain.setOnClickListener(v -> {
            if (session.isLoggedIn()) {
                session.logoutUser();
                updateUI();
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        btnViewInvoice.setOnClickListener(v -> {
            if (session.isLoggedIn()) {
                startActivity(new Intent(this, CheckoutActivity.class));
            } else {
                Toast.makeText(this, "Please login to view invoice", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        setupCategories();
        showProducts(-1);
    }

    private void setupCategories() {
        List<Category> categories = db.shopDao().getAllCategories();
        
        // Add "All" chip
        Chip allChip = new Chip(this);
        allChip.setText("All");
        allChip.setCheckable(true);
        allChip.setChecked(true);
        allChip.setOnClickListener(v -> showProducts(-1));
        chipGroupCategories.addView(allChip);

        for (Category category : categories) {
            Chip chip = new Chip(this);
            chip.setText(category.name);
            chip.setCheckable(true);
            chip.setOnClickListener(v -> showProducts(category.id));
            chipGroupCategories.addView(chip);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        if (session.isLoggedIn()) {
            User user = db.shopDao().getUserById(session.getUserId());
            btnLoginMain.setText("Logout (" + (user != null ? user.username : "") + ")");
        } else {
            btnLoginMain.setText("Login");
        }
    }

    private void showProducts(int categoryId) {
        List<Product> products;
        if (categoryId == -1) {
            products = db.shopDao().getAllProducts();
        } else {
            products = db.shopDao().getProductsByCategory(categoryId);
        }
        
        ProductAdapter adapter = new ProductAdapter(products, product -> {
            Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", product.id);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private void initDummyData() {
        if (db.shopDao().getAllCategories().isEmpty()) {
            db.shopDao().insertCategory(
                new Category("Electronics"), 
                new Category("Clothing"),
                new Category("Home"),
                new Category("Books")
            );
            List<Category> cats = db.shopDao().getAllCategories();
            
            db.shopDao().insertProduct(
                new Product("iPhone 15", 999.00, cats.get(0).id, "Latest Apple smartphone with A17 chip"),
                new Product("MacBook Air", 1199.00, cats.get(0).id, "Thin and light laptop with M2 chip"),
                new Product("Sony Headphones", 349.00, cats.get(0).id, "Industry-leading noise canceling"),
                new Product("Nike Air Max", 120.00, cats.get(1).id, "Classic comfortable running shoes"),
                new Product("Levi's 501", 69.50, cats.get(1).id, "Original fit straight leg jeans"),
                new Product("Coffee Mug", 12.00, cats.get(2).id, "Ceramic mug for your morning coffee"),
                new Product("Desk Lamp", 45.00, cats.get(2).id, "LED lamp with adjustable brightness"),
                new Product("Java Programming", 55.00, cats.get(3).id, "Complete guide to Java 17")
            );

            db.shopDao().insertUser(new User("admin", "admin123", "Administrator"));
            db.shopDao().insertUser(new User("user", "123", "Standard User"));
        }
    }
}
