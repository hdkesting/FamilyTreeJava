package nl.hdkesting.familyTree.core.gedcom;

import nl.hdkesting.familyTree.core.services.TreeService;

/**
 * Interface for readers of GEDCOM objects.
 */
public interface GedcomReader {
    /**
     * Adds the next line to the internal object, when applicable.
     * @param line
     */
    void processNextLine(PropertyLine line);

    /**
     * Store the internal object to the database.
     * @param treeService
     */
    void store(TreeService treeService);
}
