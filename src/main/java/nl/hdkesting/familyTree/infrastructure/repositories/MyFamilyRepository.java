package nl.hdkesting.familyTree.infrastructure.repositories;

import nl.hdkesting.familyTree.infrastructure.models.Family;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.math.BigInteger;
import java.util.List;
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
        System.out.println("-- getting FAM " + id);
        List<Family> result = boilerPlate(em -> em.createQuery(
                "select fam " +
                        "from Family fam " +
                        "left join fetch fam.children fc " +
                        "left join fetch fam.spouses fs " +
                        "where fam.id = :id", Family.class)
                .setParameter("id", id)
                .getResultList());

        // getSingleResult throws an exception when that ID wasn't found. getResultList then just returns an empty list.

        if (result.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(result.get(0));
    }

    public boolean existsById(long id) {
        Long result = boilerPlate(em -> em.createQuery(
                "select count(*) " +
                        "from Family fam " +
                        "where fam.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult());
        return result.equals(1L); // I expect either 0 or 1 for a count by PK
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

    public int deleteAll() {
        Integer result = boilerPlate(em ->
                em.createQuery(
                        "delete from Family")
                        .executeUpdate()
        );

        return result;
    }

    public void save(Family family) {
        var dummy = boilerPlate(em-> {
            em.merge(family);

            return 0;
        });
    }

}
