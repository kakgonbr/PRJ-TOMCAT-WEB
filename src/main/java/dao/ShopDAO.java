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
                    System.out.println("check");
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    throw new SQLException(e);
                }
            }
        }
    }
}
