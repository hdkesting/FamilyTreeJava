package nl.hdkesting.familyTree.ui.viewModels;

import nl.hdkesting.familyTree.core.dto.FamilyDto;
import nl.hdkesting.familyTree.core.dto.IndividualDto;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FamilyVm {
    private long id;
    private IndividualVm husband;
    private IndividualVm wife;
    private final List<IndividualVm> children = new ArrayList<>();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate marriageDate;
    private String marriagePlace;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate divorceDate;
    private String divorcePlace;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public IndividualVm getHusband() {
        return husband;
    }

    public void setHusband(IndividualVm husband) {
        this.husband = husband;
    }

    public IndividualVm getWife() {
        return wife;
    }

    public void setWife(IndividualVm wife) {
        this.wife = wife;
    }

    public List<IndividualVm> getChildren() {
        return children;
    }

    public LocalDate getMarriageDate() {
        return marriageDate;
    }

    public void setMarriageDate(LocalDate marriageDate) {
        this.marriageDate = marriageDate;
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
}
