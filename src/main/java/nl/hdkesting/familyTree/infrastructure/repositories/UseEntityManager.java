package nl.hdkesting.familyTree.infrastructure.repositories;

import javax.persistence.EntityManager;

public interface UseEntityManager<T> {
    T call(EntityManager entityManager);
}
