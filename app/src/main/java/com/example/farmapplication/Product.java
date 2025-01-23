package com.example.farmapplication;

public class Product {
    final private int id;
    final private String name;
    final private String plantrow;
    final private double price;

    // Constructor
    public Product(int id, String name, String row, double price) {
        this.id = id;
        this.name = name;
        this.plantrow = row;
        this.price = price;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getPlantRow() { return plantrow; }
    public double getPrice() { return price; }
}

