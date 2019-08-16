package nl.hdkesting.familyTree.core.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class IndividualDto {
    private long id;
    private String firstNames;
    private String lastName;
    private Sex sex = Sex.Unknown;
    private LocalDate birthDate;
    private String birthPlace;
    private LocalDate deathDate;
    private String deathPlace;
    private final Set<FamilyDto> spouseFamilies = new HashSet<>();
    private final Set<FamilyDto> childFamilies = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstNames() {
        return firstNames;
    }

    public void setFirstNames(String firstNames) {
        this.firstNames = firstNames;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
    }

    public String getDeathPlace() {
        return deathPlace;
    }

    public void setDeathPlace(String deathPlace) {
        this.deathPlace = deathPlace;
    }

    public Set<FamilyDto> getSpouseFamilies() {
        return spouseFamilies;
    }

    public Set<FamilyDto> getChildFamilies() {
        return childFamilies;
    }

    public String getFullName() {
        return String.format("%s %s", this.firstNames, this.lastName);
    }

    public boolean isMale() {
        return this.getSex() == Sex.Male;
    }

    public boolean isFemale() {
        return this.getSex() == Sex.Female;
    }

    @Override
    public String toString() {
        return String.format("Individual @I%d: %s /%s/", this.id, this.firstNames, this.lastName);
    }
}
