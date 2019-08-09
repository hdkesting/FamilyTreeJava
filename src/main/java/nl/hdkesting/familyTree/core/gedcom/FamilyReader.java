package nl.hdkesting.familyTree.core.gedcom;

import nl.hdkesting.familyTree.core.dto.FamilyDto;
import nl.hdkesting.familyTree.core.services.TreeService;

import java.util.ArrayList;

/**
 * Reads (and stores) a FAM (family) record.
 */
public class FamilyReader implements GedcomReader {
    private final FamilyDto family = new FamilyDto();
    private final ArrayList<Long> spouses = new ArrayList<>();
    private final ArrayList<Long> children = new ArrayList<>();

    private boolean inMarriage = false;
    private boolean inDivorce = false;

    public FamilyReader(PropertyLine line) {
        // assume line is like: "0 @F123@ FAM"
        this.family.setId(GedcomUtil.getIdFromReference(line.getKeyword()));
    }

    @Override
    public void processNextLine(PropertyLine line) {
        if (line.getLevel() == 1) {
            inMarriage = false;
            inDivorce = false;
        }

        switch (line.getKeyword()) {
            case "MARR":
                inMarriage = true;
                break;
            case "DIV":
                inDivorce = true;
                break;
            case "DATE":
                GedcomDate dt = GedcomDate.parse(line.getValue());
                if (dt != null) {
                    if (inMarriage) {
                        this.family.setMarriageDate(dt.getDate());
                    } else if (inDivorce) {
                        this.family.setDivorceDate(dt.getDate());
                    }
                }
                break;
            case "PLAC":
                if (inMarriage) {
                    this.family.setMarriagePlace(line.getValue());
                } else if (inDivorce) {
                    this.family.setDivorcePlace(line.getValue());
                }
                break;
            case "CHIL":
                // 	1 CHIL @I321@
                long cid = GedcomUtil.getIdFromReference(line.getValue());
                this.children.add(cid);
                break;
            case "WIFE":
                // fall through to HUSB
            case "HUSB":
                long sid = GedcomUtil.getIdFromReference(line.getValue());
                this.spouses.add(sid);
                break;
        }
    }

    @Override
    public void store(TreeService treeService) {
        System.out.println("Family: #" + this.family.getId());

        treeService.update(this.family);
        treeService.updateRelations(this.family.getId(), this.spouses, this.children);
    }
}
