package com.learn4.data.dto;

public class Weather {
    String date;
    String time;

    public Weather(String date, String time, String local, String type) {
        this.date = date;
        this.time = time;
        this.local = local;
        this.type = type;
        all_type = date+time+local;
    }

    String local;
    String type;
    String all_type;

    public String getDate() {
        return date;
    }

    public String getAll_type(){
        return all_type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }




}
