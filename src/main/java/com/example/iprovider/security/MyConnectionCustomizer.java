package com.example.iprovider.security;

import com.mchange.v2.c3p0.ConnectionCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;

/**
 * This class implements the ConnectionCustomizer interface and customizes the behavior of the connection pool.
 * <p>
 * It sets the user role depending on the authentication object in the security context of the current request.
 * <p>
 * If the authentication object is not available, the guest role is set.
 */
@Component
public class MyConnectionCustomizer implements ConnectionCustomizer {

    @Override
    public void onAcquire(Connection connection, String parentDataSourceIdentityToken) throws Exception {
        // do nothing
    }

    @Override
    public void onDestroy(Connection connection, String parentDataSourceIdentityToken) throws Exception {
        // do nothing
    }

    /**
     * This method is called when a connection is checked out from the pool. It sets the user role depending on the
     * authentication object in the security context of the current request. If the authentication object is not
     * available, the guest role is set.
     *
     * @param connection                    the checked out connection
     * @param parentDataSourceIdentityToken the identity token of the parent data source
     * @throws Exception if an error occurs
     */
    @Override
    public void onCheckOut(Connection connection, String parentDataSourceIdentityToken) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Statement statement = connection.createStatement();
        if (authentication == null) {
            statement.execute("SET ROLE " + "guest");
        } else {
            String userRole = authentication.getAuthorities().iterator().next().getAuthority();
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                userRole = grantedAuthority.getAuthority();
            }
            switch (userRole) {
                case "ROLE_USER" -> statement.execute("SET ROLE " + "\"user\"");
                case "ROLE_ADMIN" -> statement.execute("SET ROLE " + "admin");
                case "ROLE_MAIN_ADMIN" -> statement.execute("SET ROLE " + "main_admin");
                default -> statement.execute("SET ROLE " + "guest");
            }
        }
        statement.close();

    }

    @Override
    public void onCheckIn(Connection connection, String parentDataSourceIdentityToken) throws Exception {
        // do nothing
    }
}
