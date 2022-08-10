package com.corporation8793.dto;

public class Contents {
    public String id;
    public int group;
    public String title;
    public int image;
    public int percentage;

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



    public Contents(String id, int group, String title, int image, int percentage) {
        this.id = id;
        this.group = group;
        this.title = title;
        this.image = image;
        this.percentage = percentage;
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
