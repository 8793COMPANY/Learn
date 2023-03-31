package com.learn4.data.dto;

import java.util.ArrayList;

public class LearningObjective {
    String  id;
    String title;
    ArrayList<Subclass> subclasses;


    public LearningObjective(String id, String title, ArrayList<Subclass> subclasses) {
        this.id = id;
        this.title = title;
        this.subclasses = subclasses;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Subclass> getSubclasses() {
        return subclasses;
    }

    public void setSubclasses(ArrayList<Subclass> subclasses) {
        this.subclasses = subclasses;
    }




}
