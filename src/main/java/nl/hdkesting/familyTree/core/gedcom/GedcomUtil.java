package nl.hdkesting.familyTree.core.gedcom;

public class GedcomUtil {
    private GedcomUtil(){
        // static class - no instance
    }

    /**
     * Takes a reference string like "@F123@" and returns the numerical value (123). The letter is ignored.
     * @param reference - the reference string like @F123@
     * @return the numerical value.
     */
    public static long getIdFromReference(String reference) {
        reference = reference.trim();
        reference = reference.substring(2, reference.length()-1); // cut off the "@I.." and "..@"
        return Long.parseLong(reference);
    }
}
