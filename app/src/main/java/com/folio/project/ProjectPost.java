package com.folio.project;

public class ProjectPost {
    private String title, description;

    public ProjectPost(String title, String description){
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
