package nl.hdkesting.familyTree.infrastructure.repositories;

import nl.hdkesting.familyTree.core.dto.NameCount;

import java.util.List;

public interface IndividualNameRepository {
    List<NameCount> getLastNames();
}
