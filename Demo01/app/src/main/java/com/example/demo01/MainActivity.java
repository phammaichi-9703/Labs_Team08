package com.example.demo01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo01.adapter.ProductAdapter;
import com.example.demo01.data.AppDatabase;
import com.example.demo01.data.entity.Category;
import com.example.demo01.data.entity.Product;
import com.example.demo01.model.LoginActivity;
import com.example.demo01.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvProducts;
    ProductAdapter adapter;
    Button btnLogout, btnGoToCart;
    Spinner spCategories;
    AppDatabase db;
    PreferenceManager prefManager;
    List<Product> productList = new ArrayList<>();
    List<Category> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        prefManager = new PreferenceManager(this);

        if (!prefManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        rvProducts = findViewById(R.id.rvProducts);
        btnLogout = findViewById(R.id.btnLogout);
        btnGoToCart = findViewById(R.id.btnGoToCart);
        spCategories = findViewById(R.id.spCategories);

        initData();
        setupRecyclerView();
        setupCategorySpinner();

        btnLogout.setOnClickListener(v -> {
            prefManager.logout();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        btnGoToCart.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, OrderActivity.class));
        });
    }

    private void initData() {
        categoryList = db.categoryDao().getAllCategories();
        if (categoryList.isEmpty()) {
            db.categoryDao().insert(new Category("All", "All categories"));
            db.categoryDao().insert(new Category("Electronics", "Gadgets and more"));
            db.categoryDao().insert(new Category("Clothing", "Apparel and fashion"));
            categoryList = db.categoryDao().getAllCategories();
        }

        productList = db.productDao().getAllProducts();
        if (productList.isEmpty()) {
            db.productDao().insert(new Product("Laptop", "Powerful laptop", 1200.0, categoryList.get(1).id, ""));
            db.productDao().insert(new Product("Smartphone", "Latest model", 800.0, categoryList.get(1).id, ""));
            db.productDao().insert(new Product("T-Shirt", "Cotton t-shirt", 20.0, categoryList.get(2).id, ""));
            productList = db.productDao().getAllProducts();
        }
    }

    private void setupRecyclerView() {
        adapter = new ProductAdapter(productList, product -> {
            Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", product.id);
            startActivity(intent);
        });
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        rvProducts.setAdapter(adapter);
    }

    private void setupCategorySpinner() {
        List<String> categoryNames = new ArrayList<>();
        for (Category c : categoryList) {
            categoryNames.add(c.name);
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategories.setAdapter(spinnerAdapter);

        spCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category selected = categoryList.get(position);
                if (selected.name.equals("All")) {
                    productList.clear();
                    productList.addAll(db.productDao().getAllProducts());
                } else {
                    productList.clear();
                    productList.addAll(db.productDao().getProductsByCategory(selected.id));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
