package nl.hdkesting.familyTree.infrastructure.repositories;

import nl.hdkesting.familyTree.core.dto.NameCount;
import nl.hdkesting.familyTree.core.support.NotYetImplementedException;
import nl.hdkesting.familyTree.infrastructure.models.Individual;
import nl.hdkesting.familyTree.infrastructure.models.NameCountModel;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
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
        Individual result = boilerPlate(em -> em.createQuery(
                "select ind " +
                        "from Individual ind " +
                        "left join fetch ind.spouseFamilies sf " +
                        "left join fetch ind.childFamilies cf " +
                        "where ind.id = :id", Individual.class)
                .setParameter("id", id)
                .getSingleResult());

        return Optional.ofNullable(result);
    }

    public List<Individual> findAll() {
        List<Individual> result = boilerPlate(em -> em.createQuery(
                "select ind " +
                        "from Individual ind", Individual.class)
                .getResultList());

        return result;
    }

    public void deleteAll() {
        throw new NotYetImplementedException("deleteAll must be implemented");
    }

    public void save(Individual individual) {
        throw new NotYetImplementedException("save Individual must be implemented");
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
}
