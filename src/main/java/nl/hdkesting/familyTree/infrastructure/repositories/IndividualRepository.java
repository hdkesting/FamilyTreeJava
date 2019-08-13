package nl.hdkesting.familyTree.infrastructure.repositories;

import nl.hdkesting.familyTree.infrastructure.models.Individual;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// an implementation gets created automatically by Spring

@Repository
public interface IndividualRepository
    extends CrudRepository<Individual, Long>, IndividualNameRepository {
}
