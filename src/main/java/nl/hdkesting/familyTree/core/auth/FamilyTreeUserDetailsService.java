package nl.hdkesting.familyTree.core.auth;

import nl.hdkesting.familyTree.core.interfaces.AuthGroupRepository;
import nl.hdkesting.familyTree.core.interfaces.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FamilyTreeUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AuthGroupRepository authGroupRepository;

    public FamilyTreeUserDetailsService(UserRepository userRepository, AuthGroupRepository authGroupRepository) {
        super();
        this.userRepository = userRepository;
        this.authGroupRepository = authGroupRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = this.userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Unknown user name: " + username);
        }

        var groups = this.authGroupRepository.findByUsername(username);

        return new FamilyTreeUserPrincipal(user, groups);
    }
}
