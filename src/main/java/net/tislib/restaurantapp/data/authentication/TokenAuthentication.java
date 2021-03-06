package net.tislib.restaurantapp.data.authentication;

import lombok.Data;
import org.springframework.security.core.Authentication;

import java.util.Set;

@Data
public class TokenAuthentication implements Authentication {

    private Set<UserAuthority> authorities;

    private Object credentials;

    private Object details;

    private Object principal;

    private String name;

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

}
