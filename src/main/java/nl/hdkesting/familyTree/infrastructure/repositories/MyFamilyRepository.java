package nl.hdkesting.familyTree.infrastructure.repositories;

import nl.hdkesting.familyTree.infrastructure.models.Family;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MyFamilyRepository
    extends MyBaseRepository {

    private static final String FAMILY_QUERY = "select fam " +
            "from Family fam " +
            "left join fetch fam.children fc " +
            "left join fetch fam.spouses fs " +
            "where fam.id = :id " +
            "and fam.isDeleted = 0";

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
        List<Family> result = boilerPlate(em -> em.createQuery(FAMILY_QUERY, Family.class)
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
                        "where fam.id = :id and fam.isDeleted=0", Long.class)
                .setParameter("id", id)
                .getSingleResult());
        return result.equals(1L); // I expect either 0 or 1 for a count by PK
    }

    /**
     * Get all (known) families where the person is spouse.
     * @param spouseId
     * @return
     */
    public List<Family> getFamiliesBySpouseId(long spouseId) {
        List<Family> result = boilerPlate(em -> {
            List<Object> familyIds = em.createNativeQuery(
                "select familyid from spouses where spouseid = :id")
                    .setParameter("id", spouseId)
                    .getResultList();

            List<Family> families = new ArrayList<>();
            for (Object res : familyIds ) {
                long id = ((BigInteger)res).longValue();
                families.add(getFamilyById(em, id));
            }
            return families;
        });

        return result;
    }

    /**
     * Get all (known) families where the person is child.
     * @param childId
     * @return
     */
    public List<Family> getFamiliesByChildId(long childId) {
        List<Family> result = boilerPlate(em -> {
            List<Object> familyIds = em.createNativeQuery(
                    "select familyid from children where childid = :id")
                    .setParameter("id", childId)
                    .getResultList();

            List<Family> families = new ArrayList<>();
            for (Object res : familyIds ) {
                long id = ((BigInteger)res).longValue();
                families.add(getFamilyById(em, id));
            }
            return families;
        });

        return result;
    }

    private Family getFamilyById(EntityManager em, long id) {
        return em.createQuery(FAMILY_QUERY, Family.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    /**
     * Returns the total count of families.
     * @return
     */
    public long count() {
        Long result = boilerPlate(em -> em.createQuery(
                "select count(*) from Family fam where fam.isDeleted=0", Long.class)
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

    public long add(Family family) {
        // get max id but do not ignore deleted items!
        var newid = boilerPlate(em-> {
            long max = em.createQuery("select max(id) from Family", Long.class)
                    .getSingleResult();
            max++;
            family.id = max;
            em.merge(family);

            return max;
        });

        return newid;
    }
}
