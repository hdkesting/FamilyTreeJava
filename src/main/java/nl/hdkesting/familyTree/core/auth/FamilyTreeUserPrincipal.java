package nl.hdkesting.familyTree.core.auth;

import nl.hdkesting.familyTree.infrastructure.models.AuthGroup;
import nl.hdkesting.familyTree.infrastructure.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class FamilyTreeUserPrincipal implements UserDetails {
    private final User user;
    private final List<AuthGroup> authGroups;

    public FamilyTreeUserPrincipal(User user, List<AuthGroup> authGroups) {
        super();
        this.user = user;
        this.authGroups = authGroups;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.authGroups == null) {
            return Collections.emptySet();
        }

        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
        authGroups.forEach(grp -> {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + grp.getAuthGroup())); // *need* to add "ROLE_" !
        });

        return grantedAuthorities;
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
