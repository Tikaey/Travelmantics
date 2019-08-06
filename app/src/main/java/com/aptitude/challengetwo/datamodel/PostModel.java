package com.aptitude.challengetwo.datamodel;

import androidx.annotation.NonNull;

public class PostModel {

    private String title;
    private Double cost;
    private String description;
    private String imageString;

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public Double getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }
}
