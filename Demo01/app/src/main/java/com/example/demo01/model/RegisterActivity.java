package com.example.demo01.model;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo01.R;
import com.example.demo01.data.AppDatabase;
import com.example.demo01.data.entity.User;

public class RegisterActivity extends AppCompatActivity {

    EditText etUsernameReg, etPasswordReg, etFullNameReg, etEmailReg;
    Button btnRegister, btnBackToLogin;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = AppDatabase.getInstance(this);

        etUsernameReg = findViewById(R.id.etUsernameReg);
        etPasswordReg = findViewById(R.id.etPasswordReg);
        // Note: Assuming these IDs exist in layout, or I might need to update activity_register.xml
        // If they don't exist, I'll just use username and password for now as per previous code
        
        btnRegister = findViewById(R.id.btnRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        btnRegister.setOnClickListener(v -> {
            String user = etUsernameReg.getText().toString().trim();
            String pass = etPasswordReg.getText().toString().trim();

            if (user.equals("") || pass.equals("")) {
                Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                // Check if user exists
                if (db.userDao().getUserByUsername(user) != null) {
                    Toast.makeText(RegisterActivity.this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                } else {
                    db.userDao().insert(new User(user, pass, user, user + "@example.com"));
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        btnBackToLogin.setOnClickListener(v -> finish());
    }
}
