package nl.hdkesting.familyTree.infrastructure.repositories;

import nl.hdkesting.familyTree.infrastructure.models.Individual;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// an implementation gets created automatically by Spring

@Repository
public interface IndividualRepository
    extends CrudRepository<Individual, Long>, IndividualNameRepository {

    // Implementation will be auto-generated based on method name
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
    List<Individual> findByLastName(String lastName, Sort sort);

    @Query(value = "SELECT * FROM individual WHERE firstnames LIKE %?1% AND lastname LIKE %?2% ", nativeQuery = true)
    List<Individual> findByFirstNamesAndLastName(String firstname, String lastname);
}
