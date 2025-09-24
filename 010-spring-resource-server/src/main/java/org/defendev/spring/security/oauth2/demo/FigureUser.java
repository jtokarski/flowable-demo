package org.defendev.spring.security.oauth2.demo;

import static java.util.Objects.nonNull;



/*
 *
 * This single class can represent both:
 *   - a user principal or
 *   - a service principal (like Kong client)
 * The differentiation can be made based on:
 *   - presence of userId/clientId fields
 *   - roles/authorities (not there yet)
 * It's acceptable trade-off that helps me avoid type casting of various principal classes.
 *
 * Also, by design there is no field like "isMock" or anything like that. I don't want to pollute
 * production code with such things.
 *
 */
public class FigureUser {

    private final String userId;

    private final String firstName;

    private final String lastName;

    private final String email;

    private final String clientId;

    private final String clientName;

    public FigureUser(String userId, String firstName, String lastName, String email, String clientId,
                      String clientName) {
        if (nonNull(userId) && nonNull(clientId)) {
            throw new IllegalArgumentException("FigureUser cannot be user and service principal at the same time");
        }
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.clientId = clientId;
        this.clientName = clientName;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isUserPrincipal() {
        return nonNull(userId);
    }

    public boolean isServicePrincipal() {
        return  nonNull(clientId);
    }

}
