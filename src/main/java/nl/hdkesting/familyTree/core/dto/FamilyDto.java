package nl.hdkesting.familyTree.core.dto;

import java.util.Date;
import java.util.Set;

public class FamilyDto {
    private Long id;
    private Date marriageDate;
    private String marriagePlace;
    private Date divorceDate;
    private String divorcePlace;
    private Set<IndividualDto> spouses;
    private Set<IndividualDto> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getMarriageDate() {
        return marriageDate;
    }

    public void setMarriageDate(Date marriageDate) {
        this.marriageDate = marriageDate;
    }

    public String getMarriagePlace() {
        return marriagePlace;
    }

    public void setMarriagePlace(String marriagePlace) {
        this.marriagePlace = marriagePlace;
    }

    public Date getDivorceDate() {
        return divorceDate;
    }

    public void setDivorceDate(Date divorceDate) {
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
