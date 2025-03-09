/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.sql.SQLException;
import model.Shop;
import service.DatabaseConnection;

/**
 *
 * @author hoahtm
 */
public class ShopDAO {

    public static final class ShopManager {

        public static synchronized void createShop(Shop shop) throws SQLException {
            try (EntityManager em = DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();
                try {
                    et.begin();
                    em.persist(shop);
                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    throw new SQLException(e);
                }
            }
        }

        private static final String DELETE_SHOP = "UPDATE tblShop SET status = 0 WHERE id = ?1";

        public static synchronized void deleteShop(int id) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                em.createNativeQuery(DELETE_SHOP).setParameter(1, id).executeUpdate();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        }

        public static synchronized void updateShop(Shop shop) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    Shop dbShop = em.find(Shop.class, shop.getId());

                    if (dbShop != null) {
                        dbShop.setName(shop.getName());
                        dbShop.setAddress(shop.getAddress());
                        dbShop.setVisible(shop.getVisible());
                        dbShop.setProfileStringResourceId(shop.getProfileStringResourceId());
                        dbShop.setOwnerId(shop.getOwnerId());
                        dbShop.setStatus(shop.isStatus());
                    }

                    et.commit();

                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    throw new java.sql.SQLException(e);
                }
            }
        }
    }
    
    public static final class ShopFetcher {
        public static synchronized model.Shop getShop(int id) throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("Shop.findById", model.Shop.class).setParameter("id", id).getSingleResult();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static model.Shop getShop

        public static synchronized model.Shop getShopFromShopName(String shopName)
                throws SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("Shop.findByName", model.Shop.class).setParameter("name", shopName).getSingleResult();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static model.Shop getShop
        
        public static synchronized java.util.List<model.Shop> getShops()
                throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("Shop.findAll", model.Shop.class).getResultList();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static synchronized java.util.List<model.Shop> getShops
    }
}
