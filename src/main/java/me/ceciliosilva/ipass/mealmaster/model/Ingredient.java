package me.ceciliosilva.ipass.mealmaster.model;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Ingredient implements Serializable {

    private String id;
    private String name;
    private String description;
    private String image;

    public Ingredient(String name, String description, String image) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getId() {
        return this.id;
    }

    public HashMap<String, String> toMap(){
        HashMap<String, String> map = new HashMap<>();

        map.put("id", this.id);
        map.put("name", this.name);
        map.put("description", this.description);
        map.put("image", this.description);
        return map;
    }
}
