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
        if(sc.getUserPrincipal() instanceof User current){
            ArrayList<HashMap<String, Object>> meals = new ArrayList<>();
            for(Meal meal: current.getMeals()){
                meals.add(meal.toMap());
            }
            return Response.ok(meals).build();
        }

        return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, "Cant find user");
    }

    @POST
    @Path("add")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postMeal(@Context SecurityContext sc, String requestStr) {
        if(sc.getUserPrincipal() instanceof User current){
            try {
                JsonObject requestBody = ApiHelper.requestBodyReader(requestStr);

                String image = requestBody.getString("image");
                String name = requestBody.getString("name");
                String description = requestBody.getString("description");
                Integer numberOfPeople = requestBody.getInt("numberOfPeople");


                if(name.length() < 1){
                    return ApiHelper.simpleMsgResponse(Response.Status.BAD_REQUEST, "Name is invalid");
                }

                Meal meal = new Meal(name, image, numberOfPeople, description);

                JsonArray ingredients = requestBody.getJsonArray("ingredients");
                ingredients.forEach(jsonValue -> {
                    JsonObject jsn = jsonValue.asJsonObject();
                    meal.addIngredient(new MealIngredient(
                            jsn.getJsonNumber("amount").doubleValue(),
                            jsn.getBoolean("bought"),
                            MeasurementUnit.valueOf(jsn.getString("measurementUnit")),
                            current.getIngredientById(jsn.getString("ingredient"))
                    ));
                });

                current.addMeal(meal);

                return ApiHelper.simpleMsgResponse(Response.Status.OK, "Added Meal");
            } catch (Exception err) {
                return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, err.getMessage());
            }
        }

        return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, "Cant find user");
    }

    @DELETE
    @Path("delete/{id}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMeal(@Context SecurityContext sc, @PathParam("id") String id){
        if(sc.getUserPrincipal() instanceof User current){
            try {
                current.removeMeal(id);

                return ApiHelper.simpleMsgResponse(Response.Status.OK, "Deleted Meal");
            } catch (Exception err) {
                return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, err.getMessage());
            }
        }

        return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, "Cant find user");
    }
}
