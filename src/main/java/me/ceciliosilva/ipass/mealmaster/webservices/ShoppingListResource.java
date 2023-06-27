package me.ceciliosilva.ipass.mealmaster.webservices;

import me.ceciliosilva.ipass.mealmaster.model.*;
import me.ceciliosilva.ipass.mealmaster.utils.ApiHelper;

import javax.annotation.security.RolesAllowed;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.Console;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Path("shopping-list")
public class ShoppingListResource {
    @GET
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllShoppingLists(@Context SecurityContext sc){
        // Route for getting all users shopping lists

        // Gets the user from the security context
        if (sc.getUserPrincipal() instanceof User current) {
            // Converts the shoppinglist to a list of maps
            ArrayList<HashMap<String, Object>> shoppingListList = new ArrayList<>();

            for (ShoppingList shoppingList : current.getShoppingLists()) {
                shoppingListList.add(shoppingList.toMap());
            }

            // Returns the list of shoppingLists
            return Response.ok(shoppingListList).build();
        }

        // Returns an error if the user is not found
        return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, "Cant find user");
    }

    @POST
    @Path("add")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postShoppingList(@Context SecurityContext sc, String requestStr) {
        // Route for adding a shopping list

        if (sc.getUserPrincipal() instanceof User current) {
            try {
                // Converts the request body to a json object
                JsonObject requestBody = ApiHelper.requestBodyReader(requestStr);
                String name = requestBody.getString("name");

                // Checks if the name is valid
                if (name.length() < 1) {
                    return ApiHelper.simpleMsgResponse(Response.Status.BAD_REQUEST, "Name is invalid");
                }

                // Checks if the description is valid
                ShoppingList shoppingList = new ShoppingList(false, name);

                // Adds the meal to the Shopping-list
                JsonArray days = requestBody.getJsonArray("days");
                for (JsonValue day: days) {
                    JsonObject jsn = day.asJsonObject();
                    LocalDate date = LocalDate.parse(jsn.getString("date"));
                    String id = jsn.getString("id");
                    Meal meal = current.getMealById(id);

                    if(meal != null){
                        Weekday weekday = new Weekday(date, meal);
                        shoppingList.addWeekMeal(weekday);
                    }
                }

                // Adds the shopping-list to the user
                current.addShoppingList(shoppingList);

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
    public Response deleteShoppingList(@Context SecurityContext sc, @PathParam("id") String id) {
        // Route for deleting a shoppinglist

        // Gets the user from the security context
        if (sc.getUserPrincipal() instanceof User current) {
            try {
                // Deletes the shoppinglist
                current.removeShoppingList(id);

                // Returns the status code and a message
                return ApiHelper.simpleMsgResponse(Response.Status.OK, "Deleted Shoppinglist");
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
    public Response getShoppingListById(@Context SecurityContext sc, @PathParam("id") String id) {
        // Route for getting a shopping list

        // Gets the user from the security context
        if (sc.getUserPrincipal() instanceof User current) {
            try {
                // Gets the shopping list from user
                ShoppingList shoppingList = current.getShoppingListById(id);

                // Returns the status code and a message
                return Response.ok(shoppingList.toMap()).build();
            } catch (Exception err) {
                // Returns an error if something went wrong
                return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, err.getMessage());
            }
        }

        // Returns an error if the user is not found
        return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, "Cant find user");
    }

    @GET
    @Path("/public/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPublicShoppingListById(@PathParam("id") String id) {
        // Route for getting a shopping list

        try {
            // Gets the shopping list from user
            ShoppingList shoppingList = User.searchShoppingList(id);

            if(shoppingList == null){
                return ApiHelper.simpleMsgResponse(Response.Status.BAD_REQUEST, "Shopping list does not exit");
            }

            // Returns the status code and a message
            return Response.ok(shoppingList.toMap()).build();
        } catch (Exception err) {
            // Returns an error if something went wrong
            return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, err.getMessage());
        }
    }
}
