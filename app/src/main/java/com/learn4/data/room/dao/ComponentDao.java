package com.learn4.data.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.learn4.data.room.entity.Component;

import java.util.List;

@Dao
public interface ComponentDao {

    @Query("SELECT * FROM component")
    List<Component> findAll();

    @Query("SELECT * FROM component where number=:num")
    Component findById(String num);

    @Query("SELECT * FROM component where name=:name")
    Component findByName(String name);

    @Insert
    void insert(Component component);

    @Delete
    void delete(Component component); //내부에 값을 넣어서 삭제 가능(오버로딩)
}
