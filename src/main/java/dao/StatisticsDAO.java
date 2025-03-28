package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.sql.SQLException;

public final class StatisticsDAO {
    public static final class SystemStatisticsManager {
        public static final class SystemStatisticsContainer {
            private static int visits = 0;
            private static int peakSession = 0;
            private static long averageResponseTime = 0; // in milisec
            private static long maxResponseTime = 0;

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
                averageResponseTime = (averageResponseTime + t_responseTime) / 2;
                maxResponseTime = t_responseTime > maxResponseTime ? t_responseTime : maxResponseTime;

                // service.Logging.logger.info("Response report received, reports: {}, average: {} ms, max: {} ms", responseTimeReports, averageResponseTime, maxResponseTime);
            }
            
            public static synchronized void reportSession(int t_sessionCount) {
                ++visits;
                peakSession = t_sessionCount > peakSession ? t_sessionCount : peakSession;

                // service.Logging.logger.info("Session report received, visits: {}, peak: {} ms", visits, peakSession);
            }

            public static synchronized void reset() {
                visits = 0;
                peakSession = 0;
                averageResponseTime = 0;
                maxResponseTime = 0;
            }

        } // public static final class SystemStatisticsContainer

        private static final String CREATE_SERVER_STATISTICS = "INSERT INTO tblServerStatistics (day, totalMoneyEarned, userNum, productNum, shopNum, promotionNum, purchaseNum, visitNum, peakSessionNum, averageResponseTime, maxResponseTime)\r\n"
                + //
                "VALUES (\r\n" + //
                "    ?1,\r\n" + //
                "    (SELECT SUM(finalPrice) FROM tblOrder WHERE status = 1),\r\n" + //
                "    (SELECT COUNT(*) FROM tblUser WHERE status = 1),\r\n" + //
                "    (SELECT COUNT(*) FROM tblProduct WHERE status = 1),\r\n" + //
                "    (SELECT COUNT(*) FROM tblShop WHERE status = 1),\r\n" + //
                "    (SELECT COUNT(*) FROM tblPromotion),\r\n" + //
                "    (SELECT SUM(quantity) FROM tblOrderedItem),\r\n" + //
                "    ?2,\r\n" + //
                "    ?3,\r\n" + //
                "    ?4,\r\n" + //
                "    ?5\r\n" + //
                ")";

        /**
         * Only called if the system needs to log the current SystemStatisticsContainer
         */
        public static synchronized void addStatistics() throws SQLException {

            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {
                EntityTransaction et = em.getTransaction();

                try {
                    et.begin();

                    em.createNativeQuery(CREATE_SERVER_STATISTICS).setParameter(1, java.time.LocalDate.now())
                    .setParameter(2, SystemStatisticsContainer.getVisits())
                    .setParameter(3, SystemStatisticsContainer.getPeakSession())
                    .setParameter(4, SystemStatisticsContainer.getAverageResponseTime())
                    .setParameter(5 ,SystemStatisticsContainer.getMaxResponseTime()).executeUpdate();

                    SystemStatisticsContainer.reset();

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
        
        private static final String GET_STATISTICS = "SELECT t.* FROM (SELECT TOP 10 * FROM tblServerStatistics ORDER BY day DESC) AS t ORDER BY t.day ASC;";

        public static synchronized java.util.List<model.TblServerStatistics> getStatistics() throws java.sql.SQLException {
            try (EntityManager em = service.DatabaseConnection.getEntityManager()) {

                return em.createNativeQuery(GET_STATISTICS, model.TblServerStatistics.class).getResultList();

            } catch (Exception e) {
                throw new java.sql.SQLException(e);
            }
        }
    } // public static final class SystemStatistics

    public static final class ShopStatisticsManager {

    } // public static final class ShopStatisticsManager
}
