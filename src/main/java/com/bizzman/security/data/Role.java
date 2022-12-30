package com.bizzman.security.data;

import com.bizzman.exceptions.custom.InvalidRoleException;
import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    public String role;

    @Override
    @Deprecated(since = "Instead of this method, use the static attributes")
    public String getAuthority() {
        return null;
    }

    public void setRole(String role) {
        if (!(role.equals(Role.ADMIN) || role.equals(Role.USER))) {
            throw new InvalidRoleException("Specified roles does not exist!");
        }
        this.role = role;
    }
}
