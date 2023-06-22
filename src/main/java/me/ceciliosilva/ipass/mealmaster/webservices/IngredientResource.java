package me.ceciliosilva.ipass.mealmaster.webservices;

import me.ceciliosilva.ipass.mealmaster.model.Ingredient;
import me.ceciliosilva.ipass.mealmaster.model.User;
import me.ceciliosilva.ipass.mealmaster.utils.ApiHelper;

import javax.annotation.security.RolesAllowed;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.HashMap;

@Path("ingredients")
public class IngredientResource {

    @GET
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllIngredients(@Context SecurityContext sc) {
        // Route for getting all ingredients

        // Gets the user from the security context
        if (sc.getUserPrincipal() instanceof User current) {

            // Converts the ingredients to a list of maps
            ArrayList<HashMap<String, String>> ingredients = new ArrayList<>();
            for (Ingredient ing : current.getIngredients()) {
                ingredients.add(ing.toMap());
            }

            // Returns the list of ingredients
            return Response.ok(ingredients).build();
        }

        // Returns an error if the user is not found
        return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, "Cant find user");
    }

    @POST
    @Path("add")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postIngredient(@Context SecurityContext sc, String requestStr) {
        // Route for adding an ingredient

        // Gets the user from the security context
        if (sc.getUserPrincipal() instanceof User current) {
            try {
                // Converts the request body to a json object
                JsonObject requestBody = ApiHelper.requestBodyReader(requestStr);

                String name = requestBody.getString("name");
                String description = requestBody.getString("description");
                String image = requestBody.getString("image");

                // Checks if the name is valid
                if (name.length() < 1) {
                    return ApiHelper.simpleMsgResponse(Response.Status.BAD_REQUEST, "Name is invalid");
                }

                // Creates the ingredient and adds it to the user
                Ingredient ing = new Ingredient(name, description, image);
                current.addIngredient(ing);

                // Returns a success message
                return ApiHelper.simpleMsgResponse(Response.Status.OK, "Added ingredient");
            } catch (Exception err) {
                // Returns an error message if something went wrong
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
    public Response deleteIngredient(@Context SecurityContext sc, @PathParam("id") String id) {
        // Route for deleting an ingredient

        // Gets the user from the security context
        if (sc.getUserPrincipal() instanceof User current) {
            try {
                // Removes the ingredient from the user
                current.removeIngredient(id);

                // Returns a success message
                return ApiHelper.simpleMsgResponse(Response.Status.OK, "Deleted ingredient");
            } catch (Exception err) {
                // Returns an error message if something went wrong
                return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, err.getMessage());
            }
        }
        // Returns an error if the user is not found
        return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, "Cant find user");
    }
}
