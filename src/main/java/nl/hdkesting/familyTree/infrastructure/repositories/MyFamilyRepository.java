package nl.hdkesting.familyTree.infrastructure.repositories;

import nl.hdkesting.familyTree.core.support.NotYetImplementedException;
import nl.hdkesting.familyTree.infrastructure.models.Family;
import nl.hdkesting.familyTree.infrastructure.models.Individual;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.math.BigInteger;
import java.util.Optional;

@Repository
public class MyFamilyRepository
    extends MyBaseRepository {

    public MyFamilyRepository(EntityManagerFactory factory) {
        super(factory);
    }

    /**
     * Find a single family based on the supplied id.
     * @param id
     * @return
     */
    public Optional<Family> findById(long id) {
        Family result = boilerPlate(em -> em.createQuery(
                "select fam " +
                        "from Family fam " +
                        "left join fetch fam.children fc " +
                        "left join fetch fam.spouses fs " +
                        "where fam.id = :id", Family.class)
                .setParameter("id", id)
                .getSingleResult());

        return Optional.ofNullable(result);
    }

    /**
     * Returns the total count of families.
     * @return
     */
    public long count() {
        Long result = boilerPlate(em -> em.createQuery(
                "select count(*) " +
                        "from Family fam", Long.class)
                .getSingleResult());

        return result;
    }

    public long getTotalSpouseCount() {
        Long result = boilerPlate(em ->
                ((BigInteger)em.createNativeQuery(
                    "select count(*) from spouses")
                    .getSingleResult()).longValue()
        );

        return result;
    }

    public long getTotalChildrenCount() {
        Long result = boilerPlate(em ->
                ((BigInteger)em.createNativeQuery(
                        "select count(*) from children")
                        .getSingleResult()).longValue()
        );

        return result;
    }

    public void deleteAll() {
        throw new NotYetImplementedException("deleteAll must be implemented");
    }

    public void save(Family family) {
        throw new NotYetImplementedException("save Family must be implemented");
    }

}
