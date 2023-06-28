package me.ceciliosilva.ipass.mealmaster.webservices;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import me.ceciliosilva.ipass.mealmaster.model.User;
import me.ceciliosilva.ipass.mealmaster.utils.Logger;
import me.ceciliosilva.ipass.mealmaster.utils.MySecurityContext;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestCtx) {
        // Checks if user is logged in and valid

        String scheme = requestCtx.getUriInfo().getRequestUri().getScheme();

        // Users are treated as guests, unless a valid JWT is provided
        MySecurityContext msc = new MySecurityContext(null, scheme);
        String authHeader = requestCtx.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring("Bearer".length()).trim();

            try {
                // Validate the token
                JwtParser parser = Jwts.parserBuilder().setSigningKey(AuthResource.key).build();
                Claims claims = parser.parseClaimsJws(token).getBody();

                String user = claims.getSubject();
                msc = new MySecurityContext(User.getUserByEmail(user), scheme);

            } catch (JwtException | IllegalArgumentException e) {
                Logger.error("Authentication Filter", "Invalid JWT, processing as guest!");
            }
        }

        requestCtx.setSecurityContext(msc);
    }
}
