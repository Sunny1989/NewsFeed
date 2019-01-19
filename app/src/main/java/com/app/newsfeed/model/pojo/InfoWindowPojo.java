package com.app.newsfeed.model.pojo;

public class InfoWindowPojo {
    public String name;
    public String description;
    public String temperature;
    public String wind;

    public InfoWindowPojo(String name, String description, String temperature, String wind) {
        this.name = name;
        this.description = description;
        this.temperature = temperature;
        this.wind = wind;
    }
}
