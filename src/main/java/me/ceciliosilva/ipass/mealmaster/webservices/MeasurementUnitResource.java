package me.ceciliosilva.ipass.mealmaster.webservices;

import me.ceciliosilva.ipass.mealmaster.model.MeasurementUnit;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("unit")
public class MeasurementUnitResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUnits() {
        // Route for getting all measurement units

        // Returns all measurement units as a json array of strings (the enum values)
        return Response.ok(MeasurementUnit.values()).build();
    }

}
