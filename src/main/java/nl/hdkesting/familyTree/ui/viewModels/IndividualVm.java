package nl.hdkesting.familyTree.ui.viewModels;

import nl.hdkesting.familyTree.core.dto.IndividualDto;
import nl.hdkesting.familyTree.core.dto.Sex;

import java.time.LocalDate;

/**
 * ViewModel with basic data for a single individual.
 */
public class IndividualVm {
    public Long id;
    public String firstNames;
    public String lastName;
    public char sex = ' '; // expected: 'M' or 'F'
    public LocalDate birthDate;
    public String birthPlace;
    public LocalDate deathDate;
    public String deathPlace;

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

    public String getFullName() {
        return firstNames + " " + lastName;
    }

    public boolean isMale() {
        return this.sex == 'M';
    }

    public boolean isFemale() {
        return this.sex == 'F';
    }
}
