package me.ceciliosilva.ipass.mealmaster.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Weekday implements Serializable {
    static final long serialVersionUID = 1L;

    private Date date;
    private String description;
    private ArrayList<Meal> meals;

    public Weekday(Date date, String description) {
        this.date = date;
        this.description = description;
    }

    public void addMeal(Meal meal) {
        // Adds a meal to the weekday
        meals.add(meal);
    }
}
