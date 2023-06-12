package com.learn4.data.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.learn4.data.room.entity.Component;
import com.learn4.data.room.entity.ContentGoal;

import java.util.List;

@Dao
public interface ContentsGoalDao {

    @Query("SELECT * FROM contentGoal")
    List<ContentGoal> findAll();

    @Query("SELECT * FROM contentgoal where sub_category=:sub_category")
    ContentGoal findByGoal(String sub_category);

    @Insert
    void insert(ContentGoal contentGoal);

    @Delete
    void delete(ContentGoal contentGoal);
}
