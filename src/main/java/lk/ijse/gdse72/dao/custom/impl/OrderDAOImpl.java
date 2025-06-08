package lk.ijse.gdse72.dao.custom.impl;

import lk.ijse.gdse72.config.FactoryConfiguration;
import lk.ijse.gdse72.dao.custom.OrderDAO;
//import lk.ijse.gdse72.entity.Orders;
import lk.ijse.gdse72.entity.Orders;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OrderDAOImpl implements OrderDAO {
    private Session session;

    @Override
    public String getLastOrderCode() {
        try (Session session = FactoryConfiguration.getInstance().getSession()){
            String sql = "SELECT orderId FROM orders ORDER BY orderId DESC LIMIT 1";

            String lastOrderId = (String) session.createNativeQuery(sql).uniqueResult();
            System.out.println("After Query: " + lastOrderId);

            return lastOrderId;
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get last order code", e);
        }
    }

//    @Override
//    public boolean save(Orders entity) throws Exception {
//        Session currentSession = (session != null) ? session : FactoryConfiguration.getInstance().getSession();
//        Transaction transaction = null;
//        boolean shouldCloseSession = (session == null);
//
//        try {
//            if (session == null) {
//                transaction = currentSession.beginTransaction();
//            }
//
//            currentSession.persist(entity);
//
//            if (transaction != null) {
//                transaction.commit();
//            }
//            return true;
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            throw new Exception("Failed to save Order: " + e.getMessage(), e);
//        } finally {
//            if (shouldCloseSession && currentSession != null) {
//                currentSession.close();
//            }
//        }
//    }

    @Override
    public boolean save(Orders entity) throws Exception {
        try {
            session.persist(entity);  // use injected session directly
            return true;
        } catch (Exception e) {
            throw new Exception("Failed to save Order: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(Orders entity) throws Exception {
        Session currentSession = (session != null) ? session : FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        boolean shouldCloseSession = (session == null);

        try {
            if (session == null) {
                transaction = currentSession.beginTransaction();
            }

            currentSession.merge(entity);

            if (transaction != null) {
                transaction.commit();
            }
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new Exception("Failed to update Order: " + e.getMessage(), e);
        } finally {
            if (shouldCloseSession && currentSession != null) {
                currentSession.close();
            }
        }
    }

    @Override
    public boolean delete(String id) throws Exception {
        Session currentSession = (session != null) ? session : FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        boolean shouldCloseSession = (session == null);

        try {
            if (session == null) {
                transaction = currentSession.beginTransaction();
            }

            Orders order = currentSession.get(Orders.class, id);
            if (order != null) {
                currentSession.remove(order);
                if (transaction != null) {
                    transaction.commit();
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new Exception("Failed to delete Order: " + e.getMessage(), e);
        } finally {
            if (shouldCloseSession && currentSession != null) {
                currentSession.close();
            }
        }
    }

    @Override
    public Orders search(String id) throws Exception {
        Session currentSession = (session != null) ? session : FactoryConfiguration.getInstance().getSession();
        boolean shouldCloseSession = (session == null);

        try {
            return currentSession.get(Orders.class, id);
        } catch (Exception e) {
            throw new Exception("Failed to search Order: " + e.getMessage(), e);
        } finally {
            if (shouldCloseSession && currentSession != null) {
                currentSession.close();
            }
        }
    }

    @Override
    public List<Orders> getAll() throws Exception {
        Session currentSession = (session != null) ? session : FactoryConfiguration.getInstance().getSession();
        boolean shouldCloseSession = (session == null);

        try {
            return currentSession.createQuery("FROM Orders", Orders.class).list();
        } catch (Exception e) {
            throw new Exception("Failed to get all Orders: " + e.getMessage(), e);
        } finally {
            if (shouldCloseSession && currentSession != null) {
                currentSession.close();
            }
        }
    }

    @Override
    public void setSession(Session session) throws Exception {
        this.session = session;
    }
}
