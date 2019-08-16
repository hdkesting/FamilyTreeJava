package nl.hdkesting.familyTree.core.dto;

public class NameCount {
    private String lastName;
    private long count;

    public NameCount() {
    }

    public NameCount(String lastname, long count) {
        this.lastName = lastname;
        this.count = count;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.lastName, this.count);
    }
}
