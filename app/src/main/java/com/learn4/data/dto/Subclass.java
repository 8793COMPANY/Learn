package com.learn4.data.dto;

public class Subclass {

    String id;
    String title;
    String learning_objective;
    String condition;

    public Subclass(String id, String title, String learning_objective, String condition) {
        this.id = id;
        this.title = title;
        this.learning_objective = learning_objective;
        this.condition = condition;
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

    public String getLearning_objective() {
        return learning_objective;
    }

    public void setLearning_objective(String learning_objective) {
        this.learning_objective = learning_objective;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }





}
