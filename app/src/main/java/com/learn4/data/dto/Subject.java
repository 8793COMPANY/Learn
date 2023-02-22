package com.learn4.data.dto;

public class Subject {
    public String id;
    public int group;
    public String title;
    public int image;
    public int percentage;
    public boolean open;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }



    public Subject(String id, int group, String title, int image, int percentage, boolean open) {
        this.id = id;
        this.group = group;
        this.title = title;
        this.image = image;
        this.percentage = percentage;
        this.open = open;
    }


    public Boolean getOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }


}
