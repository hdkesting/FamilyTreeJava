package nl.hdkesting.familyTree.ui.viewModels;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for the "person" template:
 * all sorts of information around one person, like siblings, (grand)parents, spouse(s) and children
 */
public class PersonVm {
    public IndividualVm primary;
    public final List<IndividualVm> siblings = new ArrayList<>();
    public final IndividualVm[] parents = new IndividualVm[2]; // 0=father, 1=mother
    public final IndividualVm[] grandparents = new IndividualVm[4]; // 0=pat gf, 1=pat gm, 2=mat gf, 3=mat gm
}
