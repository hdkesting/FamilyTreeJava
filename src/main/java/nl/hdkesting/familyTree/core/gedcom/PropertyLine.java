package nl.hdkesting.familyTree.core.gedcom;

public class PropertyLine {
    private int level;
    private String keyword;
    private String value = "";

    public PropertyLine(String line) throws Exception {
        if (line == null || line.length() < 4) throw new Exception("Line needs data.");

        String[] parts = line.split(" ");
        this.level = Integer.parseInt(parts[0]);
        if (parts.length >= 2) this.keyword = parts[1];
        if (parts.length >= 3) this.value = line.substring(parts[0].length() + parts[1].length() + 2);
    }

    public int getLevel() {
        return level;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getValue() {
        return value;
    }
}
