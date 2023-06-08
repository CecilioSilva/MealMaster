package me.ceciliosilva.ipass.mealmaster.model;

import java.io.Serializable;

public class Meal implements Serializable {
    private Integer id;
    private String name;
    private String image;
    private String numberOfPeople;
    private String description;

    public Meal(Integer id, String name, String image, String numberOfPeople, String description) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.numberOfPeople = numberOfPeople;
        this.description = description;
    }
}
