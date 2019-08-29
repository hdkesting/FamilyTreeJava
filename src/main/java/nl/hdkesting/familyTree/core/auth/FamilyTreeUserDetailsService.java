package nl.hdkesting.familyTree.core.auth;

import nl.hdkesting.familyTree.infrastructure.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FamilyTreeUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public FamilyTreeUserDetailsService(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = this.userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Unknown user name: " + username);
        }

        return new FamilyTreeUserPrincipal(user);
    }
}
