package com.learn4.data.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.learn4.data.room.entity.ContentGoal;

import java.util.List;

@Dao
public interface ContentsGoalDao {

    @Query("SELECT * FROM contentGoal")
    List<ContentGoal> findAll();

    @Insert
    void insert(ContentGoal contentGoal);

    @Delete
    void delete(ContentGoal contentGoal);
}
