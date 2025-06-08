package lk.ijse.gdse72.dao.custom.impl;

import lk.ijse.gdse72.config.FactoryConfiguration;
import lk.ijse.gdse72.dao.custom.ItemDAO;
import lk.ijse.gdse72.entity.Item;
import org.hibernate.Session;

import java.util.List;

public class ItemDAOImpl implements ItemDAO {

    private Session session;

    @Override
    public boolean save(Item entity) throws Exception {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            session.beginTransaction();
            System.out.println("Before persist in ItemDAOImpl.save()");
            session.persist(entity);
            System.out.println("After persist in ItemDAOImpl.save()");
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save Item", e);
        }
    }

//    @Override
//    public boolean update(Item entity) throws Exception {
//        try (Session session = FactoryConfiguration.getInstance().getSession()) {
//            session.beginTransaction();
//            System.out.println("Before merge in ItemDAOImpl.update()");
//            session.merge(entity);
//            System.out.println("After merge in ItemDAOImpl.update()");
//            session.getTransaction().commit();
//            return true;
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to update Item", e);
//        }
//    }
@Override
public boolean update(Item entity) throws Exception {
    if (session == null) {
        throw new Exception("Session has not been set!");
    }
    try {
        System.out.println("Before merge in ItemDAOImpl.update()");
        session.merge(entity);
        session.flush();
        System.out.println("After merge in ItemDAOImpl.update()");
        return true;
    } catch (Exception e) {
        throw new Exception("Failed to update Item: " + e.getMessage(), e);
    }
}


    @Override
    public boolean delete(String s) throws Exception {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            session.beginTransaction();
            System.out.println("Before delete in ItemDAOImpl.delete()");
            Item item = session.get(Item.class, s);
            if (item != null) {
                session.remove(item);
                System.out.println("After delete in ItemDAOImpl.delete()");
                session.getTransaction().commit();
                return true;
            } else {
                session.getTransaction().rollback();
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete Item", e);
        }
    }

//    @Override
//    public Item search(String s) throws Exception {
//        try (Session session = FactoryConfiguration.getInstance().getSession()) {
//            session.beginTransaction();
//            System.out.println("Before Query in ItemDAOImpl.search()");
//            Item item = session.get(Item.class, s);
//            System.out.println("After Query in ItemDAOImpl.search(): " + item);
//            session.getTransaction().commit();
//            return item;
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to search Item", e);
//        }
//    }

    @Override
    public Item search(String s) throws Exception {
        try {
            System.out.println("Before Query in ItemDAOImpl.search()");
            Item item = session.get(Item.class, s);
            System.out.println("After Query in ItemDAOImpl.search(): " + item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Failed to search Item", e);
        }
    }

    @Override
    public List<Item> getAll() throws Exception {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            session.beginTransaction();
            System.out.println("Before Query in ItemDAOImpl.getAll()");
            List<Item> items = session.createNativeQuery("SELECT * FROM item", Item.class).list();
            System.out.println("After Query in ItemDAOImpl.getAll(): " + items.size());
            session.getTransaction().commit();
            return items;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all items", e);
        }
    }

    @Override
    public void setSession(Session session) throws Exception {
        this.session = session;
    }

    @Override
    public String getLastItemCode() {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            System.out.println("before Query");

            String sql = "SELECT code FROM item ORDER BY code DESC LIMIT 1";

            String lastCode = (String) session.createNativeQuery(sql).uniqueResult();
            System.out.println("after Query: " + lastCode);

            return lastCode;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get last item code", e);
        }
    }

}
