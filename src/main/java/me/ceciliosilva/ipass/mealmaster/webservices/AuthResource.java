package me.ceciliosilva.ipass.mealmaster.webservices;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import me.ceciliosilva.ipass.mealmaster.exceptions.PasswordIncorrectException;
import me.ceciliosilva.ipass.mealmaster.exceptions.UserDoesNotExistException;
import me.ceciliosilva.ipass.mealmaster.model.User;
import me.ceciliosilva.ipass.mealmaster.utils.ApiHelper;
import me.ceciliosilva.ipass.mealmaster.utils.Logger;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Key;
import java.util.AbstractMap;
import java.util.Calendar;

@Path("auth")
public class AuthResource {

    final static private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private static String createToken(String email) {
        Logger.debug("AuthResource", "Generating JWT for", email);

        try {
            // Gets current time
            Calendar expiration = Calendar.getInstance();

            // Adds 30 minutes to current time for expiry time
            expiration.add(Calendar.MINUTE, 30);

            // Builts JsonWebToken with email as payload 30 minute expiry time and the HS512 algo
            return Jwts.builder()
                    .setSubject(email)
                    .setExpiration(expiration.getTime())
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();

        } catch (Exception e) {
            Logger.error("AuthResource", "JWT error", e.toString());
            return null;
        }
    }

    @POST
    @Path("register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String jsonBody) {
        try {
            // Reads request body for data
            JsonObject requestBody = ApiHelper.requestBodyReader(jsonBody);

            // Extracts parameters
            String name = requestBody.getString("name");
            String email = requestBody.getString("email");
            String password = requestBody.getString("password");

            // Tries to register user
            boolean success = User.registerUser(name, email, password);

            if (!success){
                // If registering fails user already exists
                return ApiHelper.simpleMsgResponse(Response.Status.BAD_REQUEST,"User already exists");
            } else {
                // Send success body
                return ApiHelper.simpleMsgResponse(Response.Status.OK,"User created!");
            }
        } catch (Exception e){
            Logger.error("AuthResource", "create user", e.toString());
            return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR,"Error: " + e);
        }
    }

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logUserIn(String jsonBody){
        try {
            // Reads request body for data
            JsonObject requestBody = ApiHelper.requestBodyReader(jsonBody);

            // Extracts parameters
            String email = requestBody.getString("email");
            String password = requestBody.getString("password");

            // Tries to authenticate user with given credentials
            User user = User.authenticateUser(email, password);

            // Generates JWT token with email as payload
            String token = createToken(user.getEmail());

            if(token != null){
                // If token gets made return 200 response
                return Response.ok(new AbstractMap.SimpleEntry<>("JWT", token)).build();
            }

            // if token failed return 500 response
            return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, "Couldn't generate JWT");
        } catch (PasswordIncorrectException e){
            return ApiHelper.simpleMsgResponse(Response.Status.UNAUTHORIZED, e.getMessage());
        } catch (UserDoesNotExistException e){
            return ApiHelper.simpleMsgResponse(Response.Status.BAD_REQUEST, e.getMessage());
        }
    }
}
