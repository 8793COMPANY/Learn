package com.learn4.data.dto;

public class Supplies {
    int img;
    Boolean click;

    public Supplies(int img, Boolean click) {
        this.img = img;
        this.click = click;
    }




    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public Boolean getClick() {
        return click;
    }

    public void setClick(Boolean click) {
        this.click = click;
    }


}
