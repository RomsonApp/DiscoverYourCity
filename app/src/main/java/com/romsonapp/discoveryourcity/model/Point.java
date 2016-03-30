package com.romsonapp.discoveryourcity.model;


public class Point{
    private int id;
    private String title;
    private String image;

    public Point(int id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    public Point(String title, String image) {
        super();
        this.title = title;
        this.image = image;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}