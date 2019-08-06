package nl.hdkesting;

import nl.hdkesting.familyTree.Test2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FamilyTreeApplication {
	private static SessionFactory factory;
	private static ServiceRegistry registry;

	public static void main(String[] args) {
		SpringApplication.run(FamilyTreeApplication.class, args);

		setupHibernate();

		var sut = new Test2();
		sut.doit();
	}

	public static Session getSession() {
		return factory.openSession();
	}

	private static void setupHibernate(){
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

		System.out.println("-------- done init -----------");
	}
}
