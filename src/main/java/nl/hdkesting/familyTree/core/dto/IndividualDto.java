package nl.hdkesting.familyTree.core.dto;

import java.util.Date;
import java.util.Set;

public class IndividualDto {
    private Long id;
    private String firstNames;
    private String lastName;
    private Sex sex;
    private Date birthDate;
    private String birthPlace;
    private Date deathDate;
    private String deathPlace;
    private Set<FamilyDto> spouseFamilies;
    private Set<FamilyDto> childFamilies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
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

    public void setSpouseFamilies(Set<FamilyDto> spouseFamilies) {
        this.spouseFamilies = spouseFamilies;
    }

    public Set<FamilyDto> getChildFamilies() {
        return childFamilies;
    }

    public void setChildFamilies(Set<FamilyDto> childFamilies) {
        this.childFamilies = childFamilies;
    }
}
