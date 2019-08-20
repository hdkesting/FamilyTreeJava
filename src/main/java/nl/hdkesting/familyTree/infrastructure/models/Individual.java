package nl.hdkesting.familyTree.infrastructure.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "individual")
public class Individual {
    @Id
    @Column(name = "id")
    public Long id;

    @Column(name = "firstnames")
    public String firstNames;

    @Column(name = "lastname")
    public String lastName;

    @Column(name = "sex")
    public char sex; // 'M' or 'F'

    @Column(name = "birthdate")
    public LocalDate birthDate;

    @Column(name = "birthplace")
    public String birthPlace;

    @Column(name = "deathdate")
    public LocalDate deathDate;

    @Column(name = "deathplace")
    public String deathPlace;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "spouses",
            joinColumns = {@JoinColumn(name = "spouseid")},
            inverseJoinColumns = { @JoinColumn(name = "familyid")})
    public final Set<Family> spouseFamilies = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "children",
            joinColumns = {@JoinColumn(name = "childid")},
            inverseJoinColumns = { @JoinColumn(name = "familyid")})
    public final Set<Family> childFamilies = new HashSet<>();

    public void setBirthDate(int year, int month, int day) {
        this.birthDate = LocalDate.of(year, month, day);
    }

    @Override
    public String toString() {
        return String.format("Individual @I%d: %s /%s/", this.id, this.firstNames, this.lastName);
    }
}
