package com.example.farmapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database and Table Names
    public static final String DATABASE_NAME = "FarmProducts.db";
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_PRODUCTS = "Products";

    // Users Table Columns
    public static final String COLUMN_USER_ID = "ID";
    public static final String COLUMN_USERNAME = "Username";
    public static final String COLUMN_PASSWORD = "Password";

    // Products Table Columns
    public static final String COLUMN_PRODUCT_ID = "ID";
    public static final String COLUMN_PRODUCT_NAME = "Name";
    public static final String COLUMN_PRODUCT_ROW = "plantrow";
    public static final String COLUMN_PRODUCT_PRICE = "Price";

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT UNIQUE, "
                + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Create Products table
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PRODUCT_NAME + " TEXT, "
                + COLUMN_PRODUCT_ROW + " TEXT, "
                + COLUMN_PRODUCT_PRICE + " REAL)";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);

        // Recreate the tables
        onCreate(db);
    }

    // User Registration
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;  // Returns true if successful, false otherwise
    }

    // User Login
    public boolean loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE "
                        + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{username, password});
        return cursor.getCount() > 0; // Returns true if user exists
    }

    // CRUD Operations for Products
    // Add Product
    /*public boolean addProduct(String name, String row, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, name);
        values.put(COLUMN_PRODUCT_ROW, row);
        values.put(COLUMN_PRODUCT_PRICE, price);
        long result = db.insert(TABLE_PRODUCTS, null, values);
        return result != -1;  // Returns true if successful, false otherwise
    }*/public boolean addProduct(String name, String row, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("plantrow", row);
        values.put("Price", price);
        long result = db.insert("Products", null, values);

        if (result == -1) {
            Log.e("DatabaseHelper", "Failed to insert product");
        } else {
            Log.d("DatabaseHelper", "Product inserted successfully");
        }

        return result != -1;
    }


    // Get All Products
    /*public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
    }*/
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Products", null);

        if (cursor.getCount() == 0) {
            Log.e("DatabaseHelper", "No products found in the database");
        } else {
            Log.d("DatabaseHelper", "Products found: " + cursor.getCount());
        }

        return cursor;
    }


    // Update Product
    public boolean updateProduct(int id, String name, String row, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, name);
        values.put(COLUMN_PRODUCT_ROW, row);
        values.put(COLUMN_PRODUCT_PRICE, price);
        int result = db.update(TABLE_PRODUCTS, values, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0; // Returns true if update is successful
    }

    // Delete Product
    public boolean deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PRODUCTS, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0; // Returns true if deletion is successful
    }

    // Export Products to CSV format
    public String exportProductsToCSV(Context context) {
        Cursor cursor = getAllProducts();
        StringBuilder report = new StringBuilder("ID,Name,Row,Price\n");

        while (cursor.moveToNext()) {
            report.append(cursor.getInt(0)).append(",");
            report.append(cursor.getString(1)).append(",");
            report.append(cursor.getString(2)).append(",");
            report.append(cursor.getDouble(3)).append("\n");
        }
        cursor.close();
        return report.toString();
    }
}
