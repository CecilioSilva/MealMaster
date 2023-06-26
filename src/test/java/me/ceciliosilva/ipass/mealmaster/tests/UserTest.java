package me.ceciliosilva.ipass.mealmaster.tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.ceciliosilva.ipass.mealmaster.exceptions.PasswordIncorrectException;
import me.ceciliosilva.ipass.mealmaster.exceptions.UserDoesNotExistException;
import me.ceciliosilva.ipass.mealmaster.model.Ingredient;
import me.ceciliosilva.ipass.mealmaster.model.Meal;
import me.ceciliosilva.ipass.mealmaster.model.MealIngredient;
import me.ceciliosilva.ipass.mealmaster.model.MeasurementUnit;
import me.ceciliosilva.ipass.mealmaster.model.ShoppingList;
import me.ceciliosilva.ipass.mealmaster.model.User;
import me.ceciliosilva.ipass.mealmaster.model.Weekday;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class UserTest {

    private User user;
    private Ingredient ingredient;
    private Meal meal;
    private ShoppingList shoppingList;

    @BeforeAll
    public static void reset() {
        User.setSaveFileName("test-users.obj");
        User.clearUsers();
    }

    @BeforeEach
    public void setup() {
        User.clearUsers();
        User.registerUser("test", "test@gmail.com", "test");
        user = User.getUserByEmail("test@gmail.com");

        ingredient = new Ingredient("ingredient", "test ingredient", "https://www.test.com");
        user.addIngredient(ingredient);

        meal = new Meal("meal", "https://www.test.com", 4, "test meal");
        meal.addIngredient(new MealIngredient(100, false, MeasurementUnit.gram, ingredient));
        user.addMeal(meal);

        Weekday weekday = new Weekday(LocalDate.now(), meal);
        shoppingList = new ShoppingList(false, "test list");
        shoppingList.addWeekMeal(weekday);

        user.addShoppingList(shoppingList);
    }

    @Test
    public void getUserByEmail() {
        // Tests if the user is returned by email
        User user2 = User.getUserByEmail("test@gmail.com");
        assertEquals(user, user2);
    }

    @Test
    public void getUserByEmailNull() {
        // Tests if null is returned when the user does not exist
        User user2 = User.getUserByEmail("");
        assertNull(user2);
    }

    @Test
    public void userEquals() {
        // Tests if the user is equal to itself
        assertEquals(user, user);
    }

    @Test
    public void userEqualsNull() {
        // Tests if the user is not equal to null
        assertNotEquals(null, user);
    }

    @Test
    public void userToString() {
        // Tests if the user is equal to its string representation
        String userString = user.toString();
        assertEquals("User{name='test', email='test@gmail.com'}", userString);
    }

    @Test
    public void registerUser() {
        // Tests if the user is registered
        User.registerUser("test2", "test2@gmail.com", "test2");
        assertEquals(2, User.getUsers().size());
    }

    @Test
    public void registerExistingUser() {
        // Tests if the user is not registered if it already exists
        User.registerUser("test", "test@gmail.com", "test");

        assertEquals(1, User.getUsers().size());
    }

    @Test
    public void authenticateUser() {
        // Tests if the user is authenticated
        try {
            User logedInUser = User.authenticateUser("test@gmail.com", "test");
            assertEquals("User{name='test', email='test@gmail.com'}", logedInUser.toString());
            return;
        } catch (PasswordIncorrectException | UserDoesNotExistException e) {
            fail();
        }
    }

    @Test
    public void authenticateUserWrongPassword() {
        // Tests if the user is not authenticated with the wrong password
        try {
            User.authenticateUser("test@gmail.com", "wrongpassword");
        } catch (PasswordIncorrectException e) {
            assertTrue(true);
            return;
        } catch (UserDoesNotExistException e) {
            fail();
            return;
        }
        fail();
    }

    @Test
    public void authenticateUserWrongEmail() {
        // Tests if the user is not authenticated with the wrong email
        try {
            User.authenticateUser("", "test");
        } catch (PasswordIncorrectException e) {
            fail();
            return;
        } catch (UserDoesNotExistException e) {
            assertTrue(true);
            return;
        }
        fail();
    }

    @Test
    public void getEmail() {
        // Tests if the user email is returned
        assertEquals("test@gmail.com", user.getEmail());
    }

    @Test
    public void getName() {
        // Tests if the user name is returned
        assertEquals("test", user.getName());
    }

    @Test
    public void getIngredients() {
        // Tests if the user ingredients are returned
        assertEquals(1, user.getIngredients().size());
    }

    @Test
    public void addIngredient() {
        // Tests if the ingredient is added
        user.addIngredient(new Ingredient("ingredient2", "test ingredient2", "https://www.test2.com"));
        assertEquals(2, user.getIngredients().size());
    }

    @Test
    public void removeIngredient() {
        // Tests if the ingredient is removed
        user.removeIngredient(ingredient.getId());
        assertEquals(0, user.getIngredients().size());
    }

    @Test
    public void getIngredientById() {
        // Tests if the ingredient is returned by id
        Ingredient getIngredient = user.getIngredientById(ingredient.getId());
        assertEquals(ingredient.getId(), getIngredient.getId());
    }

    @Test
    public void getMeals() {
        // Tests if the user meals are returned
        assertEquals(1, user.getMeals().size());
    }

    @Test
    public void addMeal() {
        // Tests if the meal is added
        user.addMeal(new Meal("meal2", "https://www.test2.com", 4, "test meal2"));
        assertEquals(2, user.getMeals().size());
    }

    @Test
    public void removeMeal() {
        // Tests if the meal is removed
        user.removeMeal(meal.getId());
        assertEquals(0, user.getMeals().size());
    }

    @Test
    public void getShoppingLists() {
        // Tests if the user shopping lists are returned
        assertEquals(1, user.getShoppingLists().size());
    }

    @Test
    public void addShoppingList() {
        // Tests if the shopping list is added
        user.addShoppingList(new ShoppingList(false, "test list2"));
        assertEquals(2, user.getShoppingLists().size());
    }

    @Test
    public void removeShoppingList() {
        // Tests if the shopping list is removed
        user.removeShoppingList(shoppingList.getId());
        assertEquals(0, user.getShoppingLists().size());
    }

    @Test
    public void getShoppingListById() {
        // Tests if the shopping list is returned by id
        ShoppingList getShoppingList = user.getShoppingListById(shoppingList.getId());
        assertEquals(shoppingList.getId(), getShoppingList.getId());
    }

    @Test
    public void getPublicShoppingLists() {
        // Tests if the user public shopping lists are returned
        assertEquals(null, User.searchShoppingList(shoppingList.getId()));
    }

    @Test
    public void searchShoppingList() {
        // Tests if the user shopping list is returned by id
        shoppingList.setPublic(true);
        ShoppingList getShoppingList = User.searchShoppingList(shoppingList.getId());
        assertEquals(shoppingList.getId(), getShoppingList.getId());
    }

}
