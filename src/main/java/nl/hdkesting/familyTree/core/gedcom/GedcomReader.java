package nl.hdkesting.familyTree.core.gedcom;

import nl.hdkesting.familyTree.core.services.TreeService;

public interface GedcomReader {
    void processNextLine(String line);
    void store(TreeService treeService);
}
