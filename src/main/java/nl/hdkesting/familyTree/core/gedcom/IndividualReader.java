package nl.hdkesting.familyTree.core.gedcom;

import nl.hdkesting.familyTree.core.dto.IndividualDto;
import nl.hdkesting.familyTree.core.services.TreeService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IndividualReader implements GedcomReader {
    private final IndividualDto individual;
    private static final Pattern idPattern = Pattern.compile("\\w@I(\\d+)@\\w");

    private boolean inBirth;
    private boolean inDeath;

    public IndividualReader(String line) {
        // line: "0 @I123@ INDI"
        this.individual = new IndividualDto();

        Matcher matcher = idPattern.matcher(line);
        if (matcher.find()) {
            String match = matcher.group(); // " @I123@ "
            match = match.substring(3, matcher.end()-3); // "123"
            this.individual.setId(Long.parseLong(match));
        } // else problem!
    }

    @Override
    public void processNextLine(String line) {
        // TODO
        // line = "1 BIRT" of "2 DATE 11 Oct 1691": level keyword value

        // on "1 BIRT" remember that, but reset on next "1 ..." (same for DEAT)
        // then a "2 DATE ..." or "2 PLAC ..." can be stored in birthDate or birthPlace
    }

    @Override
    public void store(TreeService treeService) {
        treeService.update(this.individual);
    }
}
