package me.ceciliosilva.ipass.mealmaster.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ShoppingList implements Serializable {
    private boolean isPublic;
    private ArrayList<Weekday> days = new ArrayList<>();

    public ShoppingList(boolean isPublic) {
        this.isPublic = isPublic;
    }
}
