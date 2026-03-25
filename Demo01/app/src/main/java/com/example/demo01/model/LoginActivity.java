package com.example.demo01.model;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo01.MainActivity;
import com.example.demo01.R;
import com.example.demo01.data.AppDatabase;
import com.example.demo01.data.entity.User;
import com.example.demo01.utils.PreferenceManager;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin, btnGoRegister;
    AppDatabase db;
    PreferenceManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = AppDatabase.getInstance(this);
        prefManager = new PreferenceManager(this);

        // If already logged in, go to MainActivity
        if (prefManager.isLoggedIn()) {
            startMainActivity();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoRegister = findViewById(R.id.btnGoRegister);

        btnLogin.setOnClickListener(v -> {
            String userStr = etUsername.getText().toString().trim();
            String passStr = etPassword.getText().toString().trim();

            User user = db.userDao().login(userStr, passStr);
            if (user != null) {
                prefManager.setLogin(user.id, true);
                Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                startMainActivity();
            } else {
                Toast.makeText(LoginActivity.this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });

        btnGoRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
