package me.ceciliosilva.ipass.mealmaster.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class ShoppingList implements Serializable {
    static final long serialVersionUID = 1L;
    private String name;
    private final String id;
    private boolean isPublic;
    private ArrayList<Weekday> days = new ArrayList<>();

    public ShoppingList(boolean isPublic, String name) {
        this.isPublic = isPublic;
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public void addWeekMeal(Weekday weekmeal) {
        for (Weekday weekday : days) {
            if (weekday.getDate().equals(weekmeal.getDate()) && weekday.getMeal().equals(weekmeal.getMeal())) {
                return;
            }
        }

        this.days.add(weekmeal);
    }

    public ArrayList<Weekday> getMeals() {
        return (ArrayList<Weekday>) this.days.clone();
    }

    public HashMap<String, Object> toMap() {
        HashMap<LocalDate, HashMap<String, Object>> weekdayMap = new HashMap<>();

        for (Weekday day : getMeals()) {
            weekdayMap.put(day.getDate(), day.getMeal().toMap());
        }

        HashMap<String, Object> shoppingListMap = new HashMap<>();

        shoppingListMap.put("name", this.name);
        shoppingListMap.put("id", this.id);
        shoppingListMap.put("isPublic", this.isPublic);
        shoppingListMap.put("days", weekdayMap);

        return shoppingListMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ShoppingList shoppingList = (ShoppingList) o;
        return Objects.equals(id, shoppingList.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setPublic(Boolean status) {
        this.isPublic = status;
    }

    public boolean getIsPublic() {
        return this.isPublic;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    };

}
