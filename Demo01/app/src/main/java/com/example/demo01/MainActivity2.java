package com.example.demo01;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    TextView txt1;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        txt1 = findViewById(R.id.txt1);
        btnBack = findViewById(R.id.btnBack);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        int age = intent.getIntExtra("age", 0);

        txt1.setText("Name: " + name + "\nAge: " + age);

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }
}