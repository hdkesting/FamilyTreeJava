package nl.hdkesting.familyTree.core.dto;

public class NameCount {
    private final String lastName;
    private final long count;

    public NameCount(String lastname, long count) {
        this.lastName = lastname;
        this.count = count;
    }

    public String getLastName() {
        return lastName;
    }

    public long getCount() {
        return count;
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.lastName, this.count);
    }
}
