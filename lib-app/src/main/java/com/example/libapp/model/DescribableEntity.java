package com.example.libapp.model;

public abstract class DescribableEntity extends BaseEntity {
    protected String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


