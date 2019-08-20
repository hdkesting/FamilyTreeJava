package nl.hdkesting.familyTree.infrastructure.repositories;

import javax.persistence.EntityManager;

@FunctionalInterface
interface UseEntityManager<T> {
    T call(EntityManager entityManager);
}
