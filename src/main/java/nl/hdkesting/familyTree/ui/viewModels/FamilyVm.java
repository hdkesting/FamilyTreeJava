package nl.hdkesting.familyTree.ui.viewModels;

import nl.hdkesting.familyTree.core.dto.FamilyDto;
import nl.hdkesting.familyTree.core.dto.IndividualDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FamilyVm {
    public long id;
    public IndividualVm husband;
    public IndividualVm wife;
    public final List<IndividualVm> children = new ArrayList<>();

    public LocalDate marriageDate;
    public String marriagePlace;
    public LocalDate divorceDate;
    public String divorcePlace;

    public FamilyVm() {}

    public FamilyVm(FamilyDto dto) {
        this.id = dto.getId();
        this.marriageDate = dto.getMarriageDate();
        this.marriagePlace = dto.getMarriagePlace();
        this.divorceDate = dto.getDivorceDate();
        this.divorcePlace = dto.getDivorcePlace();

        for(IndividualDto spouse : dto.getSpouses()) {
            if (spouse.isMale()) {
                this.husband = new IndividualVm(spouse);
            } else if (spouse.isFemale()) {
                this.wife = new IndividualVm(spouse);
            }
        }

        for (IndividualDto child : dto.getChildren()) {
            this.children.add(new IndividualVm(child));
        }
    }
}
