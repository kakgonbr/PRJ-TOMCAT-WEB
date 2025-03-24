package dao;

import java.util.List;

import org.hibernate.Hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import model.Category;

public class CategoryDAO {
    
    public static class CategoryFetcher {
        public static final String GET_CHILD_CATEGORY = "WITH category AS (SELECT tblCategory.* FROM tblCategory WHERE id = ?1 UNION ALL SELECT c.* FROM tblCategory c JOIN category ch ON c.parent_id = ch.id) SELECT * FROM category"; // Recursive CTE, get the current category AND ALL subcategories

        public static synchronized List<Category> getChildCategories(int parent) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                Query query = em.createNativeQuery(GET_CHILD_CATEGORY, Category.class).setParameter(1, parent);
                return query.getResultList();
            }
            catch (Exception e) {
                throw new java.sql.SQLException(e);
            } 
        }

        public static synchronized List<Category> getAllCategories() throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("Category.findAll", Category.class).getResultList();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        }

        // public static synchronized List<Category> getCategoryHierarchy() throws java.sql.SQLException {
        //     try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
        //         java.util.List<Category> categories = em.createNamedQuery("Category.findAll", Category.class).getResultList();

        //         categories.forEach(Category::getCategoryList);

        //         return categories;
        //     } catch (Exception e) {
        //         throw new java.sql.SQLException(e);
        //     }
        // } // public static synchronized List<Category> getCategoryHierarchy

        // recursive!!!
        private static synchronized void initCategory(Category category) {
            if (category == null) return;

            // make sure entity manager is still active
            Hibernate.initialize(category.getCategoryList());

            for (Category subCategory : category.getCategoryList()) {
                initCategory(subCategory);
            }
        }

        public static synchronized Category getTopCategory(int id) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                Category category = em.createNamedQuery("Category.findById", Category.class).setParameter("id", id).getSingleResult();

                initCategory(category);
                
                return category;
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        } // public static synchronized Category getTopCategory 

        public static synchronized Category getTopCategory() throws java.sql.SQLException {
            return getTopCategory(0);
        }
        
        public static synchronized Category getCategoryDetails(int id) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                Category category = em.find(Category.class, id);

                // tell jpa to fetch stuff
                Hibernate.initialize(category.getImageStringResourceId());
                initCategory(category);
                
                return category;
            }
            catch (Exception e) {
                throw new java.sql.SQLException(e);
            } 
        } // public static synchronized Category getCategoryDetails
        
        public static synchronized Category getCategory(int id) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                return em.createNamedQuery("Category.findById", Category.class).setParameter("id", id).getSingleResult();
            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        }
    }
}
