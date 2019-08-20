package nl.hdkesting.familyTree.infrastructure.repositories;

import nl.hdkesting.familyTree.infrastructure.models.Family;
import nl.hdkesting.familyTree.infrastructure.models.Individual;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Test1 {
    private static SessionFactory factory;
    private static ServiceRegistry registry;

    public static void main(String[] args) {
        System.out.println("------- initializing --------");
        try {
            Configuration conf = new Configuration().configure();
            conf.addAnnotatedClass(nl.hdkesting.familyTree.infrastructure.models.Family.class);
            conf.addAnnotatedClass(nl.hdkesting.familyTree.infrastructure.models.Individual.class);
            System.out.println("------- registry --------");
            registry = new StandardServiceRegistryBuilder()
                    .applySettings(conf.getProperties())
                    .build();
            System.out.println("------- factory --------");
            factory = conf.buildSessionFactory(registry);
        } catch (Throwable ex) {
            System.err.println("Failed to create session factory object: " + ex);
            throw new ExceptionInInitializerError(ex);
        }

        System.out.println("------- opening session --------");
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Individual me = getPerson(session, -1);

            me.setBirthDate(1961, 5-1, 13);
            me.birthPlace = "Amsterdam";
            me.firstNames = "Hans Douwe";
            me.lastName = "Kesting";
            me.sex='M';

            System.out.println("------- saving --------");
            session.save(me); // NB will crash when record (with this key) already exists

            Individual paula = getPerson(session, -2);
            paula.setBirthDate(1963, 2-1, 18);
            paula.firstNames="Paula";
            paula.lastName="Andringa";
            paula.sex='F';
            session.save(paula);

            Family us = getFamily(session, -1);
            us.setMarriageDate(1996, 4, 12);
            Set<Individual> spouses = us.getSpouses();
            if (spouses == null) {
                us.setSpouses(new HashSet<Individual>());
                spouses = us.getSpouses();
            }

            if (spouses.size() == 0) {
                spouses.add(me);
                spouses.add(paula);
            }
            session.save(us);

            tx.commit();

            System.out.println("------- querying --------");
            tx = session.beginTransaction();
            List<Individual> people = session.createQuery("From Individual").list();
            System.out.println("==> # of persons: " + people.size());

            for (Individual person: people) {
                System.out.println("==> " + person.firstNames + " /" + person.lastName + "/");
            }

            System.out.println("------- committing --------");
            tx.commit();
        } catch (HibernateException ex) {
            System.out.println("------- rolling back --------");
            if (tx != null) tx.rollback();

            System.err.println("Failed to use session: " + ex);
            ex.printStackTrace();
        } finally {
            System.out.println("------- closing session --------");
            session.close();
        }

        System.out.println("------- ending --------");
        StandardServiceRegistryBuilder.destroy(registry);
        System.out.println("------- exit --------");
    }

    private static Individual getPerson(Session session, long id) {
        Query<Individual> qry = session.createQuery("From Individual Where id=:id");
        qry.setParameter("id", id);
        Individual res = qry.uniqueResult();

        if (res == null) {
            res = new Individual();
            res.id=id;
        }

        return res;
    }

    private static Family getFamily(Session session, long id) {
        Query<Family> qry = session.createQuery("From Family Where id=:id");
        qry.setParameter("id", id);
        Family res = qry.uniqueResult();

        if (res == null) {
            res = new Family();
            res.setId(id);
        }

        return res;
    }
}
