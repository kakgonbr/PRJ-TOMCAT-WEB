package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.transaction.Transactional;

/**
 * Get and make mappings of resources to system path. Note that the systemPath
 * path is <strong>relative</strong> to <code>/prj/resources</code>
 */
public class ResourceDAO {
    public static synchronized String getPath(String name) throws java.sql.SQLException {
        try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
            return em.createNamedQuery("ResourceMap.findById", model.ResourceMap.class).setParameter("id", name)
                    .getSingleResult().getSystemPath();
        } catch (Exception e) {
            throw new java.sql.SQLException(e);
        }
    }

    public static synchronized java.util.List<model.ResourceMap> getAllResources() throws java.sql.SQLException {
        try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
            return em.createNamedQuery("ResourceMap.findAll", model.ResourceMap.class).getResultList();
        } catch (Exception e) {
            throw new java.sql.SQLException(e);
        }
    }

    public static synchronized void addMapping(String name, String path) throws java.sql.SQLException {
        try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
            EntityTransaction et = em.getTransaction();

            try {
                et.begin();

                model.ResourceMap map = new model.ResourceMap();
                map.setId(name);
                map.setSystemPath(path);

                em.persist(map);

                et.commit();
            } catch (Exception e) {
                if (et.isActive()) {
                    et.rollback();
                }

                throw new java.sql.SQLException(e);
            }
        }
    }

    public static synchronized void editMapping(model.ResourceMap map) throws java.sql.SQLException {
        try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
            EntityTransaction et = em.getTransaction();

            try {
                et.begin();

                model.ResourceMap dbMap = em.find(model.ResourceMap.class, map.getId());

                dbMap.setSystemPath(map.getSystemPath());

                et.commit();
            } catch (Exception e) {
                if (et.isActive()) {
                    et.rollback();
                }

                throw new java.sql.SQLException(e);
            }
        }
    } // public static synchronized void editMapping

    public static synchronized void deleteMapping(String id) throws java.sql.SQLException {
        try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
            EntityTransaction et = em.getTransaction();

            try {
                et.begin();

                em.remove(em.find(model.ResourceMap.class, id));

                et.commit();
            } catch (Exception e) {
                if (et.isActive()) {
                    et.rollback();
                }

                throw new java.sql.SQLException(e);
            }
        }
    } // public static synchronized void editMapping
}
