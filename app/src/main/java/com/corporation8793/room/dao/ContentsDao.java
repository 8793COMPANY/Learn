package com.corporation8793.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.corporation8793.room.entity.Contents;

import java.util.List;

@Dao
public interface ContentsDao {

    @Query("SELECT * FROM contents")
    List<Contents> findAll();

    @Query("SELECT * FROM contents where level=:level")
    List<Contents> findByLevel(int level);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Contents contents);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Contents> contents);


    @Delete
    void delete(Contents contents); //내부에 값을 넣어서 삭제 가능(오버로딩)
}
