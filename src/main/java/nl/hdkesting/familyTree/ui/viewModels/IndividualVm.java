package nl.hdkesting.familyTree.ui.viewModels;

import nl.hdkesting.familyTree.core.dto.IndividualDto;
import nl.hdkesting.familyTree.core.dto.Sex;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * ViewModel with basic data for a single individual.
 */
public class IndividualVm {
    private long id;
    private String firstNames;
    private String lastName;
    private char sex = ' '; // expected: 'M' or 'F'

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String birthPlace;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deathDate;
    private String deathPlace;

    public IndividualVm() {
    }

    public IndividualVm(IndividualDto dto) {
        this.id = dto.getId();
        this.firstNames = dto.getFirstNames();
        this.lastName = dto.getLastName();
        this.sex = dto.getSex() == Sex.Male ? 'M' :
                   dto.getSex() == Sex.Female ? 'F' : ' ';
        this.birthDate = dto.getBirthDate();
        this.birthPlace = dto.getBirthPlace();
        this.deathDate = dto.getDeathDate();
        this.deathPlace = dto.getDeathPlace();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
    }

    public String getDeathPlace() {
        return deathPlace;
    }

    public void setDeathPlace(String deathPlace) {
        this.deathPlace = deathPlace;
    }

    public String getFullName() {
        return firstNames + " " + lastName;
    }

    public boolean isMale() {
        return this.sex == 'M';
    }

    public boolean isFemale() {
        return this.sex == 'F';
    }

    /**
     * Is this person known to have died at birth (meaning both birth and death are known and identical)?
     * This makes no distinction between "died before birth" and "died the day of birth".
     * @return
     */
    public boolean diedAtBirth() {
        return this.birthDate != null && this.deathDate != null && this.birthDate.equals(this.deathDate);
    }

    /**
     * Is there any useful data about birth?
     * @return
     */
    public boolean birthDataKnown() {
        return this.birthDate != null || (this.birthPlace != null && this.birthPlace.trim().length() > 0);
    }

    /**
     * Is there any useful data about death?
     * @return
     */
    public boolean deathDataKnown() {
        return this.deathDate != null || (this.deathPlace != null && this.deathPlace.trim().length() > 0);
    }
}
