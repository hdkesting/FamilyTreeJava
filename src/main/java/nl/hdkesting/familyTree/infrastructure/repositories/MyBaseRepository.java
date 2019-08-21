package nl.hdkesting.familyTree.infrastructure.repositories;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

// package-private
abstract class MyBaseRepository {
    protected final EntityManagerFactory entityManagerFactory;

    protected MyBaseRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    protected <T> T boilerPlate(UseEntityManager<T> usage) {
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
