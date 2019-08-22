package nl.hdkesting.familyTree.infrastructure.repositories;

import nl.hdkesting.familyTree.core.dto.NameCount;
import nl.hdkesting.familyTree.infrastructure.models.Individual;
import nl.hdkesting.familyTree.infrastructure.models.NameCountModel;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
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
                        "where ind.id = :id", Individual.class)
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
                        "where ind.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult());
        return result.equals(1L); // I expect either 0 or 1 for a count by PK
    }

    public List<Individual> findAll() {
        List<Individual> result = boilerPlate(em -> em.createQuery(
                "select ind " +
                        "from Individual ind", Individual.class)
                .getResultList());

        return result;
    }

    public int deleteAll() {
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

    /**
     * Returns the total count of individuals.
     * @return
     */
    public long count() {
        Long result = boilerPlate(em -> em.createQuery(
                "select count(*) " +
                        "from Individual ind", Long.class) // NB spelling/capitalisation of table name must match class
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
                    "FROM Individual " +
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
        List<Individual> result = boilerPlate(em -> em.createQuery(
                "SELECT indi FROM Individual indi " +
                    "WHERE firstNames LIKE :first AND lastName LIKE :last", Individual.class)
                .setParameter("first", "%" + firstName.replace(' ', '%') + "%")
                .setParameter("last", '%' + lastName + '%')
                .getResultList()
        );

        return result;
    }

    /**
     * Delete an individual by his/her id.
     * @param id
     * @return true when succeeded, otherwise false.
     */
    public boolean deleteById(long id) {
        var dels = boilerPlate( em -> {
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
        });

        return dels > 0;
    }
}
