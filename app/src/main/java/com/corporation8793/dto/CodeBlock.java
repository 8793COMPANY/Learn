package com.corporation8793.dto;

public class CodeBlock {
    String id;
    String name;
    String info;
    int img;

    public CodeBlock(String id, String title, String info, int img) {
        this.id = id;
        this.name = title;
        this.info = info;
        this.img = img;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }



}
