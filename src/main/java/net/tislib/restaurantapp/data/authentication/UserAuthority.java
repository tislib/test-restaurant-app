package net.tislib.restaurantapp.data.authentication;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public class UserAuthority implements GrantedAuthority {

    private String authority;

}
