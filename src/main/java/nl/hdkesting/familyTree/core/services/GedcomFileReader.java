package nl.hdkesting.familyTree.core.services;

import nl.hdkesting.familyTree.core.gedcom.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;

@Service
public class GedcomFileReader {
    private final TreeService treeService;

    public GedcomFileReader(TreeService treeService) {
        this.treeService = treeService;
    }

    public boolean readFile(String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            return false;
        }

        File file = new File(resource.getFile());

        try (
                FileReader frdr = new FileReader(file);
                BufferedReader reader = new BufferedReader(frdr)
                ) {
            String line;
            GedcomReader objectReader = new Discarder(); // initialize with dummy value, saves a null-check
            while((line = reader.readLine()) != null) {
                PropertyLine pline = new PropertyLine(line);
                if (pline.getLevel() == 0) {
                    // store previous object
                    objectReader.store(this.treeService);

                    // create reader for this object
                    switch (pline.getValue()){
                        case "INDI":
                            objectReader = new IndividualReader(pline);
                            break;
                        case "FAM":
                            objectReader = new FamilyReader(pline);
                            break;
                        default:
                            objectReader = new Discarder();
                            break;
                    }
                } else {
                    objectReader.processNextLine(pline);
                }
            }

            return true;
        } catch (IOException ex) {
            // TODO real handling and/or logging
            ex.printStackTrace();
        } catch (Exception ex2) {
            ex2.printStackTrace();
        }

        return false;
    }
}
