package me.ceciliosilva.ipass.mealmaster.model;

import java.util.ArrayList;

public class User {
    private String name;
    private String email;
    private String password;
    private ArrayList<Integer> favorites;
    private ArrayList<ShoppingList> shoppingLists;

    public User(String name, String email, String password, ArrayList<Integer> favorites) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.favorites = favorites;
    }

    public void addShoppingList(ShoppingList shoppingList) {
        shoppingLists.add(shoppingList);
    }
}
