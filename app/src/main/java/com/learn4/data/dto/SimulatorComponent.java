package com.learn4.data.dto;


public class SimulatorComponent {
    int component_image;
    String pin_num;

    public SimulatorComponent(int component_image, String pin_num) {
        this.component_image = component_image;
        this.pin_num = pin_num;
    }

    public int getComponent_image() {
        return component_image;
    }

    public void setComponent_image(int component_image) {
        this.component_image = component_image;
    }

    public String getPin_num() {
        return pin_num;
    }

    public void setPin_num(String pin_num) {
        this.pin_num = pin_num;
    }
}
