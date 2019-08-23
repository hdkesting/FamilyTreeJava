package nl.hdkesting.familyTree.core.services;

import nl.hdkesting.familyTree.core.dto.*;
import nl.hdkesting.familyTree.infrastructure.models.Family;
import nl.hdkesting.familyTree.infrastructure.models.Individual;
import nl.hdkesting.familyTree.infrastructure.repositories.MyFamilyRepository;
import nl.hdkesting.familyTree.infrastructure.repositories.MyIndividualRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TreeService {
    private static final char MALE_CHAR = 'M';
    private static final char FEMALE_CHAR = 'F';

    private final MyFamilyRepository familyRepository;
    private final MyIndividualRepository individualRepository;

    public TreeService(
            MyFamilyRepository familyRepository,
            MyIndividualRepository individualRepository) {
        this.familyRepository = familyRepository;
        this.individualRepository = individualRepository;
    }

    public Optional<IndividualDto> getIndividualById(long id) {
        Optional<Individual> individual = this.individualRepository.findById(id);

        if (individual.isPresent()) {
            IndividualDto dto = getNewIndividualDto(id);
            map(individual.get(), dto);
            return Optional.of(dto);
        }

        return Optional.empty();
    }

    public Iterable<IndividualDto> getAllIndividuals() {
        ArrayList<IndividualDto> target = new ArrayList<>();
        Iterable<Individual> sourceList = this.individualRepository.findAll();
        for(Individual source: sourceList) {
            target.add(convert(source));
        }

        return target;
    }

    public List<FamilyDto> getSpouseFamiliesByIndividualId(long id) {
        List<Family> families = this.familyRepository.getFamiliesBySpouseId(id);
        return convertFamilies(families);
    }

    public List<FamilyDto> getChildFamiliesByIndividualId(long id) {
        List<Family> families = this.familyRepository.getFamiliesByChildId(id);
        return convertFamilies(families);
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

    public boolean deleteIndividualById(long id) {
        return this.individualRepository.deleteById(id);
    }

    public boolean load(String path) {
        var reader = new GedcomFileReader(this);
        return reader.readFile(path);
    }

    public void update(IndividualDto person) {
        if (person == null) throw new IllegalArgumentException("'person' cannot be null");
        if (person.getId() <= 0) throw new IllegalArgumentException("person's id must be >0");

        // person must have an id, but may not yet exist in the database (when importing)
        long id = person.getId();
        Individual dbPerson = individualRepository.findById(id)
                .orElseGet(() -> getNewIndividual(id));

        map(person, dbPerson);
        individualRepository.save(dbPerson);
    }

    public long add(IndividualDto person) {
        if (person == null) throw new IllegalArgumentException("'person' cannot be null");
        if (person.getId() != 0) throw new IllegalArgumentException("new person's id must be ==0");

        Individual dbPerson = getNewIndividual(0);
        map(person, dbPerson);

        var id = individualRepository.add(dbPerson);
        person.setId(id);
        return id;
    }

    public void update(FamilyDto family) {
        if (family == null) throw new IllegalArgumentException("'family' cannot be null");
        if (family.getId() <= 0) throw new IllegalArgumentException("family's id must be >0");

        long id = family.getId();
        Family dbFamily = familyRepository.findById(id)
                .orElseGet(() -> new Family(id));

        map(family, dbFamily);
        familyRepository.save(dbFamily);
    }

    public void updateRelations(long familyId, ArrayList<Long> spouseIds, ArrayList<Long> childIds) {
        var optFamily =  familyRepository.findById(familyId);

        if (optFamily.isEmpty()) {
            // just ignore or should I throw ??
            return;
        }

        // check against stored relations, update where needed
        Family family = optFamily.get();

        if (spouseIds != null) {
            // check for spouses to remove
            Set<Individual> spouses = family.spouses;
            List<Individual> toremove = new ArrayList<>();
            for (Individual spouse : spouses) {
                boolean found = false;
                for (long id : spouseIds) {
                    found = found || id == spouse.id;
                }

                if (!found) {
                    toremove.add(spouse);
                }
            }

            if (!toremove.isEmpty()) {
                spouses.removeAll(toremove);
            }

            // check for spouses to add
            for (long id : spouseIds) {
                boolean found = false;
                for (Individual spouse : spouses) {
                    found = found || id == spouse.id;
                }

                if (!found) {
                    var optSpouse = this.individualRepository.findById(id);
                    optSpouse.ifPresent(spouses::add);
                }
            }
        }

        if (childIds != null) {
            // check for children to remove
            Set<Individual> children = family.children;
            List<Individual> toremove = new ArrayList<>();
            for (Individual child : children) {
                boolean found = false;
                for (long id : childIds) {
                    found = found || id == child.id;
                }

                if (!found) {
                    toremove.add(child);
                }
            }

            if (!toremove.isEmpty()) {
                children.removeAll(toremove);
            }

            // check for children to add
            for (long id : childIds) {
                boolean found = false;
                for (Individual child : children) {
                    found = found || id == child.id;
                }

                if (!found) {
                    var optChild = this.individualRepository.findById(id);
                    optChild.ifPresent(children::add);
                }
            }
        }

        familyRepository.save(family);
    }

    public void addChild(long familyId, long childId) {
        var optFamily =  familyRepository.findById(familyId);

        if (optFamily.isEmpty()) {
            // just ignore or should I throw ??
            return;
        }
        Family family = optFamily.get();
        var optChild = this.individualRepository.findById(childId);
        if (optChild.isPresent()) {
            family.children.add(optChild.get());
            familyRepository.save(family);
        }
    }

    public void addSpouse(long familyId, long spouseId) {
        var optFamily =  familyRepository.findById(familyId);

        if (optFamily.isEmpty()) {
            // just ignore or should I throw ??
            return;
        }
        Family family = optFamily.get();
        var optSpouse = this.individualRepository.findById(spouseId);
        if (optSpouse.isPresent()) {
            family.spouses.add(optSpouse.get());
            familyRepository.save(family);
        }
    }

    public Summary getSummary() {
        var sum = new Summary();

        sum.setIndividualCount(this.individualRepository.count());
        sum.setFamilyCount(this.familyRepository.count());
        sum.setSpouseCount(this.familyRepository.getTotalSpouseCount());
        sum.setChildCount(this.familyRepository.getTotalChildrenCount());

        return sum;
    }

    public List<NameCount> getLastNames() {
        return this.individualRepository.getLastNames();
    }

    public List<IndividualDto> getAllByLastname(String lastName) {
        List<Individual> list = this.individualRepository.findByLastName(lastName);

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
            map(indi, dto);
            result.add(dto);
        }

        return result;
    }

    private List<FamilyDto> convertFamilies(List<Family> list) {
        List<FamilyDto> result = new ArrayList<>(list.size());
        for (Family fam : list) {
            FamilyDto dto = getNewFamilyDto(fam.id);
            map(fam, dto);
            result.add(dto);
        }

        return result;
    }

    private IndividualDto convert(Individual source) {
        IndividualDto target = getNewIndividualDto(source.id);
        map(source, target);
        return target;
    }

    private Individual convert(IndividualDto source) {
        Individual target = individualRepository.findById(source.getId())
                .orElseGet(() -> getNewIndividual(source.getId()));
        map(source, target);
        return target;
    }

    private FamilyDto convert(Family source) {
        FamilyDto target = getNewFamilyDto(source.id);
        map(source, target);
        return target;
    }

    private Family convert(FamilyDto source) {
        Family target = familyRepository.findById(source.getId())
                .orElseGet(() -> getNewFamily(source.getId()));
        map(source, target);
        return target;
    }

    private Individual getNewIndividual(long id) {
        return new Individual(id);
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
        return new Family(id);
    }

    private void map(FamilyDto fromDtoFamily, Family toDbFamily) {
        toDbFamily.marriageDate = fromDtoFamily.getMarriageDate();
        toDbFamily.marriagePlace = fromDtoFamily.getMarriagePlace();
        toDbFamily.divorceDate = fromDtoFamily.getDivorceDate();
        toDbFamily.divorcePlace = fromDtoFamily.getDivorcePlace();
    }

    private void map(Family fromDbFamily, FamilyDto toDtoFamily) {
        toDtoFamily.setMarriageDate(fromDbFamily.marriageDate);
        toDtoFamily.setMarriagePlace(fromDbFamily.marriagePlace);
        toDtoFamily.setDivorceDate(fromDbFamily.divorceDate);
        toDtoFamily.setDivorcePlace(fromDbFamily.divorcePlace);

        for (var spouse : fromDbFamily.spouses) {
            toDtoFamily.getSpouses().add(convert(spouse));
        }

        for (var child : fromDbFamily.children) {
            toDtoFamily.getChildren().add(convert(child));
        }
    }

    private void map(IndividualDto fromDtoPerson, Individual toDbPerson) {
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
    }

    private void map(Individual fromDbPerson, IndividualDto  toDtoPerson) {
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
    }
}
