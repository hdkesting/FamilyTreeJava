package nl.hdkesting.familyTree.infrastructure.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;
import java.util.Set;

@Entity
public class Family {
    @Id
    private Long id;
    private Date marriageDate;
    private String marriagePlace;
    private Date divorceDate;
    private String divorcePlace;

    private Set<Individual> spouses;
    private Set<Individual> children;

    public Set<Individual> getSpouses() {
        return spouses;
    }

    public void setSpouses(Set<Individual> spouses) {
        this.spouses = spouses;
    }

    public Set<Individual> getChildren() {
        return children;
    }

    public void setChildren(Set<Individual> children) {
        this.children = children;
    }

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
}
