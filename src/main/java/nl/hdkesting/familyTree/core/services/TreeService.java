package nl.hdkesting.familyTree.core.services;

import nl.hdkesting.familyTree.core.dto.FamilyDto;
import nl.hdkesting.familyTree.core.dto.IndividualDto;
import nl.hdkesting.familyTree.core.dto.Sex;
import nl.hdkesting.familyTree.infrastructure.models.Family;
import nl.hdkesting.familyTree.infrastructure.models.Individual;
import nl.hdkesting.familyTree.infrastructure.repositories.FamilyRepository;
import nl.hdkesting.familyTree.infrastructure.repositories.IndividualRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class TreeService {
    private static final char maleIdentifier = 'M';
    private static final char femaleIdentifier = 'F';

    private FamilyRepository familyRepository;
    private IndividualRepository individualRepository;

    //@Autowired
    public TreeService(FamilyRepository familyRepository, IndividualRepository individualRepository) {
        this.familyRepository = familyRepository;
        this.individualRepository = individualRepository;
    }

    public Optional<IndividualDto> getIndividualById(long id) {
        Optional<Individual> individual = individualRepository.findById(id);

        if (individual.isPresent()) {
            IndividualDto dto = getNewIndividualDto(id);
            map(individual.get(), dto);
            return Optional.of(dto);
        }

        return Optional.empty();
    }

    public Iterable<IndividualDto> getAllIndividuals() {
        ArrayList<IndividualDto> target = new ArrayList<>();
        Iterable<Individual> sourceList = individualRepository.findAll();
        for(Individual source: sourceList) {
            target.add(convert(source));
        }

        return target;
    }

    public void update(IndividualDto person) {
        if (person == null) throw new IllegalArgumentException("'person' cannot be null");
        if (person.getId() <= 0) throw new IllegalArgumentException("person's id must be >0");

        long id = person.getId();
        Individual dbPerson = individualRepository.findById(id)
                .orElseGet(() -> getNewIndividual(id));

        map(person, dbPerson);
        individualRepository.save(dbPerson);
    }

    public void update(FamilyDto family) {
        if (family == null) throw new IllegalArgumentException("'family' cannot be null");
        if (family.getId() <= 0) throw new IllegalArgumentException("family's id must be >0");

        long id = family.getId();
        Family dbFamily = familyRepository.findById(id)
                .orElseGet(() -> {
                    var f = new Family();
                    f.setId(id);
                    return f;
                });

        map(family, dbFamily);
        familyRepository.save(dbFamily);
    }

    private IndividualDto convert(Individual source) {
        IndividualDto target = getNewIndividualDto(source.getId());
        map(source, target);
        return target;
    }

    private Individual getNewIndividual(long id) {
        Individual i = new Individual();
        i.setId(id);
        return i;
    }

    private IndividualDto getNewIndividualDto(long id) {
        IndividualDto i = new IndividualDto();
        i.setId(id);
        return i;
    }

    private void map(FamilyDto fromDtoFamily, Family toDbFamily) {
        toDbFamily.setMarriageDate(fromDtoFamily.getMarriageDate());
        toDbFamily.setMarriagePlace(fromDtoFamily.getMarriagePlace());
        toDbFamily.setDivorceDate(fromDtoFamily.getDivorceDate());
        toDbFamily.setDivorcePlace(fromDtoFamily.getDivorcePlace());
    }

    private void map(Family fromDbFamily, FamilyDto toDtoFamily) {
        toDtoFamily.setMarriageDate(fromDbFamily.getMarriageDate());
        toDtoFamily.setMarriagePlace(fromDbFamily.getMarriagePlace());
        toDtoFamily.setDivorceDate(fromDbFamily.getDivorceDate());
        toDtoFamily.setDivorcePlace(fromDbFamily.getDivorcePlace());
    }

    private void map(IndividualDto fromDtoPerson, Individual toDbPerson) {
        toDbPerson.setFirstNames(fromDtoPerson.getFirstNames());
        toDbPerson.setLastName(fromDtoPerson.getLastName());
        switch (fromDtoPerson.getSex()){
            case Male:
                toDbPerson.setSex(maleIdentifier);
                break;
            case Female:
                toDbPerson.setSex(femaleIdentifier);
                break;
        }

        toDbPerson.setBirthDate(fromDtoPerson.getBirthDate());
        toDbPerson.setBirthPlace(fromDtoPerson.getBirthPlace());
        toDbPerson.setDeathDate(fromDtoPerson.getDeathDate());
        toDbPerson.setDeathPlace(fromDtoPerson.getDeathPlace());
    }

    private void map(Individual fromDbPerson,IndividualDto  toDtoPerson) {
        toDtoPerson.setFirstNames(fromDbPerson.getFirstNames());
        toDtoPerson.setLastName(fromDbPerson.getLastName());
        switch (fromDbPerson.getSex()){
            case maleIdentifier:
                toDtoPerson.setSex(Sex.Male);
                break;
            case femaleIdentifier:
                toDtoPerson.setSex(Sex.Female);
                break;
        }

        toDtoPerson.setBirthDate(fromDbPerson.getBirthDate());
        toDtoPerson.setBirthPlace(fromDbPerson.getBirthPlace());
        toDtoPerson.setDeathDate(fromDbPerson.getDeathDate());
        toDtoPerson.setDeathPlace(fromDbPerson.getDeathPlace());
    }

}
