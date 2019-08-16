package nl.hdkesting.familyTree.infrastructure.models;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "family")
public class Family {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "marriagedate")
    private LocalDate marriageDate;

    @Column(name = "marriageplace")
    private String marriagePlace;

    @Column(name = "divorcedate")
    private LocalDate divorceDate;

    @Column(name = "divorceplace")
    private String divorcePlace;

    @ManyToMany
    @JoinTable(
            name = "spouses",
            joinColumns = {@JoinColumn(name = "familyid")},
            inverseJoinColumns = { @JoinColumn(name = "spouseid")})
    private Set<Individual> spouses = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "children",
            joinColumns = {@JoinColumn(name = "familyid")},
            inverseJoinColumns = { @JoinColumn(name = "childid")})
    private Set<Individual> children = new HashSet<>();

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

    public LocalDate getMarriageDate() {
        return marriageDate;
    }

    public void setMarriageDate(LocalDate marriageDate) {
        this.marriageDate = marriageDate;
    }

    public void setMarriageDate(int year, int month, int day) {
        this.marriageDate = LocalDate.of(year, month, day);
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

    @Override
    public String toString() {
        return "Family @F" + this.id;
    }
}
