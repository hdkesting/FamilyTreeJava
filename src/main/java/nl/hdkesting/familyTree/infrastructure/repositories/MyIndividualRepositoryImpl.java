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
public class MyIndividualRepositoryImpl
        extends MyBaseRepository
        implements nl.hdkesting.familyTree.core.interfaces.MyIndividualRepository {

    public MyIndividualRepositoryImpl(EntityManagerFactory factory) {
        super(factory);
    }

    /**
     * Find a single individual based on the supplied id.
     * @param id
     * @return
     */
    public Optional<Individual> findById(long id, boolean includeDeleted) {
        System.out.println("-- getting INDI " + id);
        String basequery = "select ind " +
                "from Individual ind " +
                "left join fetch ind.spouseFamilies sf " +
                "left join fetch ind.childFamilies cf " +
                "where ind.id = :id";
        String query;
        if (includeDeleted) {
            query = basequery;
        } else {
            query = basequery + " and ind.isDeleted=0";
        }

        List<Individual> result = this.boilerPlate(em -> em.createQuery(
                query, Individual.class)
                .setParameter("id", id)
                .getResultList());

        if (result.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(result.get(0));
    }

    public boolean existsById(long id) {
        Long result = this.boilerPlate(em -> em.createQuery(
                "select count(*) " +
                        "from Individual ind " +
                        "where ind.id = :id and deleted=0", Long.class)
                .setParameter("id", id)
                .getSingleResult());
        return result.equals(1L); // I expect either 0 or 1 for a count by PK
    }

    public List<Individual> findAll() {
        return this.boilerPlate(em -> em.createQuery(
                "select ind " +
                        "from Individual ind " +
                        "where ind.isDeleted=0", Individual.class)
                .getResultList());
    }

    public List<Individual> getAllDeleted() {
        return this.boilerPlate(em -> em.createQuery(
                "select ind " +
                        "from Individual ind " +
                        "where ind.isDeleted=1", Individual.class)
                .getResultList());
    }

    public int deleteAll() {
        // delete *all* individuals
        // TODO in transaction with family version

        return this.boilerPlate(em ->
                em.createQuery(
                        "delete from Individual")
                        .executeUpdate()
        );
    }

    public int restorePersons(List<Long> ids) {
        return boilerPlate(em ->
                // need to cast as "setParameterList" isn't available otherwise. It works!
                ((org.hibernate.query.Query)em.createQuery("update Individual indi set deleted=0 where indi.id in :ids"))
                    .setParameterList("ids", ids)
                    .executeUpdate());
    }

    public void save(Individual individual) {
        var dummy = boilerPlate(em-> {
            em.merge(individual);

            return 0;
        });
    }

    public long add(Individual individual) {
        // get max id but do not ignore deleted items!
        return this.boilerPlate(em-> {
            long max = em.createQuery("select max(id) from Individual", Long.class)
                    .getSingleResult();
            max++;
            individual.id = max;
            em.merge(individual);

            return max;
        });
    }

    /**
     * Returns the total count of individuals.
     * @return
     */
    public long count() {
        return this.boilerPlate(em -> em.createQuery(
                "select count(*) " +
                        "from Individual ind where ind.isDeleted=0", Long.class) // NB spelling/capitalisation of table name must match class
                .getSingleResult());
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

        return boilerPlate(em -> em.createQuery(
                "SELECT ind " +
                    "FROM Individual ind " +
                    "WHERE lastName = :lastname " +
                        "AND ind.isDeleted=0 " +
                    "ORDER BY birthDate, deathDate, lastName, firstNames", Individual.class)
                .setParameter("lastname", lastName)
                .getResultList()
            );
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

        return this.boilerPlate(em -> {
                    String qryText = "SELECT indi FROM Individual indi WHERE indi.isDeleted=0 AND indi.lastName LIKE :last";
                    if (!Strings.isNullOrEmpty(firstName)) {
                        var names = firstName.split("\\s+");
                        StringBuilder sb = new StringBuilder(qryText);
                        for (int i = 0; i < names.length; i++) {
                            sb.append(" AND indi.firstNames LIKE :first").append(i);
                        }
                        qryText = sb.toString();

                        Query query = em.createQuery(qryText, Individual.class)
                                .setParameter("last", lastName2);

                        for (int i = 0; i < names.length; i++) {
                            query.setParameter("first" + i, "%" + names[i] + "%");
                        }

                        return (List<Individual>) query.getResultList();
                    }

                    return em.createQuery(qryText, Individual.class)
                            .setParameter("last", lastName2)
                            .getResultList();
                }
        );
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

        var dels = this.boilerPlate(em -> em.createQuery("update Individual indi set deleted = 1 where indi.id = :id")
                .setParameter("id", id)
                .executeUpdate());

        return dels > 0;
    }
}
