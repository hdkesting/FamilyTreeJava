package nl.hdkesting.familyTree.infrastructure.repositories;

import javax.persistence.EntityManager;

/**
 * A "functional interface" to be able to specify a lambda that gets an EntityManager as param
 * and returns something (to be specified on usage).
 * @param <T>
 */
@FunctionalInterface
interface UseEntityManager<T> {
    T call(EntityManager entityManager);
}
