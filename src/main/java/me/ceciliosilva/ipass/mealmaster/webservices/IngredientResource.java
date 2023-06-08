package me.ceciliosilva.ipass.mealmaster.webservices;

import me.ceciliosilva.ipass.mealmaster.model.User;
import me.ceciliosilva.ipass.mealmaster.utils.ApiHelper;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("ingredients")
public class IngredientResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public Response getAllIngredients(@Context SecurityContext sc) {
        if(sc.getUserPrincipal() instanceof User){
            User current = (User) sc.getUserPrincipal();
            return ApiHelper.simpleMsgResponse(Response.Status.OK, "It works! " + current.getEmail());
        }

        return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, "Not authenticated");
    }
}
