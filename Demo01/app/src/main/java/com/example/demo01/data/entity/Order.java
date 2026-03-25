package com.example.demo01.data.entity;
//
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE))
public class Order {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public long orderDate;
    public String status; // "Pending", "Paid"
    public double totalAmount;

    public Order(int userId, long orderDate, String status, double totalAmount) {
        this.userId = userId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
    }
}
