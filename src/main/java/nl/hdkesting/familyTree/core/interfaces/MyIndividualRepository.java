package nl.hdkesting.familyTree.core.interfaces;

import nl.hdkesting.familyTree.core.dto.NameCount;
import nl.hdkesting.familyTree.infrastructure.models.Individual;
import nl.hdkesting.familyTree.infrastructure.models.NameCountModel;
import org.assertj.core.util.Strings;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface MyIndividualRepository {
    /**
     * Find a single individual based on the supplied id.
     * @param id
     * @return
     */
    Optional<Individual> findById(long id, boolean includeDeleted);

    boolean existsById(long id);

    List<Individual> findAll();

    List<Individual> getAllDeleted();

    int deleteAll();

    int restorePersons(List<Long> ids);

    void save(Individual individual);

    long add(Individual individual);

    /**
     * Returns the total count of individuals.
     * @return
     */
    long count();

    /**
     * Find all unique last names plus count of individuals per lastname.
     * @return
     */
    List<NameCount> getLastNames();

    /**
     * Find all individuals with an exact match on the supplied lastname.
     * @param lastName
     * @return
     */
    List<Individual> findByLastName(String lastName);

    /**
     * Find all individuals with firstname and lastname containing the supplied values.
     * @param firstName
     * @param lastName
     * @return
     */
    List<Individual> findByFirstNamesAndLastName(String firstName, String lastName);

    /**
     * Delete an individual by his/her id.
     * @param id
     * @return true when succeeded, otherwise false.
     */
    boolean deleteById(long id);
}
