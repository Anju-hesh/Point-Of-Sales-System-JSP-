package lk.ijse.gdse72.dao.custom.impl;

import lk.ijse.gdse72.config.FactoryConfiguration;
import lk.ijse.gdse72.dao.custom.OrderDetaiDAO;
import lk.ijse.gdse72.entity.OrderDetail;
import lk.ijse.gdse72.entity.OrderDetailPK;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class OrderDetailDAOImpl implements OrderDetaiDAO {

    private Session session;

//    @Override
//    public boolean save(OrderDetail entity) throws Exception {
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
//            throw new Exception("Failed to save OrderDetail: " + e.getMessage(), e);
//        } finally {
//            if (shouldCloseSession && currentSession != null) {
//                currentSession.close();
//            }
//        }
//    }

    @Override
    public boolean save(OrderDetail entity) throws Exception {
        if (session == null) {
            throw new Exception("Session has not been set!");
        }
        try {
            session.persist(entity);
            return true;
        } catch (Exception e) {
            throw new Exception("Failed to save OrderDetail: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(OrderDetail entity) throws Exception {
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
            throw new Exception("Failed to update OrderDetail: " + e.getMessage(), e);
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

            OrderDetail orderDetail = currentSession.get(OrderDetail.class, id);
            if (orderDetail != null) {
                currentSession.remove(orderDetail);
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
            throw new Exception("Failed to delete OrderDetail: " + e.getMessage(), e);
        } finally {
            if (shouldCloseSession && currentSession != null) {
                currentSession.close();
            }
        }
    }

    @Override
    public OrderDetail search(String s) throws Exception {
        return null;
    }

    @Override
    public OrderDetail searchOD(String orderId, String itemCode) throws Exception {
        Session currentSession = (session != null) ? session : FactoryConfiguration.getInstance().getSession();
        boolean shouldCloseSession = (session == null);

        try {
            OrderDetailPK pk = new OrderDetailPK(orderId, itemCode);
            return currentSession.get(OrderDetail.class, pk);
        } catch (Exception e) {
            throw new Exception("Failed to search OrderDetail: " + e.getMessage(), e);
        } finally {
            if (shouldCloseSession && currentSession != null) {
                currentSession.close();
            }
        }
    }


    @Override
    public List<OrderDetail> getAll() throws Exception {
        Session currentSession = (session != null) ? session : FactoryConfiguration.getInstance().getSession();
        boolean shouldCloseSession = (session == null);

        try {
            Query<OrderDetail> query = currentSession.createQuery("FROM OrderDetail", OrderDetail.class);
            return query.list();
        } catch (Exception e) {
            throw new Exception("Failed to get all OrderDetails: " + e.getMessage(), e);
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
