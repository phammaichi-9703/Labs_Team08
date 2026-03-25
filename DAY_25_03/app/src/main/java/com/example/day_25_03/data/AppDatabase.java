package com.example.day_25_03.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.day_25_03.data.dao.ShopDao;
import com.example.day_25_03.data.entities.Category;
import com.example.day_25_03.data.entities.Order;
import com.example.day_25_03.data.entities.OrderDetail;
import com.example.day_25_03.data.entities.Product;
import com.example.day_25_03.data.entities.User;

@Database(entities = {User.class, Category.class, Product.class, Order.class, OrderDetail.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract ShopDao shopDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "shop_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // For simplicity in this example
                    .build();
        }
        return instance;
    }
}
