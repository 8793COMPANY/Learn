package com.learn4.data.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.learn4.data.room.entity.BlockDictionary;

import java.util.List;

@Dao
public interface BlockDictionaryDao {

    @Query("SELECT * FROM blockDictionary")
    List<BlockDictionary> findAll();

    @Insert
    void insert(BlockDictionary blockDictionary);

    @Delete
    void delete(BlockDictionary blockDictionary);
}
