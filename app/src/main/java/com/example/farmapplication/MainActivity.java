package com.example.farmapplication;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Environment;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText productName, productRow, productPrice;
    RecyclerView recyclerView;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        productName = findViewById(R.id.productName);
        productRow = findViewById(R.id.productRow);
        productPrice = findViewById(R.id.productPrice);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ProductAdapter(this, db.getAllProducts(), db);
        recyclerView.setAdapter(adapter);

        // Add product functionality
        findViewById(R.id.addProduct).setOnClickListener(v -> {
            String name = productName.getText().toString();
            String row = productRow.getText().toString();
            double price = Double.parseDouble(productPrice.getText().toString());
            if (db.addProduct(name, row, price)) {
                Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show();
                //updateProductList();  // This was not refreshing the recycler view
                adapter.updateData(db.getAllProducts());
            } else {
                Toast.makeText(this, "Failed to Add Product", Toast.LENGTH_SHORT).show();
            }
        });

        // Download CSV report functionality
        findViewById(R.id.downloadReport).setOnClickListener(v -> downloadReport());
    }

    // Method to refresh the product list
    private void updateProductList() {
        Cursor cursor = db.getAllProducts();
        adapter.updateData(cursor);
    }

    // Method to download the report as a CSV file
    private void downloadReport() {
        Cursor cursor = db.getAllProducts();
        StringBuilder report = new StringBuilder("ID,Name,Row,Price\n");
        while (cursor.moveToNext()) {
            report.append(cursor.getInt(0)).append(",")
                    .append(cursor.getString(1)).append(",")
                    .append(cursor.getString(2)).append(",")
                    .append(cursor.getDouble(3)).append("\n");
        }

        // Save to external storage (Downloads folder)
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!path.exists()) {
            path.mkdirs(); // Create directory if it doesn't exist
        }

        File file = new File(path, "products_report.csv");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(report.toString().getBytes());
            Toast.makeText(this, "Report Downloaded", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error Downloading Report", Toast.LENGTH_SHORT).show();
        }


    }
}
