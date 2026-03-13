package com.example.demo01.model;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo01.DatabaseHelper;
import com.example.demo01.R;

public class RegisterActivity extends AppCompatActivity {

    EditText etUsernameReg, etPasswordReg;
    Button btnRegister, btnBackToLogin;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);

        etUsernameReg = findViewById(R.id.etUsernameReg);
        etPasswordReg = findViewById(R.id.etPasswordReg);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        btnRegister.setOnClickListener(v -> {
            String user = etUsernameReg.getText().toString().trim();
            String pass = etPasswordReg.getText().toString().trim();

            if (user.equals("") || pass.equals("")) {
                Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                db.addUser(user, pass);
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                finish(); // Quay lại màn hình Login
            }
        });

        btnBackToLogin.setOnClickListener(v -> finish());
    }
}
