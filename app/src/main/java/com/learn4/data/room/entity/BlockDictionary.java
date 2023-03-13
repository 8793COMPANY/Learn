package com.learn4.data.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "blockDictionary")
public class BlockDictionary {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String block_name;
    private String block_explanation;
    private String block_category;
    private String block_parameter;
    private String block_type;

    public BlockDictionary(String block_name, String block_explanation, String block_category, String block_parameter, String block_type){
        this.block_name = block_name;
        this.block_explanation = block_explanation;
        this.block_category = block_category;
        this.block_parameter = block_parameter;
        this.block_type = block_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBlock_name() {
        return block_name;
    }

    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    public String getBlock_explanation() {
        return block_explanation;
    }

    public void setBlock_explanation(String block_explanation) {
        this.block_explanation = block_explanation;
    }

    public String getBlock_category() {
        return block_category;
    }

    public void setBlock_category(String block_category) {
        this.block_category = block_category;
    }

    public String getBlock_parameter() {
        return block_parameter;
    }

    public void setBlock_parameter(String block_parameter) {
        this.block_parameter = block_parameter;
    }

    public String getBlock_type() {
        return block_type;
    }

    public void setBlock_type(String block_type) {
        this.block_type = block_type;
    }
}
