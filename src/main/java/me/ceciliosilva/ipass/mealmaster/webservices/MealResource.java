package me.ceciliosilva.ipass.mealmaster.webservices;

import me.ceciliosilva.ipass.mealmaster.model.*;
import me.ceciliosilva.ipass.mealmaster.utils.ApiHelper;

import javax.annotation.security.RolesAllowed;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.HashMap;

@Path("meals")
public class MealResource {

    @GET
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMeals(@Context SecurityContext sc) {
        // Route for getting all meals

        // Gets the user from the security context
        if (sc.getUserPrincipal() instanceof User current) {
            // Converts the meal to a list of maps
            ArrayList<HashMap<String, Object>> meals = new ArrayList<>();
            for (Meal meal : current.getMeals()) {
                meals.add(meal.toMap());
            }

            // Returns the list of meals
            return Response.ok(meals).build();
        }

        // Returns an error if the user is not found
        return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, "Cant find user");
    }

    @POST
    @Path("add")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postMeal(@Context SecurityContext sc, String requestStr) {
        // Route for adding an meal
        if (sc.getUserPrincipal() instanceof User current) {
            try {
                // Converts the request body to a json object
                JsonObject requestBody = ApiHelper.requestBodyReader(requestStr);

                String image = requestBody.getString("image");
                String name = requestBody.getString("name");
                String description = requestBody.getString("description");
                Integer numberOfPeople = requestBody.getInt("numberOfPeople");

                // Checks if the name is valid
                if (name.length() < 1) {
                    return ApiHelper.simpleMsgResponse(Response.Status.BAD_REQUEST, "Name is invalid");
                }

                // Checks if the description is valid
                Meal meal = new Meal(name, image, numberOfPeople, description);

                // Adds the ingredients to the meal
                JsonArray ingredients = requestBody.getJsonArray("ingredients");
                ingredients.forEach(jsonValue -> {
                    JsonObject jsn = jsonValue.asJsonObject();
                    meal.addIngredient(new MealIngredient(
                            jsn.getJsonNumber("amount").doubleValue(),
                            false,
                            MeasurementUnit.valueOf(jsn.getString("unit")),
                            current.getIngredientById(jsn.getString("id"))));
                });

                // Adds the meal to the user
                current.addMeal(meal);

                // Returns the status code and a message
                return ApiHelper.simpleMsgResponse(Response.Status.OK, "Added Meal");
            } catch (Exception err) {
                // Returns an error if something went wrong
                return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, err.getMessage());
            }
        }

        // Returns an error if the user is not found
        return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, "Cant find user");
    }

    @DELETE
    @Path("delete/{id}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMeal(@Context SecurityContext sc, @PathParam("id") String id) {
        // Route for deleting an meal

        // Gets the user from the security context
        if (sc.getUserPrincipal() instanceof User current) {
            try {
                // Deletes the meal
                current.removeMeal(id);

                // Returns the status code and a message
                return ApiHelper.simpleMsgResponse(Response.Status.OK, "Deleted Meal");
            } catch (Exception err) {
                // Returns an error if something went wrong
                return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, err.getMessage());
            }
        }

        // Returns an error if the user is not found
        return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, "Cant find user");
    }

    @GET
    @Path("{id}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMealById(@Context SecurityContext sc, @PathParam("id") String id) {
        // Route for deleting an meal

        // Gets the user from the security context
        if (sc.getUserPrincipal() instanceof User current) {
            try {
                // Deletes the meal
                Meal meal = current.getMealById(id);

                // Returns the status code and a message
                return Response.ok(meal.toMap()).build();
            } catch (Exception err) {
                // Returns an error if something went wrong
                return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, err.getMessage());
            }
        }

        // Returns an error if the user is not found
        return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, "Cant find user");
    }
}
