package dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import model.Category;

public class CategoryDAO {
    
    public static class CategoryFetcher {

        public static final String GET_CHILD_CATEGORY = "SELECT *\r\n" + //
                        "FROM tblCategory c\r\n" + //
                        "WHERE c.parent_id = ?1";

        public static synchronized List<Category> getChildCategories(int categoryId) throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                Query query = em.createNativeQuery(GET_CHILD_CATEGORY, Category.class).setParameter(1, categoryId);
                return query.getResultList();
            }
            catch (Exception e) {
                throw new java.sql.SQLException(e);
            } 
        }
        
    }
}
