package nl.hdkesting.familyTree.ui.controllers.viewModels;

import nl.hdkesting.familyTree.core.dto.Summary;

public class SummaryVm {
    public long personCount;
    public long familyCount;
    public long childCount;
    public long spouseCount;

    public SummaryVm() {
    }

    public SummaryVm(Summary summaryDto) {
        if (summaryDto != null) {
            this.personCount = summaryDto.getIndividualCount();
            this.familyCount = summaryDto.getFamilyCount();
            this.childCount = summaryDto.getChildCount();
            this.spouseCount = summaryDto.getSpouseCount();
        }
    }
}
