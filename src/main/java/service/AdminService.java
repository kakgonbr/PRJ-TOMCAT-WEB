package service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import model.dto.*;


public class AdminService {
    public static class StatsService {
        public static java.util.List<Map<String, Object>> createCharts() throws java.sql.SQLException {
        java.util.List<model.TblServerStatistics> statistics;

        statistics = dao.StatisticsDAO.SystemStatisticsManager.getStatistics();

        java.util.List<String> dates = statistics.stream().map(model.TblServerStatistics::getDay).map(misc.Utils::formatDate).collect(Collectors.toList());

        // this is really, really long
        Map<String, Object> chartTotalMoney = new HashMap<>();
        chartTotalMoney.put("for", "chartTotalMoney");
        chartTotalMoney.put("type", "line");
        chartTotalMoney.put("label", "Money Earned");
        chartTotalMoney.put("labels", dates);
        chartTotalMoney.put("values", statistics.stream().map(model.TblServerStatistics::getTotalMoneyEarned).collect(Collectors.toList()));

        Map<String, Object> chartNumVisit = new HashMap<>();
        chartNumVisit.put("for", "chartNumVisit");
        chartNumVisit.put("type", "line");
        chartNumVisit.put("label", "Number of Visits");
        chartNumVisit.put("labels", dates);
        chartNumVisit.put("values", statistics.stream().map(model.TblServerStatistics::getVisitNum).collect(Collectors.toList()));

        Map<String, Object> chartNumPurchase = new HashMap<>();
        chartNumPurchase.put("for", "chartNumPurchase");
        chartNumPurchase.put("type", "line");
        chartNumPurchase.put("label", "Number of Purchases");
        chartNumPurchase.put("labels", dates);
        chartNumPurchase.put("values", statistics.stream().map(model.TblServerStatistics::getPurchaseNum).collect(Collectors.toList()));
        
        Map<String, Object> chartNumUsers = new HashMap<>();
        chartNumUsers.put("for", "chartNumUsers");
        chartNumUsers.put("type", "line");
        chartNumUsers.put("label", "Number of Users");
        chartNumUsers.put("labels", dates);
        chartNumUsers.put("values", statistics.stream().map(model.TblServerStatistics::getUserNum).collect(Collectors.toList()));
        
        Map<String, Object> chartNumProducts = new HashMap<>();
        chartNumProducts.put("for", "chartNumProducts");
        chartNumProducts.put("type", "line");
        chartNumProducts.put("label", "Number of Products");
        chartNumProducts.put("labels", dates);
        chartNumProducts.put("values", statistics.stream().map(model.TblServerStatistics::getProductNum).collect(Collectors.toList()));
        
        Map<String, Object> chartNumShops = new HashMap<>();
        chartNumShops.put("for", "chartNumShops");
        chartNumShops.put("type", "line");
        chartNumShops.put("label", "Number of Shops");
        chartNumShops.put("labels", dates);
        chartNumShops.put("values", statistics.stream().map(model.TblServerStatistics::getShopNum).collect(Collectors.toList()));
        
        Map<String, Object> chartNumPromotions = new HashMap<>();
        chartNumPromotions.put("for", "chartNumPromotions");
        chartNumPromotions.put("type", "line");
        chartNumPromotions.put("label", "Number of promotions");
        chartNumPromotions.put("labels", dates);
        chartNumPromotions.put("values", statistics.stream().map(model.TblServerStatistics::getPromotionNum).collect(Collectors.toList()));

        Map<String, Object> chartAvgResponse = new HashMap<>();
        chartAvgResponse.put("for", "chartAvgResponse");
        chartAvgResponse.put("type", "line");
        chartAvgResponse.put("label", "Average request response time (ms)");
        chartAvgResponse.put("labels", dates);
        chartAvgResponse.put("values", statistics.stream().map(model.TblServerStatistics::getPromotionNum).collect(Collectors.toList()));

        Map<String, Object> chartMaxResponse = new HashMap<>();
        chartMaxResponse.put("for", "chartMaxResponse");
        chartMaxResponse.put("type", "line");
        chartMaxResponse.put("label", "Maximum request response time (ms)");
        chartMaxResponse.put("labels", dates);
        chartMaxResponse.put("values", statistics.stream().map(model.TblServerStatistics::getPromotionNum).collect(Collectors.toList()));
        
        java.util.List<Map<String, Object>> charts = java.util.Arrays.asList(chartTotalMoney, chartNumVisit, chartNumPurchase, chartNumUsers, chartNumProducts, chartNumShops, chartNumPromotions, chartAvgResponse, chartMaxResponse);


        return charts;
        }
    }

