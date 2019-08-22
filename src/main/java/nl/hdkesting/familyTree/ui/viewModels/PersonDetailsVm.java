package nl.hdkesting.familyTree.ui.viewModels;

import nl.hdkesting.familyTree.infrastructure.models.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * ViewModel for the "person" template:
 * all sorts of information around one person, like siblings, (grand)parents, spouse(s) and children
 */
public class PersonDetailsVm {
    public IndividualVm primary;
    public FamilyVm family = new FamilyVm();
    public final List<IndividualVm> siblings = new ArrayList<>();
    public FamilyVm maternalGrandparents = new FamilyVm();
    public FamilyVm paternalGrandparents = new FamilyVm();

    public final List<FamilyVm> marriages = new ArrayList<>();

    public void setSiblings() {
        for (IndividualVm sib : family.children) {
            if (sib.getId() != primary.getId()) {
                siblings.add(sib);
            }
        }
    }

    public void sortData() {
        Collections.sort(siblings, new SortIndividualsByBirth());
        for (FamilyVm fam : this.marriages) {
            Collections.sort(fam.children, new SortIndividualsByBirth());
        }
    }

    class SortIndividualsByBirth implements Comparator<IndividualVm> {
        public int compare(IndividualVm a, IndividualVm b) {
            if (a.getBirthDate() == null) {
                return b.getBirthDate() == null ? 0 : 1;
            }
            if (b.getBirthDate() == null) {
                return -1;
            }

            return a.getBirthDate().compareTo(b.getBirthDate());
        }
    }
}
