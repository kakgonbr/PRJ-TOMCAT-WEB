package dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import model.Category;

public class CategoryDAO {
    
    public static class CategoryFetcher {
        public static final String GET_CHILD_CATEGORY = "WITH category AS (SELECT id FROM tblCategory WHERE id = ?1 UNION ALL SELECT c.id FROM tblCategory c JOIN category ch ON c.parent_id = ch.id)"; // Recursive CTE, get the current category AND ALL subcategories

        public static synchronized List<Category> getChildCategories(int parent) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                Query query = em.createNativeQuery(GET_CHILD_CATEGORY, Category.class).setParameter(1, parent);
                return query.getResultList();
            }
            catch (Exception e) {
                throw new java.sql.SQLException(e);
            } 
        }
        
        public static synchronized Category getCategoryDetails(int id) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                Category category = em.find(Category.class, id);

                // tell jpa to fetch stuff
                category.getImageStringResourceId();
                
                
                return category;
            }
            catch (Exception e) {
                throw new java.sql.SQLException(e);
            } 
        }
        
    }
}
