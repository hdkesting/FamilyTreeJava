package nl.hdkesting.familyTree.core.interfaces;

import nl.hdkesting.familyTree.infrastructure.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

