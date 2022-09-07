package com.corporation8793.room.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "component")
public class Component {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String number;
    private String name;
    private String component_msg;


    public Component(String number, String name, String component_msg){
        this.number=number;
        this.name=name;
        this.component_msg = component_msg;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComponent_msg() {
        return component_msg;
    }

    public void setComponent_msg(String component_msg) {
        this.component_msg = component_msg;
    }


}
