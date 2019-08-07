package nl.hdkesting.familyTree.core.gedcom;

import nl.hdkesting.familyTree.core.dto.FamilyDto;
import nl.hdkesting.familyTree.core.services.TreeService;

public class FamilyReader implements GedcomReader {
    private final FamilyDto family;
    public FamilyReader(String line) {
        this.family = new FamilyDto();

        // TODO read ID

    }
    @Override
    public void processNextLine(String line) {
        // TODO
    }

    @Override
    public void store(TreeService treeService) {
        treeService.update(this.family);
    }
}
