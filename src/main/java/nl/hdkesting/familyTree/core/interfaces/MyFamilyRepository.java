package nl.hdkesting.familyTree.core.interfaces;

import nl.hdkesting.familyTree.infrastructure.models.Family;

import java.util.List;
import java.util.Optional;

public interface MyFamilyRepository {
    /**
     * Find a single family based on the supplied id.
     * @param id
     * @return
     */
    Optional<Family> findById(long id);

    boolean existsById(long id);

    /**
     * Get all (known) families where the person is spouse.
     * @param spouseId
     * @return
     */
    List<Family> getFamiliesBySpouseId(long spouseId);

    /**
     * Get all (known) families where the person is child.
     * @param childId
     * @return
     */
    List<Family> getFamiliesByChildId(long childId);

    /**
     * Returns the total count of families.
     * @return
     */
    long count();

    long getTotalSpouseCount();

    long getTotalChildrenCount();

    int deleteAll();

    void save(Family family);

    long add(Family family);
}
