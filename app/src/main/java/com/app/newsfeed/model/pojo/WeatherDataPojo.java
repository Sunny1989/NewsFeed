package com.app.newsfeed.model.pojo;

import java.util.List;

public class WeatherDataPojo {
    public int id;
    public String dt;
    public Clouds clouds;
    public Coord coord;
    public Wind wind;
    public String cod;
    public Sys sys;
    public String name;
    public List<Weather> weather;
    public Rain rain;
    public Main main;
}
