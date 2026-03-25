package com.example.day_25_03.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "products",
        foreignKeys = @ForeignKey(entity = Category.class,
                parentColumns = "id",
                childColumns = "categoryId",
                onDelete = ForeignKey.CASCADE))
public class Product {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public double price;
    public int categoryId;
    public String description;

    public Product(String name, double price, int categoryId, String description) {
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.description = description;
    }
}
