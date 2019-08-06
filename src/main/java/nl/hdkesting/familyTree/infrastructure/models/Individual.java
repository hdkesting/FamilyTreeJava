package nl.hdkesting.familyTree.infrastructure.models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "individual")
public class Individual {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "firstnames")
    private String firstNames;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "sex")
    private char sex; // 'M' or 'F'

    @Column(name = "birthdate")
    private Date birthDate;

    @Column(name = "birthplace")
    private String birthPlace;

    @Column(name = "deathdate")
    private Date deathDate;

    @Column(name = "deathplace")
    private String deathPlace;

    @ManyToMany
    @JoinTable(
            name = "spouses",
            joinColumns = {@JoinColumn(name = "spouseid")},
            inverseJoinColumns = { @JoinColumn(name = "familyid")})
    private Set<Family> spouseFamilies;

    @ManyToMany
    @JoinTable(
            name = "children",
            joinColumns = {@JoinColumn(name = "childid")},
            inverseJoinColumns = { @JoinColumn(name = "familyid")})
    private Set<Family> childFamilies;

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

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
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
}
