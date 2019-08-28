package nl.hdkesting.familyTree.core.services;

import nl.hdkesting.familyTree.core.interfaces.MyFamilyRepository;
import nl.hdkesting.familyTree.core.interfaces.MyIndividualRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

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
    public void readFile() {
        this.treeService.load("source/sampleFamily.ged");

        Mockito.verify(familyRepository).findById(1L);
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
