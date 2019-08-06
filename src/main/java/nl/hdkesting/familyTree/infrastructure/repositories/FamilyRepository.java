package nl.hdkesting.familyTree.infrastructure.repositories;

import nl.hdkesting.familyTree.infrastructure.models.Family;
import org.springframework.data.repository.CrudRepository;

// an implementation apparently gets created automatically by Spring

public interface FamilyRepository
    extends CrudRepository<Family, Long> {
}
