package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.Product;

public class ProductDAO {
    public static class ProductFetcher {
        private static final String GET_PRODUCTS = "SELECT * FROM tblProduct";

        public static synchronized java.util.List<Product> getProducts(Connection connection) throws SQLException {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(GET_PRODUCTS);

                java.util.List<Product> products = new java.util.ArrayList<>();

                while (rs.next()) {
                    products.add(Product.fromResultSet(rs));
                }

                return products;
            }
        } // public static synchronized java.util.List<Product> getProducts

        private static final String GET_PRODUCTS_BY_ID = "SELECT * FROM tblProduct WHERE id = ?";
        
        public static synchronized Product getProduct(Connection connection, int id) throws SQLException {
            try (PreparedStatement ps = connection.prepareStatement(GET_PRODUCTS_BY_ID)) {
                ps.setInt(1, id);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    return Product.fromResultSet(rs);
                }
            }

            throw new SQLException("CANNOT FIND PRODUCT");
        } // public static synchronized Product getProduct

        private static final String GET_RECOMMENDATION = "{CALL GetRecommendation(?)}";

        public static synchronized java.util.List<Product> getRecommendation(Connection connection, String query) throws SQLException {
            try (CallableStatement cs = connection.prepareCall(GET_RECOMMENDATION)) {
                cs.setString(1, query);
                ResultSet rs = cs.executeQuery();

                java.util.List<Product> products = new java.util.ArrayList<>();

                while (rs.next()) {
                    products.add(Product.fromResultSet(rs));
                }

                return products;
            }
        } // public static synchronized java.util.List<Product> getRecommendation
    } // public static class ProductFetcher

    /**
     * Try not to touch this, its methods are mostly run by the job handler
     */
    public static class TFIDF {
        private static final String COMPUTE_TFIDF = "{CALL ComputeTFIDF()}";

        public static synchronized void computeTFIDF(Connection connection) throws SQLException {
            try (CallableStatement cs = connection.prepareCall(COMPUTE_TFIDF)) {
                cs.executeUpdate();

                connection.commit();
            }
        } // public static synchronized void computeTFIDF
    }

    public static class ProductManager {
        // TODO: Implement
    }
}
