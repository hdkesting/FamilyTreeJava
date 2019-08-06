package nl.hdkesting.familyTree;

import nl.hdkesting.FamilyTreeApplication;
import nl.hdkesting.familyTree.core.dto.IndividualDto;
import nl.hdkesting.familyTree.core.services.TreeService;
import nl.hdkesting.familyTree.infrastructure.models.Individual;
import nl.hdkesting.familyTree.infrastructure.repositories.IndividualRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class Test2 {
    @Autowired
    private TreeService treeService;

    public void doit() {
        Session session = FamilyTreeApplication.getSession();

        Transaction tx = null;

        try {
            IndividualDto me = new IndividualDto();
            me.setId(-1L);
            me.setFirstNames("Hans");
            me.setLastName("Kesting");

            treeService.update(me);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
