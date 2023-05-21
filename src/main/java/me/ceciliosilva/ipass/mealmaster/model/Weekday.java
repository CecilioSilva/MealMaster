package me.ceciliosilva.ipass.mealmaster.model;

import java.util.ArrayList;
import java.util.Date;

public class Weekday {
    private Date date;
    private String description;
    private ArrayList<Meal> meals;

    public Weekday(Date date, String description) {
        this.date = date;
        this.description = description;
    }

    public void addMeal(Meal meal) {
        meals.add(meal);
    }
}
