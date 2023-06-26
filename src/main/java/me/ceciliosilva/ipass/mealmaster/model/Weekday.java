package me.ceciliosilva.ipass.mealmaster.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Weekday implements Serializable {
    static final long serialVersionUID = 1L;

    private final LocalDate date;
    private final Meal meal;

    public Weekday(LocalDate date, Meal meal) {
        this.date = date;
        this.meal = meal;
    }

    public Meal getMeal() {
        return meal;
    }

    public LocalDate getDate() {
        return date;
    }

    public Boolean equals(Weekday weekday) {
        return this.date.equals(weekday.getDate()) && this.meal.equals(weekday.getMeal());
    }
}
