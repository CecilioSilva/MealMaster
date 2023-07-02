package me.ceciliosilva.ipass.mealmaster.model;

import me.ceciliosilva.ipass.mealmaster.exceptions.PasswordIncorrectException;
import me.ceciliosilva.ipass.mealmaster.exceptions.UserDoesNotExistException;
import me.ceciliosilva.ipass.mealmaster.utils.DataHelper;
import me.ceciliosilva.ipass.mealmaster.utils.Logger;

import java.io.Serializable;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Objects;

@SuppressWarnings("unused")
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

    public static boolean registerUser(String name, String email, String password, boolean seed) {
        Logger.info("User", "Registering user - ", email);
        loadUsers();
        User newUser = new User(name, email, password, new ArrayList<>());

        // Checks if the users array contains the user
        if (users.contains(newUser)) {
            Logger.warning("User", "User already exists - ", email);
            return false;
        }

        // Seed the new user with a dummy recipe
        if(seed){
            newUser.seed();
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

    // region Seed new User
    public void seed() {
        // adds default data to the user

        // region Brownie recipe

        // region ingredients
        Ingredient butter = new Ingredient("De Zaanse Hoeve Roomboter ongezouten",
                "Ongezouten roomboter bevat 81% melkvet.",
                "https://static.ah.nl/dam/product/AHI_43545239363237373934?revLabel=2&rendition=800x800_JPG_Q90&fileType=binary");
        this.addIngredient(butter);

        Ingredient chocolate = new Ingredient("AH Tablet melk", "Melkchocolade",
                "https://static.ah.nl/dam/product/AHI_43545239373733353534?revLabel=1&rendition=800x800_JPG_Q90&fileType=binary");
        this.addIngredient(chocolate);

        Ingredient sugar = new Ingredient("AH Kristalsuiker", "Kristalsuiker",
                "https://static.ah.nl/dam/product/AHI_43545239383337393137?revLabel=1&rendition=800x800_JPG_Q90&fileType=binary");
        this.addIngredient(sugar);

        Ingredient vanilla = new Ingredient("Dr. Oetker Vanille aroma",
                "Dr. Oetker vanille aroma verfijnt gebak en desserts en zorgt voor een heerlijk zachte vanillesmaak.",
                "https://static.ah.nl/dam/product/AHI_43545239373533333733?revLabel=2&rendition=800x800_JPG_Q90&fileType=binary");
        this.addIngredient(vanilla);

        Ingredient eggs = new Ingredient("AH Verse scharreleieren M", "Scharreleieren",
                "https://static.ah.nl/dam/product/AHI_43545239393130333236?revLabel=1&rendition=800x800_JPG_Q90&fileType=binary");
        this.addIngredient(eggs);

        Ingredient salt = new Ingredient("Drogheria Middellandse zeezout",
                "Drogheria & Alimentari Middellandse zeezout heeft een heel eigen aroma en een veel mildere smaak dan gewoon tafelzout. Het wordt gewonnen uit de Middellandse zee en bevat veel calcium en magnesium.",
                "https://static.ah.nl/dam/product/AHI_43545239383533353231?revLabel=1&rendition=800x800_JPG_Q90&fileType=binary");
        this.addIngredient(salt);

        Ingredient flour = new Ingredient("AH Tarwe bloem", "Tarwebloem",
                "https://static.ah.nl/dam/product/AHI_43545239363630333030?revLabel=1&rendition=800x800_JPG_Q90&fileType=binary");
        this.addIngredient(flour);
        // endregion

        // region meal
        Meal brownie = new Meal(
                "Best Homemade Brownie",
                "https://lh5.ggpht.com/RIu0OGCdq4C8SxHvabkWbP7ZElMX7wiUSfBNmvQwvkP2wzlJMS1hMzIoDRwmLOB7OMoVbPkvKxrtG7_gIW3j=w640-h640-c-rw-v1-e365",
                4,
                """
                        1. To prepare the pan, butter a 9x9 square pan and line the bottom with parchment paper. Butter the bottom again and lightly dust the pan with chocolate or cocoa powder.
                        
                        2. Preheat the oven to 400 degrees.
                        
                        3. Melt the chocolate and the butter in a small saucepan over low heat, stirring constantly until the mixture is smooth and glossy. Remove from heat and stir in the vanilla.
                        
                        4. Beat the eggs and salt in a stand mixer. Add the sugar and beat on high for about 10 minutes, until the mixture has turned quite white. Add the chocolate mixture to the eggs, beating on low until just mixed.
                        
                        5. Gently stir in the flour until it just disappears.
                        
                        6. Pour the batter into the prepared pan, place in the middle of the oven and immediately turn the temperature down to 350 degrees. Bake for 40 minutes; the brownies will be quite fudgy and a toothpick should come out not quite clean. Cool on a rack.
                        
                        7. Invert the pan, remove the parchment paper and invert again onto a cutting surface. Cut into squares."""
                );

        brownie.addIngredient(new MealIngredient(151, false, MeasurementUnit.gram, butter));
        brownie.addIngredient(new MealIngredient(142, false, MeasurementUnit.gram, chocolate));
        brownie.addIngredient(new MealIngredient(400, false, MeasurementUnit.gram, sugar));
        brownie.addIngredient(new MealIngredient(12, false, MeasurementUnit.milliliter, vanilla));
        brownie.addIngredient(new MealIngredient(4, false, MeasurementUnit.count, eggs));
        brownie.addIngredient(new MealIngredient(3, false, MeasurementUnit.gram, salt));
        brownie.addIngredient(new MealIngredient(125, false, MeasurementUnit.gram, flour));
        this.addMeal(brownie);
        // endregion

        // region Shopping-list
        ShoppingList shoppingList = new ShoppingList(false, "Deserts");
        Weekday tomorrow = new Weekday(LocalDate.now(), brownie);
        shoppingList.addWeekMeal(tomorrow);

        this.addShoppingList(shoppingList);
        // endregion

        // endregion
    }
    // endregion
}
