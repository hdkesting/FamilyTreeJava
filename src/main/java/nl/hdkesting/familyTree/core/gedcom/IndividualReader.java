package nl.hdkesting.familyTree.core.gedcom;

import nl.hdkesting.familyTree.core.dto.IndividualDto;
import nl.hdkesting.familyTree.core.dto.Sex;
import nl.hdkesting.familyTree.core.services.TreeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads (and stores) a person record (INDI).
 */
public class IndividualReader implements GedcomReader {
    private final IndividualDto individual = new IndividualDto();
    private static final Pattern idPattern = Pattern.compile("\\w@I(\\d+)@\\w");

    private boolean inBirth;
    private boolean inDeath;

    public IndividualReader(PropertyLine line) {
        // assume line is like: "0 @I123@ INDI"
        this.individual.setId(GedcomUtil.getIdFromReference(line.getKeyword()));
    }

    @Override
    public void processNextLine(PropertyLine line) {
        // line = "1 BIRT" of "2 DATE 11 Oct 1691": level SPACE keyword SPACE value

        // on "1 BIRT" remember that, but reset on next "1 ..." (same for DEAT)
        // then a "2 DATE ..." or "2 PLAC ..." can be stored in birthDate or birthPlace (or deathDate/Place)

        if (line.getLevel() == 1) {
            inBirth = false;
            inDeath = false;
        }

        switch (line.getKeyword()) {
            case "NAME":
                processName(line.getValue());
                break;
            case "SEX":
                switch (line.getValue()){
                    case "M":
                        this.individual.setSex(Sex.Male);
                        break;
                    case "F":
                        this.individual.setSex(Sex.Female);
                        break;
                }
                break;
            case "BIRT":
                inBirth = true;
                break;
            case "DEAT":
                inDeath = true;
                break;
            case "DATE":
                GedcomDate dt = GedcomDate.parse(line.getValue());
                if (dt != null) {
                    if (inBirth) {
                        this.individual.setBirthDate(dt.getDate());
                    } else if (inDeath) {
                        this.individual.setDeathDate(dt.getDate());
                    }
                }
                break;
            case "PLAC":
                if (inBirth) {
                    this.individual.setBirthPlace(line.getValue());
                } else if (inDeath) {
                    this.individual.setDeathPlace(line.getValue());
                }
                break;
        }
    }

    @Override
    public void store(TreeService treeService) {
        System.out.println("Individual: #" + this.individual.getId() + ", "
                + this.individual.getFirstNames() + " " + this.individual.getLastName());
        treeService.update(this.individual);
    }

    private void processName(String fullName) {
        // fullName is like "first name /lastname/", so split in "first name" and "lastname"
        int p1 = fullName.indexOf("/");
        String name = fullName.substring(0, p1-1).trim();
        this.individual.setFirstNames(name);
        int p2 = fullName.indexOf("/", p1+1);
        name = fullName.substring(p1+1, p2);
        if (name.startsWith("??")) {
            name = "??";
        }
        this.individual.setLastName(name);
    }
}
