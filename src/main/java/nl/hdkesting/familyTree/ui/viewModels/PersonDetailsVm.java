package nl.hdkesting.familyTree.ui.viewModels;

import java.util.ArrayList;
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
            if (sib.id != primary.id) {
                siblings.add(sib);
            }
        }
    }
}
