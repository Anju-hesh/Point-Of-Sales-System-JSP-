package lk.ijse.gdse72.dao.custom.impl;

import lk.ijse.gdse72.config.FactoryConfiguration;
import lk.ijse.gdse72.dao.custom.CustomerDAO;
import lk.ijse.gdse72.entity.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    private Session session;

    @Override
    public boolean save(Customer customer) throws Exception {
        Session currentSession = (session != null) ? session : FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        boolean shouldCloseSession = (session == null);

        try {
            transaction = currentSession.beginTransaction();
            currentSession.persist(customer);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Failed to save Customer", e);
        } finally {
            if (shouldCloseSession && currentSession != null) {
                currentSession.close();
            }
        }
    }

    @Override
    public boolean update(Customer customer) throws Exception{
        Session currentSession = (session != null) ? session : FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        boolean shouldCloseSession = (session == null);

        try {
            System.out.println("Before Merge In Try block DAOImpl update");
            transaction = currentSession.beginTransaction();
            Customer existingCustomer = currentSession.get(Customer.class, customer.getId());
            if (existingCustomer == null) return false;

            currentSession.merge(customer);
            System.out.println("After Merge In Try block DAOImpl update");
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Failed to update Customer", e);
        } finally {
            if (shouldCloseSession && currentSession != null) {
                currentSession.close();
            }
        }
    }

    @Override
    public boolean delete(String id) throws Exception{
        Session currentSession = (session != null) ? session : FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        boolean shouldCloseSession = (session == null);

        try {
//            System.out.println("Before Query In Try block DAOImpl");

            transaction = currentSession.beginTransaction();
            Query query = currentSession.createNativeQuery("DELETE FROM customer WHERE id = :customerId");
            query.setParameter("customerId", id);
            int result = query.executeUpdate();
            transaction.commit();

//            System.out.println("After Query In Try block DAOImpl");
            return result > 0;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Failed to delete Customer", e);
        } finally {
            if (shouldCloseSession && currentSession != null) {
                currentSession.close();
            }
        }
    }

//    @Override
//    public Customer get(String id) {
//        return null;
//    }

    @Override
    public Customer search(String id) throws Exception{
        Session currentSession = (session != null) ? session : FactoryConfiguration.getInstance().getSession();
        boolean shouldCloseSession = (session == null);

        try {
            return currentSession.get(Customer.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Customer by ID: " + id, e);
        } finally {
            if (shouldCloseSession && currentSession != null) {
                currentSession.close();
            }
        }
    }

    @Override
    public List<Customer> getAll() throws Exception{
        Session currentSession = FactoryConfiguration.getInstance().getSession();
//        boolean shouldCloseSession = (session == null);
        System.out.println(currentSession + "Current Session");

//        Transaction tx = null;

        try {
//            tx = currentSession.beginTransaction();
            System.out.println("Retrieving all customers from the database...daoimpl try Block before Query");

            List<Customer> customers = currentSession.createNativeQuery("SELECT * FROM customer", Customer.class).list();

            System.out.println("Retrieving all customers from the database...daoimpl try Block After Query");
//            List<Customer> customers = query.list();

//            tx.commit();
            return customers;

        } catch (Exception e) {
//            if (tx != null) tx.rollback();

            e.printStackTrace();
            return List.of();
        } finally {
            if (currentSession != null) {
                currentSession.close();
            }
        }
    }

    @Override
    public String getLastCustomerId() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        String lastId = null;

        try {
            transaction = session.beginTransaction();

            String sql = "SELECT id FROM customer ORDER BY id DESC LIMIT 1";
            lastId = (String) session.createNativeQuery(sql).uniqueResult();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }

        return lastId;
    }


    @Override
    public void setSession(Session session) throws Exception {
        this.session = session;
    }
}