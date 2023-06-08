package me.ceciliosilva.ipass.mealmaster.model;

import java.io.Serializable;

public class ShoppingList implements Serializable {
    private boolean isPublic;

    public ShoppingList(boolean isPublic) {
        this.isPublic = isPublic;
    }
}
