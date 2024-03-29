package nl.hdkesting.familyTree.core.dto;

import org.assertj.core.util.Strings;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class FamilyDto {
    private long id;
    private LocalDate marriageDate;
    private String marriagePlace;
    private LocalDate divorceDate;
    private String divorcePlace;
    private final Set<IndividualDto> spouses = new HashSet<>();
    private final Set<IndividualDto> children = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
        this.marriagePlace =  Strings.isNullOrEmpty(marriagePlace) ? null : marriagePlace;
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
        this.divorcePlace =  Strings.isNullOrEmpty(divorcePlace) ? null : divorcePlace;
    }

    public Set<IndividualDto> getSpouses() {
        return spouses;
    }

    public Set<IndividualDto> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "Family @F" + this.id;
    }
}
