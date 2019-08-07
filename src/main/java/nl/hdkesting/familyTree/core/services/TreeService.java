package nl.hdkesting.familyTree.core.services;

import nl.hdkesting.familyTree.core.dto.FamilyDto;
import nl.hdkesting.familyTree.core.dto.IndividualDto;
import nl.hdkesting.familyTree.core.dto.Sex;
import nl.hdkesting.familyTree.infrastructure.models.Family;
import nl.hdkesting.familyTree.infrastructure.models.Individual;
import nl.hdkesting.familyTree.infrastructure.repositories.FamilyRepository;
import nl.hdkesting.familyTree.infrastructure.repositories.IndividualRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Service
public class TreeService {
    private static final char MALE_CHAR = 'M';
    private static final char FEMALE_CHAR = 'F';
    private static final int MAXDEPTH = 4;

    private final FamilyRepository familyRepository;
    private final IndividualRepository individualRepository;

    // @Autowired
    public TreeService(FamilyRepository familyRepository, IndividualRepository individualRepository) {
        this.familyRepository = familyRepository;
        this.individualRepository = individualRepository;
    }

    public Optional<IndividualDto> getIndividualById(long id) {
        Optional<Individual> individual = individualRepository.findById(id);

        if (individual.isPresent()) {
            IndividualDto dto = getNewIndividualDto(id);
            map(individual.get(), dto, MAXDEPTH);
            return Optional.of(dto);
        }

        return Optional.empty();
    }

    public Iterable<IndividualDto> getAllIndividuals() {
        ArrayList<IndividualDto> target = new ArrayList<>();
        Iterable<Individual> sourceList = individualRepository.findAll();
        for(Individual source: sourceList) {
            target.add(convert(source, MAXDEPTH));
        }

        return target;
    }

    public void update(IndividualDto person) {
        if (person == null) throw new IllegalArgumentException("'person' cannot be null");
        if (person.getId() <= 0) throw new IllegalArgumentException("person's id must be >0");

        long id = person.getId();
        Individual dbPerson = individualRepository.findById(id)
                .orElseGet(() -> getNewIndividual(id));

        map(person, dbPerson, 2);
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

        map(family, dbFamily, 2);
        familyRepository.save(dbFamily);
    }

    private IndividualDto convert(Individual source, int depth) {
        IndividualDto target = getNewIndividualDto(source.getId());
        map(source, target, depth);
        return target;
    }

    private Individual convert(IndividualDto source, int depth) {
        Individual target = individualRepository.findById(source.getId())
                .orElseGet(() -> getNewIndividual(source.getId()));
        map(source, target, depth);
        return target;
    }

    private FamilyDto convert(Family source, int depth) {
        FamilyDto target = getNewFamilyDto(source.getId());
        map(source, target, depth);
        return target;
    }

    private Family convert(FamilyDto source, int depth) {
        Family target = familyRepository.findById(source.getId())
                .orElseGet(() -> getNewFamily(source.getId()));
        map(source, target, depth);
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

    private FamilyDto getNewFamilyDto(long id) {
        FamilyDto f = new FamilyDto();
        f.setId(id);
        return f;
    }

    private Family getNewFamily(long id) {
        Family f = new Family();
        f.setId(id);
        return f;
    }

    private void map(FamilyDto fromDtoFamily, Family toDbFamily, int depth) {
        toDbFamily.setMarriageDate(fromDtoFamily.getMarriageDate());
        toDbFamily.setMarriagePlace(fromDtoFamily.getMarriagePlace());
        toDbFamily.setDivorceDate(fromDtoFamily.getDivorceDate());
        toDbFamily.setDivorcePlace(fromDtoFamily.getDivorcePlace());

        depth--;
        if (depth <= 0) return;

        // beware of deep recursion, add a depth limit
        Set<IndividualDto> spousesSource = fromDtoFamily.getSpouses();
        if (spousesSource.size() > 0) {
            Set<Individual> spousesTarget = toDbFamily.getSpouses();
            for (IndividualDto spouseDto : spousesSource) {
                spousesTarget.add(convert(spouseDto, depth));
            }
        }
    }

    private void map(Family fromDbFamily, FamilyDto toDtoFamily, int depth) {
        toDtoFamily.setMarriageDate(fromDbFamily.getMarriageDate());
        toDtoFamily.setMarriagePlace(fromDbFamily.getMarriagePlace());
        toDtoFamily.setDivorceDate(fromDbFamily.getDivorceDate());
        toDtoFamily.setDivorcePlace(fromDbFamily.getDivorcePlace());

        depth--;
        if (depth <= 0) return;

    }

    private void map(IndividualDto fromDtoPerson, Individual toDbPerson, int depth) {
        toDbPerson.setFirstNames(fromDtoPerson.getFirstNames());
        toDbPerson.setLastName(fromDtoPerson.getLastName());
        switch (fromDtoPerson.getSex()){
            case Male:
                toDbPerson.setSex(MALE_CHAR);
                break;
            case Female:
                toDbPerson.setSex(FEMALE_CHAR);
                break;
        }

        toDbPerson.setBirthDate(fromDtoPerson.getBirthDate());
        toDbPerson.setBirthPlace(fromDtoPerson.getBirthPlace());
        toDbPerson.setDeathDate(fromDtoPerson.getDeathDate());
        toDbPerson.setDeathPlace(fromDtoPerson.getDeathPlace());

        depth--;
        if (depth <= 0) return;
    }

    private void map(Individual fromDbPerson, IndividualDto  toDtoPerson, int depth) {
        toDtoPerson.setFirstNames(fromDbPerson.getFirstNames());
        toDtoPerson.setLastName(fromDbPerson.getLastName());
        switch (fromDbPerson.getSex()){
            case MALE_CHAR:
                toDtoPerson.setSex(Sex.Male);
                break;
            case FEMALE_CHAR:
                toDtoPerson.setSex(Sex.Female);
                break;
        }

        toDtoPerson.setBirthDate(fromDbPerson.getBirthDate());
        toDtoPerson.setBirthPlace(fromDbPerson.getBirthPlace());
        toDtoPerson.setDeathDate(fromDbPerson.getDeathDate());
        toDtoPerson.setDeathPlace(fromDbPerson.getDeathPlace());

        depth--;
        if (depth <= 0) return;

        var sourceChildFams = fromDbPerson.getChildFamilies();
        var targetChildFams = toDtoPerson.getChildFamilies();
        for (Family source : sourceChildFams) {
            targetChildFams.add(convert(source, depth));
        }

        var sourceSpouseFams = fromDbPerson.getSpouseFamilies();
        var targetSpouseFams = toDtoPerson.getSpouseFamilies();
        for (Family source : sourceSpouseFams) {
            targetSpouseFams.add(convert(source, depth));
        }
    }
}
