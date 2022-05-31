package com.corporation8793.room.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contents")
public class Contents {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int level;
    private String name;
    private String basic_problem;
    private String deep_problem1;
    private String deep_problem2;
    private String basic_supplies;
    private String deepening_supplies1;
    private String deepening_supplies2;

    public Contents(int level, String name, String basic_problem, String deep_problem1, String deep_problem2
            , String basic_supplies, String deepening_supplies1, String deepening_supplies2){
        this.level = level;
        this.name = name;
        this.basic_problem = basic_problem;
        this.deep_problem1 = deep_problem1;
        this.deep_problem2 = deep_problem2;
        this.basic_supplies = basic_supplies;
        this.deepening_supplies1 = deepening_supplies1;
        this.deepening_supplies2 = deepening_supplies2;


    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBasic_problem() {
        return basic_problem;
    }

    public void setBasic_problem(String basic_problem) {
        this.basic_problem = basic_problem;
    }

    public String getDeep_problem1() {
        return deep_problem1;
    }

    public void setDeep_problem1(String deep_problem1) {
        this.deep_problem1 = deep_problem1;
    }

    public String getDeep_problem2() {
        return deep_problem2;
    }

    public void setDeep_problem2(String deep_problem2) {
        this.deep_problem2 = deep_problem2;
    }

    public String getBasic_supplies() {
        return basic_supplies;
    }

    public void setBasic_supplies(String basic_supplies) {
        this.basic_supplies = basic_supplies;
    }

    public String getDeepening_supplies1() {
        return deepening_supplies1;
    }

    public void setDeepening_supplies1(String deepening_supplies1) {
        this.deepening_supplies1 = deepening_supplies1;
    }

    public String getDeepening_supplies2() {
        return deepening_supplies2;
    }

    public void setDeepening_supplies2(String deepening_supplies2) {
        this.deepening_supplies2 = deepening_supplies2;
    }







}
