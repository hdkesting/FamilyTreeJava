package nl.hdkesting.familyTree.core.gedcom;

public class GedcomUtil {
    private GedcomUtil(){
        // static class - no instance
    }

    public static long getIdFromReference(String reference) {
        // ref is like "@F123@" -> return 123
        reference = reference.trim();
        reference = reference.substring(2, reference.length()-1); // cut off the "@I.." and "..@"
        return Long.parseLong(reference);
    }
}
