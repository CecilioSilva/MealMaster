package me.ceciliosilva.ipass.mealmaster.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;

import me.ceciliosilva.ipass.mealmaster.utils.ApiHelper;

public class ApiHelperTest {

    @Test
    public void simpleMsgResponseStatus() {
        // Tests if the simpleMsgResponse returns the correct response
        Response response = ApiHelper.simpleMsgResponse(Response.Status.OK, "Test");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void simpleMsgResponseMsg() {
        // Tests if the simpleMsgResponse returns the correct response
        Response response = ApiHelper.simpleMsgResponse(Response.Status.OK, "Test");

        assertEquals("{\"msg\":\"Test\"}", response.getEntity());
    }

    @Test
    public void requestBodyReader() {
        // Tests if the requestBodyReader returns the correct response
        String requestBody = "{\"msg\":\"Test\"}";
        String msg = ApiHelper.requestBodyReader(requestBody).getString("msg");

        assertEquals("Test", msg);
    }

}
