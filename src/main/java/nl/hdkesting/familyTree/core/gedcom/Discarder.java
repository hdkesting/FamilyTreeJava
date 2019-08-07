package nl.hdkesting.familyTree.core.gedcom;

import nl.hdkesting.familyTree.core.services.TreeService;

/**
 * Reads through a gedcom object but ignores it.
 */
public class Discarder implements GedcomReader {
    @Override
    public void processNextLine(String line) {
        // ignore
    }

    @Override
    public void store(TreeService treeService) {
        // ignore
    }
}