package nl.hdkesting.familyTree.core.services;

import nl.hdkesting.familyTree.core.gedcom.Discarder;
import nl.hdkesting.familyTree.core.gedcom.FamilyReader;
import nl.hdkesting.familyTree.core.gedcom.GedcomReader;
import nl.hdkesting.familyTree.core.gedcom.IndividualReader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
public class GedcomFileReader {
    private final TreeService treeService;

    public GedcomFileReader(TreeService treeService) {
        this.treeService = treeService;
    }

    public void ReadFile(String path) {
        try (
                FileReader frdr = new FileReader(path);
                BufferedReader reader = new BufferedReader(frdr)
                ) {
            String line;
            GedcomReader objectReader = new Discarder();
            while((line = reader.readLine()) != null) {
                if (line.startsWith("0 ")) {
                    // store previous object
                    objectReader.store(this.treeService);

                    // create different reader
                    if (line.endsWith("INDI")) {
                        objectReader = new IndividualReader(line);
                    } else if (line.endsWith("FAM")) {
                        objectReader = new FamilyReader(line);
                    } else {
                        objectReader = new Discarder();
                    }
                } else {
                    objectReader.processNextLine(line);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
