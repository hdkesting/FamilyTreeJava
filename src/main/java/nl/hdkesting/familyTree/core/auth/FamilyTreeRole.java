package nl.hdkesting.familyTree.core.auth;

public enum FamilyTreeRole {
    ADMIN("ADMIN"),
    USER("USER");

    private final String name;
    private FamilyTreeRole(String name) {
       this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
