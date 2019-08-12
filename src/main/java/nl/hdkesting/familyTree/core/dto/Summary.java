package nl.hdkesting.familyTree.core.dto;

public class Summary {
    private long individualCount;
    private long familyCount;
    private long spouseCount;
    private long childCount;

    public long getIndividualCount() {
        return individualCount;
    }

    public void setIndividualCount(long individualCount) {
        this.individualCount = individualCount;
    }

    public long getFamilyCount() {
        return familyCount;
    }

    public void setFamilyCount(long familyCount) {
        this.familyCount = familyCount;
    }

    public long getSpouseCount() {
        return spouseCount;
    }

    public void setSpouseCount(long spouseCount) {
        this.spouseCount = spouseCount;
    }

    public long getChildCount() {
        return childCount;
    }

    public void setChildCount(long childCount) {
        this.childCount = childCount;
    }
}
