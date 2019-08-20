package nl.hdkesting.familyTree.infrastructure.repositories;

import nl.hdkesting.familyTree.core.dto.NameCount;
import nl.hdkesting.familyTree.core.support.NotYetImplementedException;
import nl.hdkesting.familyTree.infrastructure.models.Individual;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

@Repository
public class MyIndividualRepository {
    private final EntityManagerFactory entityManagerFactory;

    public MyIndividualRepository(EntityManagerFactory factory) {
        this.entityManagerFactory = factory;
    }

    public Optional<Individual> findById(long id) {
        Individual result = null;

        // TODO try-with-resources (entityManager)
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = entityManagerFactory
                    .createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();

            result = entityManager.createQuery(
                    "select ind " +
                    "from Individual ind " +
                    "where ind.id = :id", Individual.class)
                    .setParameter("id", id)
                    .getSingleResult();

            transaction.commit();
        } catch (Throwable e) {
            if (transaction != null &&
            transaction.isActive())
            transaction.rollback();
            throw e;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return Optional.ofNullable(result);
    }

    public List<Individual> findAll() {
        List<Individual> result = null;

        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = entityManagerFactory
                    .createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();

            result = entityManager.createQuery(
                    "select ind " +
                            "from Individual ind", Individual.class)
                    .getResultList();

            transaction.commit();
        } catch (Throwable e) {
            if (transaction != null &&
                    transaction.isActive())
                transaction.rollback();
            throw e;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return result;
    }

    public void deleteAll() {
        throw new NotYetImplementedException();
    }

    public void save(Individual individual) {
        throw new NotYetImplementedException();
    }

    public long count() {
        Long result = boilerPlate(new UseEntityManager<Long>() {
            @Override
            public Long call(EntityManager entityManager) {
                return entityManager.createQuery(
                        "select count(*) " +
                                "from Individual ind", Long.class) // NB spelling/capitalisation of table name must match class
                        .getSingleResult();
            }
        });

        return result;
    }

    public List<NameCount> getLastNames() {
        throw new NotYetImplementedException();
    }

    public List<Individual> findByLastName(String lastName, Sort sort) {
        throw new NotYetImplementedException();
    }

    public List<Individual> findByFirstNamesAndLastName(String firstName, String lastName) {
        throw new NotYetImplementedException();
    }

    private <T> T boilerPlate(UseEntityManager<T> usage) {
        EntityManager entityManager = null; // does not implement AutoClosable, so no try-with-resources
        EntityTransaction transaction = null;
        T result = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();

            // call injected method
            result = usage.call(entityManager);

            transaction.commit();
        } catch (Throwable e) {
            if (transaction != null &&
                    transaction.isActive())
                transaction.rollback();
            throw e;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return result;
    }
}
