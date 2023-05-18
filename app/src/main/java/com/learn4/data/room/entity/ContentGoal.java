package com.learn4.data.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ContentGoal")
public class ContentGoal {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String main_category;
    private String sub_category;
    private String total_category;
    private String category_goal;
    private String category_condition;

    public ContentGoal(String main_category, String sub_category, String total_category, String category_goal, String category_condition) {
        this.main_category = main_category;
        this.sub_category = sub_category;
        this.total_category = total_category;
        this.category_goal = category_goal;
        this.category_condition = category_condition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMain_category() {
        return main_category;
    }

    public void setMain_category(String main_category) {
        this.main_category = main_category;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getTotal_category() {
        return total_category;
    }

    public void setTotal_category(String total_category) {
        this.total_category = total_category;
    }

    public String getCategory_goal() {
        return category_goal;
    }

    public void setCategory_goal(String category_goal) {
        this.category_goal = category_goal;
    }

    public String getCategory_condition() {
        return category_condition;
    }

    public void setCategory_condition(String category_condition) {
        this.category_condition = category_condition;
    }
}
