package me.ceciliosilva.ipass.mealmaster.model;

import me.ceciliosilva.ipass.mealmaster.exceptions.PasswordIncorrectException;
import me.ceciliosilva.ipass.mealmaster.exceptions.UserDoesNotExistException;
import me.ceciliosilva.ipass.mealmaster.utils.DataHelper;
import me.ceciliosilva.ipass.mealmaster.utils.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class User implements Serializable {
    private static String saveFileName = "users.obj";
    private String name;
    private String email;
    private String password;
    private ArrayList<Integer> favorites;
    private ArrayList<ShoppingList> shoppingLists;

    private static ArrayList<User> users;

    public User(String name, String email, String password, ArrayList<Integer> favorites) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.favorites = favorites;
    }

    public void addShoppingList(ShoppingList shoppingList) {
        shoppingLists.add(shoppingList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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

    public static void loadUsers(){
        // Loads users array from data storage
        users = DataHelper.loadObject(saveFileName, new ArrayList<>());
    }

    public static void saveUsers(){
        // Serializes the users array to data storage
        DataHelper.saveObject(saveFileName, users);
    }

    public static ArrayList<User> getUsers(){
        loadUsers();
        return users;
    }

    public static boolean registerUser(String name, String email, String password){
        Logger.info("User", "Registering user - ", email);
        loadUsers();
        User newUser = new User(name, email, password, new ArrayList<>());

        // Checks if the users array contains the user
        if(users.contains(newUser)){
            Logger.warning("User", "User already exists - ", email);
            return false;
        }

        // If the user doesn't exist add it to the users array and save the array
        users.add(newUser);
        saveUsers();
        return true;
    }

    public static User authenticateUser(String email, String password) throws PasswordIncorrectException, UserDoesNotExistException {
        Logger.info("User", "Logging in user - ", email);
        loadUsers();

        // Checks every user if the email and password match any of the existing users
        for (User user: users){

            // If the email is the same the user exists
            if(user.email.equals(email)){

                // Checks if the password matches
                if(user.password.equals(password)){
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
}
