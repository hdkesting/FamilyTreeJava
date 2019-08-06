package nl.hdkesting.familyTree.infrastructure.repositories;

import nl.hdkesting.familyTree.infrastructure.models.Individual;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.sql.Date;
import java.util.Iterator;
import java.util.List;

public class Temporary {
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

            Individual me = new Individual();
            me.setBirthDate(new Date(61, 4, 13)); // obsolete ctor, but use anyway in this dummy code
            me.setBirthPlace("Amsterdam");
            me.setFirstNames("Hans Douwe");
            me.setLastName("Kesting");
            me.setSex('M');
            me.setId(-1l);
            System.out.println("------- saving --------");
            session.save(me); // NB will crash when record (with this key) already exists
            tx.commit();

            System.out.println("------- querying --------");
            tx = session.beginTransaction();
            List<Individual> people = session.createQuery("From Individual").list();
            System.out.println("==> # of persons: " + people.size());

            for (Individual person: people) {
                System.out.println("==> " + person.getFirstNames() + " /" + person.getLastName() + "/");
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
}
