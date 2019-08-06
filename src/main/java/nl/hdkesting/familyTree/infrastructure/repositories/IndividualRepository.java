package nl.hdkesting.familyTree.infrastructure.repositories;

import nl.hdkesting.familyTree.infrastructure.models.Individual;
import org.springframework.data.repository.CrudRepository;

// an implementation apparently gets created automatically by Spring

public interface IndividualRepository
    extends CrudRepository<Individual, Long> {
}
