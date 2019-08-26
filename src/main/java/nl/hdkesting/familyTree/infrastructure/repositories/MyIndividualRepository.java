package nl.hdkesting.familyTree.infrastructure.repositories;

import nl.hdkesting.familyTree.core.dto.NameCount;
import nl.hdkesting.familyTree.infrastructure.models.Individual;
import nl.hdkesting.familyTree.infrastructure.models.NameCountModel;
import org.assertj.core.util.Strings;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MyIndividualRepository extends MyBaseRepository {

    public MyIndividualRepository(EntityManagerFactory factory) {
        super(factory);
    }

    /**
     * Find a single individual based on the supplied id.
     * @param id
     * @return
     */
    public Optional<Individual> findById(long id) {
        System.out.println("-- getting INDI " + id);
        List<Individual> result = boilerPlate(em -> em.createQuery(
                "select ind " +
                        "from Individual ind " +
                        "left join fetch ind.spouseFamilies sf " +
                        "left join fetch ind.childFamilies cf " +
                        "where ind.id = :id and ind.isDeleted=0", Individual.class)
                .setParameter("id", id)
                .getResultList());

        if (result.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(result.get(0));
    }

    public boolean existsById(long id) {
        Long result = boilerPlate(em -> em.createQuery(
                "select count(*) " +
                        "from Individual ind " +
                        "where ind.id = :id and deleted=0", Long.class)
                .setParameter("id", id)
                .getSingleResult());
        return result.equals(1L); // I expect either 0 or 1 for a count by PK
    }

    public List<Individual> findAll() {
        List<Individual> result = boilerPlate(em -> em.createQuery(
                "select ind " +
                        "from Individual ind where ind.isDeleted=0", Individual.class)
                .getResultList());

        return result;
    }

    public int deleteAll() {
        // delete *all*.
        // TODO in transaction with family version
        Integer result = boilerPlate(em ->
                em.createQuery(
                        "delete from Individual")
                        .executeUpdate()
        );

        return result;
    }

    public void save(Individual individual) {
        var dummy = boilerPlate(em-> {
            em.merge(individual);

            return 0;
        });
    }

    public long add(Individual individual) {
        // get max id but do not ignore deleted items!
        var newid = boilerPlate(em-> {
            long max = em.createQuery("select max(id) from Individual", Long.class)
                    .getSingleResult();
            max++;
            individual.id = max;
            em.merge(individual);

            return max;
        });

        return newid;
    }

    /**
     * Returns the total count of individuals.
     * @return
     */
    public long count() {
        Long result = boilerPlate(em -> em.createQuery(
                "select count(*) " +
                        "from Individual ind where ind.isDeleted=0", Long.class) // NB spelling/capitalisation of table name must match class
                .getSingleResult());

        return result;
    }

    /**
     * Find all unique last names plus count of individuals per lastname.
     * @return
     */
    public List<NameCount> getLastNames() {
        List<NameCountModel> names = boilerPlate(em -> em.createQuery(
                "SELECT new NameCountModel(lastName, count(*)) " +
                    "FROM Individual ind " +
                    "WHERE ind.isDeleted=0 " +
                    "GROUP BY lastName ORDER BY lastName", NameCountModel.class)
                .getResultList()
            );

        List<NameCount> result = new ArrayList<>();
        for (NameCountModel name : names) {
            result.add(new NameCount(name.getLastname(), name.getCount()));
        }

        return result;
    }

    /**
     * Find all individuals with an exact match on the supplied lastname.
     * @param lastName
     * @return
     */
    public List<Individual> findByLastName(String lastName) {
        List<Individual> result = boilerPlate(em -> em.createQuery(
                "SELECT ind " +
                    "FROM Individual ind " +
                    "WHERE lastName = :lastname " +
                        "AND ind.isDeleted=0 " +
                    "ORDER BY birthDate, deathDate, lastName, firstNames", Individual.class)
                .setParameter("lastname", lastName)
                .getResultList()
            );

        return result;
    }

    /**
     * Find all individuals with firstname and lastname containing the supplied values.
     * @param firstName
     * @param lastName
     * @return
     */
    public List<Individual> findByFirstNamesAndLastName(String firstName, String lastName) {
        String lastName2;
        if (Strings.isNullOrEmpty(lastName)) {
            lastName2 = "%";
        } else {
            lastName2 = "%" + lastName + "%";
        }

        List<Individual> result = boilerPlate(em -> {
            String qryText = "SELECT indi FROM Individual indi WHERE indi.isDeleted=0 AND indi.lastName LIKE :last";
            if (!Strings.isNullOrEmpty(firstName)) {
                var names = firstName.split("\\s+");
                StringBuilder sb = new StringBuilder(qryText);
                for (int i=0; i<names.length; i++) {
                    sb.append(" AND indi.firstNames LIKE :first" + i);
                }
                qryText = sb.toString();

                Query query = em.createQuery(qryText, Individual.class)
                        .setParameter("last", lastName2);

                for (int i=0; i<names.length; i++) {
                    query.setParameter("first" + i, "%" + names[i] + "%");
                }

                return query.getResultList();
            }

            return em.createQuery(qryText, Individual.class)
                             .setParameter("last", lastName2)
                            .getResultList();
            }
        );

        return result;
    }

    /**
     * Delete an individual by his/her id.
     * @param id
     * @return true when succeeded, otherwise false.
     */
    public boolean deleteById(long id) {
        /*var dels = boilerPlate( em -> {
            em.createNativeQuery("delete from children where childid = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            em.createNativeQuery("delete from spouses where spouseid = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            var cnt = em.createNativeQuery("delete from Individual where id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            return cnt;
        });*/

        var dels = boilerPlate(em -> em.createQuery("update Individual indi set deleted = 1 where indi.id = :id")
                  .setParameter("id", id)
                    .executeUpdate());

        return dels > 0;
    }
}
