package me.ceciliosilva.ipass.mealmaster.model;

import me.ceciliosilva.ipass.mealmaster.exceptions.PasswordIncorrectException;
import me.ceciliosilva.ipass.mealmaster.exceptions.UserDoesNotExistException;
import me.ceciliosilva.ipass.mealmaster.utils.DataHelper;
import me.ceciliosilva.ipass.mealmaster.utils.Logger;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Objects;

public class User implements Serializable, Principal {
    static final long serialVersionUID = 1L;
    private static String saveFileName = "users.obj";
    private String name;
    private String email;
    private String password;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private ArrayList<Meal> meals = new ArrayList<>();
    private ArrayList<ShoppingList> shoppingLists = new ArrayList<>();
    private ArrayList<String> favorites;

    private static ArrayList<User> users;

    public User(String name, String email, String password, ArrayList<String> favorites) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.favorites = favorites;
    }

    public static User getUserByEmail(String email) {
        for (User user : users) {
            if (user.email.equals(email)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public static void loadUsers() {
        // Loads users array from data storage
        users = DataHelper.loadObject(saveFileName, new ArrayList<>());
    }

    public static void saveUsers() {
        // Serializes the users array to data storage
        DataHelper.saveObject(saveFileName, users);
    }

    public static void clearUsers() {
        // Clears the users array
        users = new ArrayList<>();
        saveUsers();
    };

    public static void setSaveFileName(String saveFileName) {
        User.saveFileName = saveFileName;
    }

    public static ArrayList<User> getUsers() {
        return new ArrayList<User>(users);
    }

    public static boolean registerUser(String name, String email, String password) {
        Logger.info("User", "Registering user - ", email);
        loadUsers();
        User newUser = new User(name, email, password, new ArrayList<>());

        // Checks if the users array contains the user
        if (users.contains(newUser)) {
            Logger.warning("User", "User already exists - ", email);
            return false;
        }

        // If the user doesn't exist add it to the users array and save the array
        users.add(newUser);
        saveUsers();
        return true;
    }

    public static User authenticateUser(String email, String password)
            throws PasswordIncorrectException, UserDoesNotExistException {
        Logger.info("User", "Logging in user - ", email);
        loadUsers();

        // Checks every user if the email and password match any of the existing users
        for (User user : users) {

            // If the email is the same the user exists
            if (user.email.equals(email)) {

                // Checks if the password matches
                if (user.password.equals(password)) {
                    return user;
                }
                throw new PasswordIncorrectException();
            }
        }
        throw new UserDoesNotExistException();
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    // region Ingredients
    public ArrayList<Ingredient> getIngredients() {
        return new ArrayList<Ingredient>(this.ingredients);
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        saveUsers();
    }

    public void removeIngredient(String id) {
        ArrayList<Ingredient> filteredIngredients = new ArrayList<>();

        for (Ingredient ing : this.ingredients) {
            if (!ing.getId().equals(id)) {
                filteredIngredients.add(ing);
            }
        }

        this.ingredients = filteredIngredients;
        saveUsers();
    }

    public Ingredient getIngredientById(String ingredientId) {
        for (Ingredient ing : this.ingredients) {
            if (ing.getId().equals(ingredientId)) {
                return ing;
            }
        }
        return null;
    }
    // endregion

    // region Meal
    public ArrayList<Meal> getMeals() {
        return new ArrayList<Meal>(this.meals);
    }

    public void addMeal(Meal meal) {
        this.meals.add(meal);
        saveUsers();
    }

    public void removeMeal(String id) {
        ArrayList<Meal> filteredMeals = new ArrayList<>();

        for (Meal meal : this.meals) {
            if (!meal.getId().equals(id)) {
                filteredMeals.add(meal);
            }
        }

        this.meals = filteredMeals;
        saveUsers();
    }

    public Meal getMealById(String id) {
        for (Meal meal : this.meals) {
            if (meal.getId().equals(id)) {
                return meal;
            }
        }

        return null;
    }
    // endregion

    // region Shopping lists
    public ArrayList<ShoppingList> getShoppingLists() {
        return new ArrayList<ShoppingList>(this.shoppingLists);
    }

    public void addShoppingList(ShoppingList list) {

        if (this.shoppingLists == null) {
            this.shoppingLists = new ArrayList<>();
        }

        this.shoppingLists.add(list);
        saveUsers();
    }

    public void removeShoppingList(String id) {
        ArrayList<ShoppingList> filteredShoppingLists = new ArrayList<>();

        for (ShoppingList shoppingList : this.shoppingLists) {
            if (!shoppingList.getId().equals(id)) {
                filteredShoppingLists.add(shoppingList);
            }
        }

        this.shoppingLists = filteredShoppingLists;
        saveUsers();
    }

    public ShoppingList getShoppingListById(String id) {
        for (ShoppingList shoppingList : this.shoppingLists) {
            if (shoppingList.getId().equals(id)) {
                return shoppingList;
            }
        }

        return null;
    }

    public static ShoppingList searchShoppingList(String id) {
        for (User user : users) {
            ShoppingList shoppingList = user.getShoppingListById(id);

            // If shopping list exists return it
            if (shoppingList != null) {
                // If shopping list is public return it
                if (shoppingList.getIsPublic()) {
                    return shoppingList;
                }
                return null;
            }
        }
        return null;
    }
    // endregion
}
