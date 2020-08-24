package com.example.fitnesstrackerapp.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitnesstrackerapp.Item;

import java.util.List;

@Dao
public interface ItemDao {

    @Insert
    void insert(Item item);

    @Delete
    void delete(Item item);

    @Query("SELECT * FROM items_table ")
    List<Item> getAllItems();

    @Query("DELETE FROM items_table")
    void deleteAll();
}
