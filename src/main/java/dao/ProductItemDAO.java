/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.math.BigDecimal;
import model.ProductItem;
import service.DatabaseConnection;

/**
 *
 * @author hoahtm
 */
public class ProductItemDAO {

    public static class productItemManager {

        public static synchronized void editProductItem(int productItemId, int stock, BigDecimal price) throws java.sql.SQLException {
            try (EntityManager em = DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    ProductItem productItem = em.find(ProductItem.class, productItemId);
                    if (productItem != null) {
                        productItem.setStock(stock);
                        productItem.setPrice(price);
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

        public static synchronized void addCustomizations(int productItemId,
                java.util.List<model.ProductCustomization> customizations) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    model.ProductItem productItem = em.find(model.ProductItem.class, productItemId);
                    if (productItem == null) {
                        throw new java.sql.SQLException("ProductItem not found: " + productItemId);
                    }

                    for (final model.ProductCustomization customization : customizations) {
                        model.VariationValue variationValue = customization.getVariationValueId();

                        if (em.find(model.Variation.class, variationValue) == null) {
                            continue;
                        }

                        em.persist(variationValue);
                        customization.setProductItemId(productItem);
                        em.persist(customization);
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

}
