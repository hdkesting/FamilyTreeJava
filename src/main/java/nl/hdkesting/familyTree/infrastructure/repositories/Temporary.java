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
        try {
            Configuration conf = new Configuration().configure();
            registry = new StandardServiceRegistryBuilder()
                    .applySettings(conf.getProperties())
                    .build();
            factory = conf.buildSessionFactory(registry);
        } catch (Throwable ex) {
            System.err.println("Failed to create session factory object: " + ex);
            throw new ExceptionInInitializerError(ex);
        }

        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Individual me = new Individual();
            me.setBirthDate(new Date(1961, 4, 13)); // obsolete ctor, but use anyway in this dummy code
            me.setBirthPlace("Amsterdam");
            me.setFirstNames("Hans Douwe");
            me.setLastName("Kesting");
            me.setId(-1l);
            session.save(me);

            List people = session.createQuery("FROM individual").list();
            for (Iterator iterator = people.iterator(); iterator.hasNext();) {
                Individual person = (Individual)iterator.next();
                System.out.println(person.getFirstNames() + " " + person.getLastName());
            }
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) tx.rollback();

            System.err.println("Failed to use session: " + ex);
            ex.printStackTrace();
        } finally {
            session.close();
        }

        StandardServiceRegistryBuilder.destroy(registry);
    }
}
