package nl.hdkesting.familyTree.infrastructure.models;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "family")
public class Family {
    @Id
    @Column(name = "id")
    public Long id;

    @Column(name = "marriagedate")
    public LocalDate marriageDate;

    @Column(name = "marriageplace")
    public String marriagePlace;

    @Column(name = "divorcedate")
    public LocalDate divorceDate;

    @Column(name = "divorceplace")
    public String divorcePlace;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "familyid")
    public final Set<SpouseFamily> spouses = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "familyid")
    public final Set<ChildFamily> children = new HashSet<>();

    public void setMarriageDate(int year, int month, int day) {
        this.marriageDate = LocalDate.of(year, month, day);
    }

    @Override
    public String toString() {
        return "Family @F" + this.id;
    }
}
