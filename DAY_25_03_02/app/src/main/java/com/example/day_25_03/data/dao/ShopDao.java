
package com.example.day_25_03.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.day_25_03.data.entities.Category;
import com.example.day_25_03.data.entities.Order;
import com.example.day_25_03.data.entities.OrderDetail;
import com.example.day_25_03.data.entities.Product;
import com.example.day_25_03.data.entities.User;

import java.util.List;

@Dao
public interface ShopDao {
    // User
    @Insert
    long insertUser(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    @Query("SELECT * FROM users WHERE id = :userId")
    User getUserById(int userId);

    // Category
    @Insert
    void insertCategory(Category... categories);

    @Query("SELECT * FROM categories")
    List<Category> getAllCategories();

    // Product
    @Insert
    void insertProduct(Product... products);

    @Query("SELECT * FROM products")
    List<Product> getAllProducts();

    @Query("SELECT * FROM products WHERE categoryId = :categoryId")
    List<Product> getProductsByCategory(int categoryId);

    @Query("SELECT * FROM products WHERE id = :productId")
    Product getProductById(int productId);

    // Order
    @Insert
    long insertOrder(Order order);

    @Update
    void updateOrder(Order order);

    @Query("SELECT * FROM orders WHERE userId = :userId AND status = 'Pending' LIMIT 1")
    Order getPendingOrderByUser(int userId);

    @Query("SELECT * FROM orders WHERE id = :orderId")
    Order getOrderById(int orderId);

    // OrderDetail
    @Insert
    void insertOrderDetail(OrderDetail orderDetail);

    @Query("SELECT * FROM order_details WHERE orderId = :orderId")
    List<OrderDetail> getOrderDetailsByOrder(int orderId);
}
