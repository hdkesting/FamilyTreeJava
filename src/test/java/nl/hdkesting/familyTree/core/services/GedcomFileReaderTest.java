package nl.hdkesting.familyTree.core.services;

import nl.hdkesting.familyTree.core.interfaces.MyFamilyRepository;
import nl.hdkesting.familyTree.core.interfaces.MyIndividualRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.Reader;
import java.io.StringReader;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.atLeast;

@RunWith(MockitoJUnitRunner.class)
public class GedcomFileReaderTest {
    private TreeService treeService;
    private MyFamilyRepository familyRepository;
    private MyIndividualRepository individualRepository;

    @Before
    public void beforeEachTest() {
        this.familyRepository = Mockito.mock(MyFamilyRepository.class);
        this.individualRepository = Mockito.mock(MyIndividualRepository.class);
        this.treeService = new TreeService(this.familyRepository, this.individualRepository);
    }

    @Test
    @Ignore("Reads full! resource! file")
    public void readFile_ObsoleteResourcePath() {
        boolean success = this.treeService.load("source/sampleFamily.ged");
        assert success;
        verify(individualRepository, atLeast(1)).findById(2L, true);
        verify(familyRepository, atLeast(1)).findById(anyLong());
    }

    @Test
    public void readFile_SingleIndi() {
        String gedcom = "0 @I10@ INDI\n" +
                "1 NAME Frederik /Kesting/\n" +
                "1 SEX M\n" +
                "1 BIRT\n" +
                "2 DATE 23 Dec 1893\n" +
                "2 PLAC Amsterdam\n" +
                "1 DEAT\n" +
                "2 DATE 11 Jul 1972\n" +
                "2 PLAC Amsterdam\n" +
                "2 _STAT Dead\n" +
                "1 REFN 10\n" +
                "1 OCCU \n" +
                "2 NOTE Oprichter Handelsonderneming Zilco Bakkerijgereedschap en \n" +
                "3 CONT chocoladevormen\n" +
                "1 CHAN\n" +
                "2 DATE 26 Jul 1998\n" +
                "3 TIME 17:42\n" +
                "1 FAMS @F9@\n" +
                "1 FAMC @F8@\n";
        Reader reader = new StringReader(gedcom);

        boolean success = this.treeService.load(reader);

        assert success;

        verify(individualRepository).findById(10L, true);
        verifyZeroInteractions(familyRepository);
    }

    @Test
    @Ignore("just to test that testing works")
    public void redTest() {
        assert false;
    }

    @Test
    public void greenTest() {
        assert true;
    }

}
