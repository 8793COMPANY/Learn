package com.learn4.data.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.learn4.data.room.entity.TestEntity;

import java.util.List;

@Dao
public interface TestEntityDao {

    @Query("SELECT * FROM testEntity")
    List<TestEntity> findAll();

    @Insert
    void insert(TestEntity testEntity);

    @Delete
    void delete(TestEntity testEntity);
}
