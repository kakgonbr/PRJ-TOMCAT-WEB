package dao;

import java.sql.Connection;
import java.sql.Statement;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class StatisticsDAO {
    public static final class SystemStatisticsManager {
        public static final class SystemStatisticsContainer {
            private static int visits = 0;
            private static int peakSession = 0;
            private static long averageResponseTime = 0; // in milisec
            private static long maxResponseTime = 0;
            private static int responseTimeReports = 0;

            public static long getAverageResponseTime() {
                return averageResponseTime;
            }

            public static long getMaxResponseTime() {
                return maxResponseTime;
            }

            public static int getPeakSession() {
                return peakSession;
            }

            public static int getVisits() {
                return visits;
            }

            public static synchronized void reportResponseTime(long t_responseTime) {
                ++responseTimeReports;

                averageResponseTime = (averageResponseTime + t_responseTime) / responseTimeReports;
                maxResponseTime = t_responseTime > maxResponseTime ? t_responseTime : maxResponseTime;
            }

            public static synchronized void reportSession(int t_sessionCount) {
                ++visits;
                peakSession = t_sessionCount > peakSession ? t_sessionCount : peakSession;
            }

        } // public static final class SystemStatisticsContainer

        private static final String CREATE_SERVER_STATISTICS = "INSERT INTO tblServerStatistics (day, totalMoneyEarned, userNum, productNum, shopNum, promotionNum, purchaseNum, visitNum, peakSessionNum, averageResponseTime, maxResponseTime)\r\n"
                + //
                "VALUES (\r\n" + //
                "    ?,\r\n" + //
                "    SELECT SUM(finalPrice) FROM tblOrder,\r\n" + //
                "    SELECT COUNT(*) FROM tblUser,\r\n" + //
                "    SELECT COUNT(*) FROM tblProduct,\r\n" + //
                "    SELECT COUNT(*) FROM tblShop,\r\n" + //
                "    SELECT COUNT(*) FROM tblPromotion,\r\n" + //
                "    SELECT SUM(quantity) FROM tblOrderedItem,\r\n" + //
                "    ?,\r\n" + //
                "    ?,\r\n" + //
                "    ?,\r\n" + //
                "    ?\r\n" + //
                ")";

        /**
         * Only called if the system needs to log the current SystemStatisticsContainer
         */
        public static synchronized void addStatistics() throws SQLException {

            // try (PreparedStatement addPS =
            // connection.prepareStatement(CREATE_SERVER_STATISTICS)) {
            // // 5
            // addPS.setDate(1, java.sql.Date.valueOf(java.time.LocalDate.now()));
            // addPS.setInt(2, SystemStatisticsContainer.getVisits());
            // addPS.setInt(3, SystemStatisticsContainer.getPeakSession());
            // addPS.setLong(4, SystemStatisticsContainer.getAverageResponseTime());
            // addPS.setLong(5, SystemStatisticsContainer.getMaxResponseTime());

            // addPS.executeUpdate();

            // connection.commit();

            // } catch (SQLException e) {
            // connection.rollback();
            // throw e;
            // }

            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    em.createNativeQuery(CREATE_SERVER_STATISTICS, model.ChatBox.class).setParameter(1, java.time.LocalDate.now())
                    .setParameter(2, SystemStatisticsContainer.getVisits())
                    .setParameter(3, SystemStatisticsContainer.getPeakSession())
                    .setParameter(4, SystemStatisticsContainer.getAverageResponseTime())
                    .setParameter(5 ,SystemStatisticsContainer.getMaxResponseTime()).executeUpdate();

                    et.commit();
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();

                        throw new java.sql.SQLException(e);
                    }
                }
            }

        } // public static void addStatistics(Connection connection) throws
          // java.sql.SQLException
    } // public static final class SystemStatistics

    public static final class ShopStatisticsManager {

    } // public static final class ShopStatisticsManager
}
