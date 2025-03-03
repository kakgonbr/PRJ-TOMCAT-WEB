package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Get and make mappings of resources to system path. Note that the systemPath path is <strong>relative</strong> to <code>/prj/resources</code>
 */
public class ResourceDAO {
    
    private static final String GET_PATH = "SELECT systemPath FROM tblResourceMap WHERE id = ?"; // id is string

    public static synchronized String getPath(Connection connection, String name) throws java.sql.SQLException {
        try (PreparedStatement ps = connection.prepareStatement(GET_PATH)) {
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }

            throw new java.sql.SQLException("NO PATH FOUND");
        }
    }

    private static final String MAKE_MAPPING = "INSERT INTO tblResourceMap (id, systemPath) VALUES (?, ?)";

    public static synchronized void addMapping(Connection connection, String name, String path) throws java.sql.SQLException {
        try (PreparedStatement ps = connection.prepareStatement(MAKE_MAPPING)) {
            ps.setString(1, name);
            ps.setString(1, path);

            if (ps.executeUpdate() == 0) throw new java.sql.SQLException("CANNOT ADD MAPPING");

            connection.commit();
        }
    }
}