    public static class DatabaseEditService {
        public static java.util.List<ProductDTO> getProductDTOs() throws java.sql.SQLException {
            return dao.ProductDAO.ProductFetcher.getProducts().stream().map(ProductDTO::new).toList();
        }

        public static java.util.List<ShopDTO> getShopDTOs() throws java.sql.SQLException {
            return dao.ShopDAO.ShopFetcher.getShops().stream().map(ShopDTO::new).toList();
        }

        public static java.util.List<UserDTO> getUserDTOs() throws java.sql.SQLException {
            return dao.UserDAO.UserFetcher.getUsers().stream().map(UserDTO::new).toList();
        }

        public static java.util.List<ResourceDTO> getResourceDTOs() throws java.sql.SQLException {
            return dao.ResourceDAO.getAllResources().stream().map(ResourceDTO::new).toList();
        }

        public static java.util.List<PromotionDTO> getPromotionDTOs() throws java.sql.SQLException {
            return dao.PromotionDAO.PromotionFetcher.getPromotions().stream().map(PromotionDTO::new).toList();
        }

        public static void persistProductDTO(ProductDTO productDTO) throws java.sql.SQLException {
            if (productDTO.getId() == null) {
                dao.ProductDAO.ProductManager.addProduct(productDTO.toProduct());
                return;
            }

            dao.ProductDAO.ProductManager.editProduct(productDTO.toProduct());
        }

        public static void persistShopDTO(ShopDTO shopDTO) throws java.sql.SQLException {
            if (shopDTO.getId() == null) {
                dao.ShopDAO.ShopManager.createShop(shopDTO.toShop());
                return;
            }

            dao.ShopDAO.ShopManager.updateShop(shopDTO.toShop());
        }

        public static void persistUserDTO(UserDTO userDTO) throws java.sql.SQLException {
            if (userDTO.getId() == null) {
                dao.UserDAO.UserManager.createUser(userDTO.toUser());
                return;
            }
            
            dao.UserDAO.UserManager.updateUser(userDTO.toUser());
        }

        public static void persistResourceDTO(ResourceDTO resourceDTO) throws java.sql.SQLException {
            if (resourceDTO.getId() == null) {
                dao.ResourceDAO.addMapping(resourceDTO.getId(), resourceDTO.getSystemPath());

                return;
            }

            dao.ResourceDAO.editMapping(resourceDTO.toResourceMap());
        }

        public static void persistPromotionDTO(PromotionDTO promotionDTO) throws java.sql.SQLException {
            if (promotionDTO.getId() == null) {
                dao.PromotionDAO.PromotionManager.addPromotion(promotionDTO.toPromotion());

                return;
            }

            dao.PromotionDAO.PromotionManager.editPromotion(promotionDTO.toPromotion());
        }

        public static java.util.List<java.util.Map<String, Object>> getDTOs() throws java.sql.SQLException {
            
            java.util.Map<String, Object> resources = new java.util.HashMap<>();
            resources.put("name", "resources");
            resources.put("records", getResourceDTOs());

            java.util.Map<String, Object> products = new java.util.HashMap<>();
            products.put("name", "products");
            products.put("records", getProductDTOs());

            java.util.Map<String, Object> shops = new java.util.HashMap<>();
            shops.put("name", "shops");
            shops.put("records", getShopDTOs());

            java.util.Map<String, Object> users = new java.util.HashMap<>();
            users.put("name", "users");
            users.put("records", getUserDTOs());

            java.util.Map<String, Object> promotions = new java.util.HashMap<>();
            promotions.put("name", "promotions");
            promotions.put("records", getPromotionDTOs());

            return java.util.Arrays.asList(resources, products, shops, users);
        }
    }
}
