package me.ceciliosilva.ipass.mealmaster.utils;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.ws.rs.core.Response;
import java.io.StringReader;

public class ApiHelper {

    public static Response simpleMsgResponse(Response.Status status, String msg) {
        // Creates a default response object for the api
        JsonObjectBuilder responseObject = Json.createObjectBuilder();
        responseObject.add("msg", msg);

        return Response.status(status).entity(responseObject.build().toString()).build();
    }

    public static JsonObject requestBodyReader(String requestBody) {
        // Converts Json String to java json object
        StringReader strReader = new StringReader(requestBody);
        JsonReader jsonReader = Json.createReader(strReader);

        return jsonReader.readObject();
    }

}
