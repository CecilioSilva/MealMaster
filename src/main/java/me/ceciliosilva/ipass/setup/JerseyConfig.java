package me.ceciliosilva.ipass.setup;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        packages("me.ceciliosilva.ipass.mealmaster.webservices");
    }
}
