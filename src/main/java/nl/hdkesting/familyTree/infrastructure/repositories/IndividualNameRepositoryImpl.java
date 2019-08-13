package nl.hdkesting.familyTree.infrastructure.repositories;

import nl.hdkesting.familyTree.core.dto.NameCount;
import nl.hdkesting.familyTree.infrastructure.models.NameCountModel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;


public class IndividualNameRepositoryImpl implements IndividualNameRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<NameCount> getLastNames() {
        List<NameCountModel> names = entityManager.createNamedQuery(NameCountModel.LASTNAME_QUERY, NameCountModel.class)
                                    .getResultList();

        List<NameCount> result = new ArrayList<>();
        for (NameCountModel name : names) {
            result.add(new NameCount(name.getLastname(), name.getCount()));
        }

        return result;
    }
}
