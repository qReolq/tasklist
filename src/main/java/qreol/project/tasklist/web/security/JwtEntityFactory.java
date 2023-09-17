package qreol.project.tasklist.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import qreol.project.tasklist.domain.user.Role;
import qreol.project.tasklist.domain.user.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtEntityFactory {

    public static JwtEntity create(User user) {
        JwtEntity entity = new JwtEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setName(user.getName());
        entity.setPassword(user.getPassword());
        entity.setAuthorities(mapToGrantedAuthorities(new ArrayList<>(user.getRoles())));

        return entity;
    }

    private static Collection<? extends GrantedAuthority> mapToGrantedAuthorities(List<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new).toList();
    }

}
