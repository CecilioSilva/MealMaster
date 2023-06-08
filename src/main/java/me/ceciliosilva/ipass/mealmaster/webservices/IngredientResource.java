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
        if(sc.getUserPrincipal() instanceof User current){

            ArrayList<HashMap<String, String>> ingredients = new ArrayList<>();
            for(Ingredient ing: current.getIngredients()){
                ingredients.add(ing.toMap());
            }

            return Response.ok(ingredients).build();
        }

        return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, "Cant find user");
    }

    @POST
    @Path("add")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postIngredient(@Context SecurityContext sc, String requestStr){
        if(sc.getUserPrincipal() instanceof User current){
            try {
                JsonObject requestBody = ApiHelper.requestBodyReader(requestStr);

                String name = requestBody.getString("name");
                String description = requestBody.getString("description");
                String image = requestBody.getString("image");

                if(name.length() < 1){
                    return ApiHelper.simpleMsgResponse(Response.Status.BAD_REQUEST, "Name is invalid");
                }

                Ingredient ing = new Ingredient(name, description, image);

                current.addIngredient(ing);

                return ApiHelper.simpleMsgResponse(Response.Status.OK, "Added ingredient");
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
    public Response deleteIngredient(@Context SecurityContext sc, @PathParam("id") String id){
        if(sc.getUserPrincipal() instanceof User current){
            try {
                current.removeIngredient(id);

                return ApiHelper.simpleMsgResponse(Response.Status.OK, "Deleted ingredient");
            } catch (Exception err) {
                return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, err.getMessage());
            }
        }
        return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, "Cant find user");
    }
}
