package me.ceciliosilva.ipass.mealmaster.webservices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("")
public class ApiResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getStatus() {
        // Route to check if api is online
        return "OK";
    }
}
