package io.artcreativity.monpremierprojet.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.artcreativity.monpremierprojet.entities.Product;

//@Dao
//public interface ProductRoomDao {
//    @Query("SELECT * FROM product")
//    List<Product> findAll();
//
//    @Query("SELECT * FROM product WHERE id IN (:userIds)")
//    List<Product> loadAllByIds(int[] userIds);
//
//    @Query("SELECT * FROM product WHERE name LIKE :search AND " +
//            "description LIKE :search")
//    default List<Product> findByName(String name, String search) {
//        return null;
//    }
//
//    @Insert
//    void insertAll(Product... products);
//
//    @Insert
//    void insert(Product product);
//
//    @Delete
//    void delete(Product product);
//
//    default void update(Product product) {
//
//    }
//
//    List<Product> findByName(String search);
//}

@Dao
public interface ProductRoomDao {
    @Query("SELECT * FROM product")
    List<Product> findAll();

    @Query("SELECT * FROM product WHERE id IN (:userIds)")
    List<Product> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM product WHERE name LIKE :name AND " +
            "description LIKE :description")
    List<Product> findByName(String name, String description);

    @Insert
    void insertAll(Product... products);

    @Insert
    void insert(Product product);

    @Delete
    void delete(Product product);

    @Update
    void update(Product product);
}
