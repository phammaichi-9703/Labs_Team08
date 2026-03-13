package com.example.demo01;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo01.adapter.MyAdapter;
import com.example.demo01.model.Cat;
import com.example.demo01.model.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView reView;
    MyAdapter adapter;
    Button btn, btnLogout;
    EditText etPhoneNumber;
    ImageButton btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reView = findViewById(R.id.reView);
        btn = findViewById(R.id.btn);
        btnLogout = findViewById(R.id.btnLogout);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnCall = findViewById(R.id.btnCall);

        List<Cat> list = new ArrayList<>();
        list.add(new Cat("Mèo Anh", R.drawable.cat1));
        list.add(new Cat("Mèo Ba Tư", R.drawable.cat2));
        list.add(new Cat("Mèo Xiêm", R.drawable.cat3));
        list.add(new Cat("Mèo Maine Coon", R.drawable.cat4));
        list.add(new Cat("Mèo Cun", R.drawable.cat5));

        adapter = new MyAdapter(list);

        reView.setLayoutManager(new GridLayoutManager(this, 2));
        reView.setAdapter(adapter);

        adapter.setOnItemListener(position ->
                Toast.makeText(this,
                        "Chi yêu Cún " + list.get(position).getName(),
                        Toast.LENGTH_SHORT).show()
        );

        btn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            intent.putExtra("name", "Zee mnsd");
            intent.putExtra("age", 20);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Xử lý Intent không tường minh để gọi điện
        btnCall.setOnClickListener(v -> {
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            if (!phoneNumber.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
