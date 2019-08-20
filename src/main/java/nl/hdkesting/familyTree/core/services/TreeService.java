package nl.hdkesting.familyTree.core.services;

import nl.hdkesting.familyTree.core.dto.*;
import nl.hdkesting.familyTree.infrastructure.models.Family;
import nl.hdkesting.familyTree.infrastructure.models.Individual;
import nl.hdkesting.familyTree.infrastructure.repositories.FamilyRepository;
import nl.hdkesting.familyTree.infrastructure.repositories.IndividualRepository;
import nl.hdkesting.familyTree.infrastructure.repositories.MyIndividualRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TreeService {
    private static final char MALE_CHAR = 'M';
    private static final char FEMALE_CHAR = 'F';
    private static final int MAXDEPTH = 6;

    private final FamilyRepository familyRepository;
    private final MyIndividualRepository individualRepository;

    public TreeService(
            FamilyRepository familyRepository,
            MyIndividualRepository individualRepository) {
        this.familyRepository = familyRepository;
        this.individualRepository = individualRepository;
    }

    public Optional<IndividualDto> getIndividualById(long id) {
        Optional<Individual> individual = this.individualRepository.findById(id);

        if (individual.isPresent()) {
            IndividualDto dto = getNewIndividualDto(id);
            map(individual.get(), dto, MAXDEPTH);
            return Optional.of(dto);
        }

        return Optional.empty();
    }

    public Iterable<IndividualDto> getAllIndividuals() {
        ArrayList<IndividualDto> target = new ArrayList<>();
        Iterable<Individual> sourceList = this.individualRepository.findAll();
        for(Individual source: sourceList) {
            target.add(convert(source, MAXDEPTH));
        }

        return target;
    }

    public boolean clearAll() {
        try {
            this.individualRepository.deleteAll();
            this.familyRepository.deleteAll();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean load(String path) {
        var reader = new GedcomFileReader(this);
        return reader.readFile(path);
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

    public void updateRelations(long familyId, ArrayList<Long> spouseIds, ArrayList<Long> childIds) {
        var optFamily =  familyRepository.findById(familyId);

        if (optFamily.isPresent()) {
            // check against stored relations, update where needed
            Family family = optFamily.get();

            // check for spouses to remove
            Set<Individual> spouses = family.getSpouses();
            for (Individual spouse : spouses) {
                boolean found = false;
                long spouseId = spouse.id;
                for (long id : spouseIds) {
                    found = found || id == spouseId;
                }

                if (!found) {
                    spouses.remove(spouse); // can I modify the list?
                }
            }

            // check for spouses to add
            for (long id : spouseIds) {
                boolean found = false;
                for (Individual spouse : spouses) {
                    found = found || id == spouse.id;
                }

                if (!found) {
                    var newspouse = this.individualRepository.findById(id);
                    if (newspouse.isPresent()){
                        spouses.add(newspouse.get());
                    }
                }
            }

            // check for children to remove
            Set<Individual> children = family.getChildren();
            for (Individual child : children) {
                boolean found = false;
                long childId = child.id;
                for (long id : childIds) {
                    found = found || id == childId;
                }

                if (!found) {
                    children.remove(child);
                }
            }

            // check for children to add
            for (long id : childIds) {
                boolean found = false;
                for (Individual child : children) {
                    found = found || id == child.id;
                }

                if (!found) {
                    var newchild = this.individualRepository.findById(id);
                    if (newchild.isPresent()){
                        children.add(newchild.get());
                    }
                }
            }

            familyRepository.save(family);
        } // else ignore
    }

    public Summary getSummary() {
        var sum = new Summary();

        sum.setIndividualCount(this.individualRepository.count());
        sum.setFamilyCount(this.familyRepository.count());
        sum.setSpouseCount(this.familyRepository.getSpousesCount());
        sum.setChildCount(this.familyRepository.getChildrenCount());

        return sum;
    }

    public List<NameCount> getLastNames() {
        return this.individualRepository.getLastNames();
    }

    public List<IndividualDto> getAllByLastname(String lastName) {
        List<Individual> list = this.individualRepository.findByLastName(lastName, Sort.by(new String[] {"birthDate", "deathDate", "lastName", "firstNames"}));

        return convert(list);
    }

    public List<IndividualDto> searchByName(String firstName, String lastName) {
        List<Individual> list = this.individualRepository.findByFirstNamesAndLastName(firstName, lastName);

        return convert(list);
    }

    private List<IndividualDto> convert(List<Individual> list) {
        List<IndividualDto> result = new ArrayList<>(list.size());
        for (Individual indi : list) {
            IndividualDto dto = getNewIndividualDto(indi.id);
            map(indi, dto, 1);
            result.add(dto);
        }

        return result;
    }

    private IndividualDto convert(Individual source, int depth) {
        IndividualDto target = getNewIndividualDto(source.id);
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
        i.id = id;
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

        var sourceSpouses = fromDbFamily.getSpouses();
        var targetSpouses = toDtoFamily.getSpouses();
        for (Individual source : sourceSpouses) {
            targetSpouses.add(convert(source, depth));
        }

        var sourceChildren = fromDbFamily.getChildren();
        var targetChildren = toDtoFamily.getChildren();
        for (Individual source : sourceChildren) {
            targetChildren.add(convert(source, depth));
        }
    }

    private void map(IndividualDto fromDtoPerson, Individual toDbPerson, int depth) {
        toDbPerson.firstNames = fromDtoPerson.getFirstNames();
        toDbPerson.lastName = fromDtoPerson.getLastName();
        switch (fromDtoPerson.getSex()){
            case Male:
                toDbPerson.sex = MALE_CHAR;
                break;
            case Female:
                toDbPerson.sex = FEMALE_CHAR;
                break;
            default:
                toDbPerson.sex = '?';
        }

        toDbPerson.birthDate = fromDtoPerson.getBirthDate();
        toDbPerson.birthPlace = fromDtoPerson.getBirthPlace();
        toDbPerson.deathDate = fromDtoPerson.getDeathDate();
        toDbPerson.deathPlace = fromDtoPerson.getDeathPlace();

        depth--;
        if (depth <= 0) return;

        // TODO
    }

    private void map(Individual fromDbPerson, IndividualDto  toDtoPerson, int depth) {
        toDtoPerson.setFirstNames(fromDbPerson.firstNames);
        toDtoPerson.setLastName(fromDbPerson.lastName);
        switch (fromDbPerson.sex){
            case MALE_CHAR:
                toDtoPerson.setSex(Sex.Male);
                break;
            case FEMALE_CHAR:
                toDtoPerson.setSex(Sex.Female);
                break;
            default:
                toDtoPerson.setSex(Sex.Unknown);
        }

        toDtoPerson.setBirthDate(fromDbPerson.birthDate);
        toDtoPerson.setBirthPlace(fromDbPerson.birthPlace);
        toDtoPerson.setDeathDate(fromDbPerson.deathDate);
        toDtoPerson.setDeathPlace(fromDbPerson.deathPlace);

        depth--;
        if (depth <= 0) return;

        var sourceChildFams = fromDbPerson.childFamilies;
        var targetChildFams = toDtoPerson.getChildFamilies();
        for (Family source : sourceChildFams) {
            targetChildFams.add(convert(source, depth));
        }

        var sourceSpouseFams = fromDbPerson.spouseFamilies;
        var targetSpouseFams = toDtoPerson.getSpouseFamilies();
        for (Family source : sourceSpouseFams) {
            targetSpouseFams.add(convert(source, depth));
        }
    }
}
