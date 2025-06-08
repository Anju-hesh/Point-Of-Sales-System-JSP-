package lk.ijse.gdse72.config;

import lk.ijse.gdse72.entity.Customer;
import lk.ijse.gdse72.entity.Item;
import lk.ijse.gdse72.entity.OrderDetail;
import lk.ijse.gdse72.entity.Orders;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FactoryConfiguration {
    private static FactoryConfiguration factoryConfiguration;
    private static SessionFactory sessionFactory;

    private FactoryConfiguration() {
        try {
            Configuration configuration = new Configuration();
            Properties properties = new Properties();

            InputStream input = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("hibernate.properties");

            if (input == null) {
                throw new RuntimeException("hibernate.properties file not found in classpath.");
            }

            properties.load(input);
            configuration.setProperties(properties);

            configuration.addAnnotatedClass(Customer.class);
            configuration.addAnnotatedClass(Item.class);
            configuration.addAnnotatedClass(Orders.class);
            configuration.addAnnotatedClass(OrderDetail.class);
//            configuration.addAnnotatedClass(OrderDetail.OrderDetailPK.class);

            System.out.println("Building SessionFactory...");
            sessionFactory = configuration.buildSessionFactory();
            System.out.println("SessionFactory built successfully!");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (sessionFactory != null) {
                    sessionFactory.close();
                }
            }));

        } catch (IOException e) {
            System.err.println("Failed to load hibernate properties: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Hibernate configuration", e);
        } catch (Exception e) {
            System.err.println("Error initializing Hibernate: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Hibernate initialization error", e);
        }
    }

    public static synchronized FactoryConfiguration getInstance() {
        if (factoryConfiguration == null) {
            factoryConfiguration = new FactoryConfiguration();
        }
        return factoryConfiguration;
    }

    public Session getSession() {
        if (sessionFactory == null) {
            throw new RuntimeException("SessionFactory is not initialized");
        }
        return sessionFactory.openSession();
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
        factoryConfiguration = null;
    }
}
