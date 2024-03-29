package nl.hdkesting.familyTree.core.services;

import nl.hdkesting.familyTree.core.gedcom.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;

/**
 * Reader for a GEDCOM file. The various objects will be stored through the supplied TreeService.
 */
@Service
public class GedcomFileReader {
    private final TreeService treeService;

    /**
     * Creates a new reader.
     * @param treeService
     */
    public GedcomFileReader(TreeService treeService) {
        if (treeService == null) throw new IllegalArgumentException("treeService may not be null.");
        this.treeService = treeService;
    }

    /**
     * Reads the file from a resource, as specified by the path.
     * @param path
     * @return true if it was successful.
     */
    @Deprecated(since = "Supply a reader instead of a path")
    public boolean readFile(String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            return false;
        }

        File file = new File(resource.getFile());

        try (
                FileReader frdr = new FileReader(file);
                ) {
            return readFile(frdr);
        } catch (Exception ex) {
            // TODO real handling and/or logging
            ex.printStackTrace();
        }

        return false;
    }

    /**
     * Reads the file from some initialized "Reader".
     * @param basicreader
     * @return
     */
    public boolean readFile(Reader basicreader) {
        if (basicreader == null) {
            throw new IllegalArgumentException("basicreader cannot be null");
        }

        /* assumptions:
            - the database is empty or was loaded from this same file: an ID is unique only within one file.
            - the INDI records go first, so that the spouse and child references in the FAM records point to existing db records.
         */

        try (
                BufferedReader reader = new BufferedReader(basicreader)
        ) {
            String line;
            GedcomReader objectReader = new Discarder(); // initialize with dummy value, saves a null-check
            while((line = reader.readLine()) != null) {
                PropertyLine pline = new PropertyLine(line);
                if (pline.getLevel() == 0) {
                    // store previous object
                    objectReader.store(this.treeService);

                    // create reader for this object: "keyword" = index, "value" = object-keyword
                    switch (pline.getValue()){
                        case "INDI":
                            objectReader = new IndividualReader(pline);
                            break;
                        case "FAM":
                            objectReader = new FamilyReader(pline);
                            break;
                        default:
                            // HEAD, TRLR, SOUR, NOTE, ...
                            objectReader = new Discarder();
                            break;
                    }
                } else {
                    // any other level: continue filling current object
                    objectReader.processNextLine(pline);
                }
            }

            // and store the last object (although that would be an ignored TRLR in a real file)
            objectReader.store(this.treeService);

            // the whole file was read without issues: done!
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
