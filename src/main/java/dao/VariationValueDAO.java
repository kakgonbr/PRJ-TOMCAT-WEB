/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import java.sql.SQLException;
import java.util.List;
import model.VariationValue;

public final class VariationValueDAO {

    public static final class VariationValueManager {

        public static synchronized void createVariationValue(VariationValue variationValue) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();
                try {
                    et.begin();
                    em.persist(variationValue);
                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    throw new SQLException(e);
                }
            }
        }

        private static final String DELETE_VARIATION_VALUE = "UPDATE tblVariationValue SET status = 0 WHERE id = ?1";

        public static synchronized void deleteVariationValue(int id) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                em.createNativeQuery(DELETE_VARIATION_VALUE).setParameter(1, id).executeUpdate();
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }

        public static synchronized void updateVariationValue(VariationValue variationValue) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();
                try {
                    et.begin();
                    VariationValue dbVariationValue = em.find(VariationValue.class, variationValue.getId());
                    dbVariationValue.setValue(variationValue.getValue());
                    dbVariationValue.setVariationId(variationValue.getVariationId());
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

    public static final class VariationValueFetcher {

        public static synchronized VariationValue getVariationValue(int id) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("VariationValue.findById", VariationValue.class)
                        .setParameter("id", id)
                        .getSingleResult();
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }

        public static synchronized List<VariationValue> getVariationValuesByVariationId(int variationId) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("VariationValue.findByVariationId", VariationValue.class)
                        .setParameter("variationId", variationId)
                        .getResultList();
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }

        public static synchronized VariationValue getVariationValueByValue(String value) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("VariationValue.findByValue", VariationValue.class)
                        .setParameter("value", value)
                        .getSingleResult();
            } catch (NoResultException e) {
                return null;
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }

        public static synchronized VariationValue getVariationValueByValueAndVariation(String value, int variationId) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("VariationValue.findByValueAndVariation", VariationValue.class)
                        .setParameter("value", value)
                        .setParameter("variationId", variationId)
                        .getSingleResult();
            } catch (NoResultException e) {
                return null;
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }

        public static synchronized List<VariationValue> getAllVariationValues() throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("VariationValue.findAll", VariationValue.class)
                        .getResultList();
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }
    }
}
