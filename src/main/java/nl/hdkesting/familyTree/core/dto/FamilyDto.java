package nl.hdkesting.familyTree.core.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class FamilyDto {
    private Long id;
    private LocalDate marriageDate;
    private String marriagePlace;
    private LocalDate divorceDate;
    private String divorcePlace;
    private Set<IndividualDto> spouses = new HashSet<>();
    private Set<IndividualDto> children = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getMarriageDate() {
        return marriageDate;
    }

    public void setMarriageDate(LocalDate marriageDate) {
        this.marriageDate = marriageDate;
    }

    public String getMarriagePlace() {
        return marriagePlace;
    }

    public void setMarriagePlace(String marriagePlace) {
        this.marriagePlace = marriagePlace;
    }

    public LocalDate getDivorceDate() {
        return divorceDate;
    }

    public void setDivorceDate(LocalDate divorceDate) {
        this.divorceDate = divorceDate;
    }

    public String getDivorcePlace() {
        return divorcePlace;
    }

    public void setDivorcePlace(String divorcePlace) {
        this.divorcePlace = divorcePlace;
    }

    public Set<IndividualDto> getSpouses() {
        return spouses;
    }

    public void setSpouses(Set<IndividualDto> spouses) {
        this.spouses = spouses;
    }

    public Set<IndividualDto> getChildren() {
        return children;
    }

    public void setChildren(Set<IndividualDto> children) {
        this.children = children;
    }
}
