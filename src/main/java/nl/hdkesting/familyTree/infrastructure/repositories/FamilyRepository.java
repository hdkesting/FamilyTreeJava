package nl.hdkesting.familyTree.infrastructure.repositories;

import nl.hdkesting.familyTree.infrastructure.models.Family;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// an implementation gets created automatically by Spring

@Repository
public interface FamilyRepository
    extends CrudRepository<Family, Long> {

    @Query(value = "SELECT Count(*) FROM spouses", nativeQuery = true)
    long getSpousesCount();

    @Query(value = "SELECT Count(*) FROM children", nativeQuery = true)
    long getChildrenCount();
}
