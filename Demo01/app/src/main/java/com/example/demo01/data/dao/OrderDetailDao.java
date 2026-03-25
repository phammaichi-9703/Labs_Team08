package com.example.demo01.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.demo01.data.entity.OrderDetail;
import java.util.List;

@Dao
public interface OrderDetailDao {
    @Insert
    void insert(OrderDetail orderDetail);

    @Query("SELECT * FROM order_details WHERE orderId = :orderId")
    List<OrderDetail> getOrderDetailsByOrder(int orderId);

    @Query("SELECT * FROM order_details WHERE orderId = :orderId AND productId = :productId LIMIT 1")
    OrderDetail getOrderDetail(int orderId, int productId);
}
