package com.example.farmapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText username, password;
    Button login;
    TextView switchToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        switchToRegister = findViewById(R.id.switchToRegister); // TextView to switch to RegisterActivity

        login.setOnClickListener(v -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();
            if (db.loginUser(user, pass)) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        // Switching to the Registration Activity when clicked
        switchToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
