package nl.hdkesting.familyTree.core.auth;

import nl.hdkesting.familyTree.infrastructure.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class FamilyTreeUserPrincipal implements UserDetails {
    private final User user;

    public FamilyTreeUserPrincipal(User user) {
        super();
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // apparently "hasRole" adds "ROLE_" to the supplied name !! Java is great
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + FamilyTreeRole.ADMIN.toString()));
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        // later: get from database through User object
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
