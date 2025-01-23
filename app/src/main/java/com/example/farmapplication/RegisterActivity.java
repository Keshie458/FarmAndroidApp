package com.example.farmapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText username, password;
    Button register;
    TextView switchToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        switchToLogin = findViewById(R.id.switchToLogin); // TextView to switch to LoginActivity

        register.setOnClickListener(v -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();
            if (db.registerUser(user, pass)) {
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class)); // Navigate back to Login
            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
            }
        });

        // Switching to the Login Activity when clicked
        switchToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
