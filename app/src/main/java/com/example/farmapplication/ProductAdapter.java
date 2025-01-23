package com.example.farmapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> productList;
    private DatabaseHelper db;
    private Context context;

    // Constructor
    public ProductAdapter(Context context, Cursor cursor, DatabaseHelper db) {
        this.context = context;
        this.db = db;
        this.productList = getProductList(cursor);
    }

    // Converts Cursor data into a List of Product objects
    private List<Product> getProductList(Cursor cursor) {
        List<Product> products = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String plantrow = cursor.getString(2);
            double price = cursor.getDouble(3);
            products.add(new Product(id, name, plantrow, price));
        }
        cursor.close();
        return products;
    }

    // Method to update the product list and notify the adapter
    public void updateData(Cursor cursor) {
        this.productList = getProductList(cursor);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the product item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productRow.setText("Row: " + product.getPlantRow());
        holder.productPrice.setText("Price: $" + product.getPrice());

        // Set up the delete button to remove the product from the database
        holder.deleteButton.setOnClickListener(v -> {
            db.deleteProduct(product.getId());
            updateData(db.getAllProducts());
            Toast.makeText(context, "Product Deleted", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // ViewHolder to bind the product data to the views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productRow, productPrice;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productRow = itemView.findViewById(R.id.productRow);
            productPrice = itemView.findViewById(R.id.productPrice);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
