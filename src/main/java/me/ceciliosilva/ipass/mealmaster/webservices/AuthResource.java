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

    final static public Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private static String validatePassword(String password) {
        // Validates password

        // Checks if password is at least 8 characters long
        int minLength = 8;
        if (password.length() < minLength) {
            return "Password should be at least 8 characters long";
        }

        // Checks if password is at most 40 characters long
        String upperChars = "(.*[A-Z].*)";
        if (!password.matches(upperChars)) {
            return "Password should have an uppercase letter";
        }

        // Checks if password has a lowercase letter
        String lowerChars = "(.*[a-z].*)";
        if (!password.matches(lowerChars)) {
            return "Password should have a lowercase letter";
        }

        // Checks if password has a number
        String numbers = "(.*[0-9].*)";
        if (!password.matches(numbers)) {
            return "Password should have a number";
        }

        // Checks if password has a special character
        String specialChars = "(.*[@,#,$,%,.,!,^,*].*$)";
        if (!password.matches(specialChars)) {
            return "Password must have atleast one special character among @#$%";
        }

        return "";
    }

    private static String validateUsername(String username) {
        // Validates username

        // Checks if username is at least 3 characters long and at most 40 characters
        // long
        if (username.length() < 3 || username.length() > 40) {
            return "Username should be between 3 and 40 characters";
        }

        return "";
    }

    private static String validateEmail(String email) {
        // Validates email

        // Checks if email is valid
        String emailRegex = "^(.+)@(.+)$";
        if (!email.matches(emailRegex)) {
            // If email is not valid return error
            return "Invalid email";
        }

        return "";
    }

    private static String createToken(String email, String name) {
        // Creates JWT token for user
        Logger.debug("AuthResource", "Generating JWT for", email);

        try {
            // Gets current time
            Calendar expiration = Calendar.getInstance();

            // Adds 30 minutes to current time for expiry time
            expiration.add(Calendar.MINUTE, 30);

            // Builts JsonWebToken with email as payload 30 minute expiry time and the HS512
            // algo
            return Jwts.builder()
                    .setSubject(email)
                    .setExpiration(expiration.getTime())
                    .claim("name", name)
                    .claim("role", "user")
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();

        } catch (Exception e) {
            // If error occurs return null
            Logger.error("AuthResource", "JWT error", e.toString());
            return null;
        }
    }

    @POST
    @Path("register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String jsonBody) {
        // Creates user

        try {
            // Reads request body for data
            JsonObject requestBody = ApiHelper.requestBodyReader(jsonBody);

            // Extracts parameters
            String name = requestBody.getString("name");
            String email = requestBody.getString("email");
            String password = requestBody.getString("password");

            // Validates password
            String isPwdValid = validatePassword(password);
            if (!isPwdValid.equals("")) {
                return ApiHelper.simpleMsgResponse(Response.Status.BAD_REQUEST, isPwdValid);
            }

            // Validates user name
            String isUsrValid = validateUsername(password);
            if (!isUsrValid.equals("")) {
                return ApiHelper.simpleMsgResponse(Response.Status.BAD_REQUEST, isUsrValid);
            }

            // Validates email
            String isEmValid = validateEmail(email);
            if (!isEmValid.equals("")) {
                return ApiHelper.simpleMsgResponse(Response.Status.BAD_REQUEST, isEmValid);
            }

            // Tries to register user
            boolean success = User.registerUser(name, email, password);

            if (!success) {
                // If registering fails user already exists
                return ApiHelper.simpleMsgResponse(Response.Status.BAD_REQUEST, "User already exists");
            } else {
                // Send success body
                return ApiHelper.simpleMsgResponse(Response.Status.OK, "User created!");
            }
        } catch (Exception e) {
            Logger.error("AuthResource", "create user", e.toString());
            // If error occurs return 500 response
            return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, "Error: " + e);
        }
    }

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logUserIn(String jsonBody) {
        // Logs user in
        try {
            // Reads request body for data
            JsonObject requestBody = ApiHelper.requestBodyReader(jsonBody);

            // Extracts parameters
            String email = requestBody.getString("email");
            String password = requestBody.getString("password");

            // Tries to authenticate user with given credentials
            User user = User.authenticateUser(email, password);

            // Generates JWT token with email as payload
            String token = createToken(user.getEmail(), user.getName());

            if (token != null) {
                // If token gets made return 200 response
                return Response.ok(new AbstractMap.SimpleEntry<>("JWT", token)).build();
            }

            // if token failed return 500 response
            return ApiHelper.simpleMsgResponse(Response.Status.INTERNAL_SERVER_ERROR, "Couldn't generate JWT");
        } catch (PasswordIncorrectException | UserDoesNotExistException e) {
            // If user doesn't exist or password is incorrect return 401 response
            return ApiHelper.simpleMsgResponse(Response.Status.UNAUTHORIZED, "Password or username incorrect");
        }
    }
}
