package nl.hdkesting.familyTree.infrastructure.repositories;

import nl.hdkesting.familyTree.infrastructure.models.Individual;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// an implementation gets created automatically by Spring

@Repository
public interface IndividualRepository
    extends CrudRepository<Individual, Long>, IndividualNameRepository {

    // Implementation will be auto-generated based on method name
    List<Individual> findByLastName(String lastName, Sort sort);
}
