package com.learn4.data.dto;

public class Project {
    public int id;
    public String projectName;
    public String contents_list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getContents_list() {
        return contents_list;
    }

    public void setContents_list(String contents_list) {
        this.contents_list = contents_list;
    }



    public Project(int id, String projectName, String contents_list) {
        this.id = id;
        this.projectName = projectName;
        this.contents_list = contents_list;
    }


}
