package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import java.sql.SQLException;
import java.util.List;
import model.Variation;

public final class VariationDAO {

    public static final class VariationManager {

        public static synchronized void createVariation(Variation variation) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();
                    em.persist(variation);
                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    throw new SQLException(e);
                }
            }
        }

        private static final String DELETE_VARIATION = "UPDATE tblVariation SET status = 0 WHERE id = ?1";

        public static synchronized void deleteVariation(int id) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                em.createNativeQuery(DELETE_VARIATION).setParameter(1, id).executeUpdate();
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }

        public static synchronized void updateVariation(Variation variation) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();
                    Variation dbVariation = em.find(Variation.class, variation.getId());

                    dbVariation.setName(variation.getName());
                    dbVariation.setDatatype(variation.getDatatype());
                    dbVariation.setUnit(variation.getUnit());
                    dbVariation.setCategoryId(variation.getCategoryId());

                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    throw new SQLException(e);
                }
            }
        }
    }

    public static final class VariationFetcher {

        public static synchronized Integer getVariationIdByNameAndCategory(String name, int categoryId) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery(
                        "Variation.findByNameAndCategory", Integer.class)
                        .setParameter("name", name)
                        .setParameter("categoryId", categoryId)
                        .getSingleResult();
            } catch (NoResultException e) {
                return null;
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }

        public static synchronized Variation getVariation(int id) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("Variation.findById", Variation.class)
                        .setParameter("id", id)
                        .getSingleResult();
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }

        public static synchronized Variation getTopVariation(int id) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                Variation variation = em.createNamedQuery("Variation.findById", Variation.class)
                        .setParameter("id", id)
                        .getSingleResult();

                variation.getVariationValueList().size();

                return variation;
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }

        public static synchronized List<Variation> getVariations() throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("Variation.findAll", Variation.class).getResultList();
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }

        public static synchronized List<Variation> getVariationsByDatatype(String datatype) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("Variation.findByDatatype", Variation.class)
                        .setParameter("datatype", datatype)
                        .getResultList();
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }

        public static synchronized List<Variation> getVariationsByUnit(String unit) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("Variation.findByUnit", Variation.class)
                        .setParameter("unit", unit)
                        .getResultList();
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }
    }
}
