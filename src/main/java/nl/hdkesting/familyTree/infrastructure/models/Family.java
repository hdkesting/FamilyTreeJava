package nl.hdkesting.familyTree.infrastructure.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "family")
public class Family {
    public Family() {}

    public Family(long id){
        this.id = id;
    }

    @Id
    @Column(name = "id")
    public long id;

    @Column(name = "marriagedate")
    public LocalDate marriageDate;

    @Column(name = "marriageplace")
    public String marriagePlace;

    @Column(name = "divorcedate")
    public LocalDate divorceDate;

    @Column(name = "divorceplace")
    public String divorcePlace;

    /*
        @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "spouses",
            joinColumns = {@JoinColumn(name = "spouseid")},
            inverseJoinColumns = { @JoinColumn(name = "familyid")})

     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "spouses",
        joinColumns = @JoinColumn(name = "familyid", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "spouseid", referencedColumnName = "id"))
    public final Set<Individual> spouses = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "children",
        joinColumns = @JoinColumn(name = "familyid", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "childid", referencedColumnName = "id"))
    public final Set<Individual> children = new HashSet<>();

    public void setMarriageDate(int year, int month, int day) {
        this.marriageDate = LocalDate.of(year, month, day);
    }

    @Override
    public String toString() {
        return "Family @F" + this.id;
    }
}
