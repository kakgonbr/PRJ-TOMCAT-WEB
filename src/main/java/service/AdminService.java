package service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


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
        public static class ProductDTO implements java.io.Serializable {
            private Integer id;
            private String name;
            private String description;
            private Integer categoryId;
            private Integer availablePromotionId;
            private String imageStringResourceId;
            private Integer shopId;

            public ProductDTO() {}

            public ProductDTO(model.Product product) {
                setAvailablePromotionId(product.getAvailablePromotionId().getId());
                setCategoryId(product.getCategoryId().getId());
                setDescription(product.getDescription());
                setId(product.getId());
                setImageStringResourceId(product.getImageStringResourceId().getId());
                setName(product.getName());
                setShopId(product.getShopId().getId());
            }

            public model.Product toProduct() {
                model.Product product = new model.Product();

                product.setId(id);
                product.setName(name);
                product.setDescription(description);
                // DANGER: NOT GETTING REFERENCE USING THE ENTITY MANAGER HERE, MAKE SURE THIS DOESN'T SCREW UP ANYTHING
                product.setCategoryId(new model.Category(getCategoryId()));
                product.setAvailablePromotionId(new model.Promotion(getAvailablePromotionId()));
                product.setImageStringResourceId(new model.ResourceMap(getImageStringResourceId()));

                return product;
            }

            public void setAvailablePromotionId(Integer availablePromotionId) {
                this.availablePromotionId = availablePromotionId;
            }

            public void setCategoryId(Integer categoryId) {
                this.categoryId = categoryId;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public void setImageStringResourceId(String imageStringResourceId) {
                this.imageStringResourceId = imageStringResourceId;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setShopId(Integer shopId) {
                this.shopId = shopId;
            }
            
            public Integer getAvailablePromotionId() {
                return availablePromotionId;
            }

            public Integer getCategoryId() {
                return categoryId;
            }

        public String getDescription() {
                return description;
            }

            public Integer getId() {
                return id;
            }

            public String getImageStringResourceId() {
                return imageStringResourceId;
            }

            public String getName() {
                return name;
            }

            public Integer getShopId() {
                return shopId;
            }
        }

        public static class UserDTO implements java.io.Serializable {
            private Integer id;
            private String email;
            private String username;
            private String phoneNumber;
            private String password;
            private String persistentCookie;
            private String googleId;
            private String facebookId;
            private String displayName;
            private String bio;
            private Boolean isAdmin;
            private BigDecimal credit;
            private String profileStringResourceId;
            private Boolean status;
            

            public UserDTO() {}

            public UserDTO(model.User user) {
                setBio(user.getBio());
                setCredit(user.getCredit());
                setDisplayName(user.getDisplayName());
                setEmail(user.getEmail());
                setFacebookId(user.getFacebookId());
                setGoogleId(user.getGoogleId());
                setId(user.getId());
                setIsAdmin(user.getIsAdmin());
                setPassword(user.getPassword());
                setPersistentCookie(user.getPersistentCookie());
                setPhoneNumber(user.getPhoneNumber());
                setProfileStringResourceId(user.getProfileStringResourceId().getId());
                setStatus(user.isStatus());
                setUsername(user.getUsername());
            }

            public model.User toUser() {
                model.User user = new model.User();
                user.setId(id);
                user.setEmail(email);
                user.setUsername(username);
                user.setPhoneNumber(phoneNumber);
                user.setPassword(password);
                user.setPersistentCookie(persistentCookie);
                user.setGoogleId(googleId);
                user.setFacebookId(facebookId);
                user.setDisplayName(displayName);
                user.setBio(bio);
                user.setIsAdmin(isAdmin);
                user.setCredit(credit);
                user.setProfileStringResourceId(new model.ResourceMap(profileStringResourceId));
                user.setStatus(status);

                return user;
            }

            public Integer getId() {
                return id;
            }

            public String getEmail() {
                return email;
            }

            public String getUsername() {
                return username;
            }

            public String getPhoneNumber() {
                return phoneNumber;
            }

            public String getPassword() {
                return password;
            }

            public String getPersistentCookie() {
                return persistentCookie;
            }

            public String getGoogleId() {
                return googleId;
            }

            public String getFacebookId() {
                return facebookId;
            }

            public String getDisplayName() {
                return displayName;
            }

            public String getBio() {
                return bio;
            }

            public Boolean getIsAdmin() {
                return isAdmin;
            }

            public BigDecimal getCredit() {
                return credit;
            }

            public String getProfileStringResourceId() {
                return profileStringResourceId;
            }

            public Boolean getStatus() {
                return status;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public void setPhoneNumber(String phoneNumber) {
                this.phoneNumber = phoneNumber;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public void setPersistentCookie(String persistentCookie) {
                this.persistentCookie = persistentCookie;
            }

            public void setGoogleId(String googleId) {
                this.googleId = googleId;
            }

            public void setFacebookId(String facebookId) {
                this.facebookId = facebookId;
            }

            public void setDisplayName(String displayName) {
                this.displayName = displayName;
            }

            public void setBio(String bio) {
                this.bio = bio;
            }

            public void setIsAdmin(Boolean isAdmin) {
                this.isAdmin = isAdmin;
            }

            public void setCredit(BigDecimal credit) {
                this.credit = credit;
            }

            public void setProfileStringResourceId(String profileStringResourceId) {
                this.profileStringResourceId = profileStringResourceId;
            }

            public void setStatus(Boolean status) {
                this.status = status;
            }
        }

        public static class ShopDTO implements java.io.Serializable {
            private String name;
            private String address;
            private Integer id;
            private Boolean visible;
            private Integer ownerId;

            public ShopDTO() {}

            public ShopDTO(model.Shop shop) {
                setAddress(shop.getAddress());
                setId(shop.getId());
                setName(shop.getName());
                setOwnerId(shop.getOwnerId().getId());
                setVisible(shop.getVisible());
            }

            public model.Shop toShop() {
                model.Shop shop = new model.Shop();

                shop.setName(name);
                shop.setAddress(address);
                shop.setId(id);
                shop.setVisible(visible);
                shop.setOwnerId(new model.User(ownerId));

                return shop;
            }

            public String getName() {
                return name;
            }

            public String getAddress() {
                return address;
            }

            public Integer getId() {
                return id;
            }

            public Boolean getVisible() {
                return visible;
            }

            public Integer getOwnerId() {
                return ownerId;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public void setVisible(Boolean visible) {
                this.visible = visible;
            }

            public void setOwnerId(Integer ownerId) {
                this.ownerId = ownerId;
            }
        }

        public static class PromotionDTO implements java.io.Serializable {
            private String name;
            private int value;
            private Date expireDate;
            private Integer id;
            private Boolean type;
            private Boolean ofAdmin;
            private Date creationDate;
            private Integer creatorId;

            public PromotionDTO() {}

            public PromotionDTO(model.Promotion promotion) {
                setCreationDate(promotion.getCreationDate());
                setCreatorId(promotion.getCreatorId().getId());
                setExpireDate(promotion.getExpireDate());
                setId(promotion.getId());
                setName(promotion.getName());
                setOfAdmin(promotion.getOfAdmin());
                setType(promotion.getType());
                setValue(promotion.getValue());
            }

            public String getName() {
                return name;
            }

            public int getValue() {
                return value;
            }

            public Date getExpireDate() {
                return expireDate;
            }

            public Integer getId() {
                return id;
            }

            public Boolean getType() {
                return type;
            }

            public Boolean getOfAdmin() {
                return ofAdmin;
            }

            public Date getCreationDate() {
                return creationDate;
            }

            public Integer getCreatorId() {
                return creatorId;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setValue(int value) {
                this.value = value;
            }

            public void setExpireDate(Date expireDate) {
                this.expireDate = expireDate;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public void setType(Boolean type) {
                this.type = type;
            }

            public void setOfAdmin(Boolean ofAdmin) {
                this.ofAdmin = ofAdmin;
            }

            public void setCreationDate(Date creationDate) {
                this.creationDate = creationDate;
            }

            public void setCreatorId(Integer creatorId) {
                this.creatorId = creatorId;
            }
        }   

        public static class ResourceDTO implements java.io.Serializable {
            private String systemPath;
            private String id;

            public ResourceDTO() {}

            public ResourceDTO(model.ResourceMap resourceMap) {
                setId(resourceMap.getId());
                setSystemPath(resourceMap.getSystemPath());
            }

            public model.ResourceMap toResourceMap() {
                model.ResourceMap resourceMap = new model.ResourceMap();

                resourceMap.setSystemPath(systemPath);
                resourceMap.setId(id);

                return resourceMap;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setSystemPath(String systemPath) {
                this.systemPath = systemPath;
            }
            
            public String getId() {
                return id;
            }

            public String getSystemPath() {
                return systemPath;
            }
        }

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
            // TODO: IMPLEMENT
            throw new java.sql.SQLException("UNIMPLEMENTED");
        }

        public static void persistProductDTO(ProductDTO productDTO) throws java.sql.SQLException {
            dao.ProductDAO.ProductManager.editProduct(productDTO.toProduct());
        }

        public static void persistShopDTO(ShopDTO shopDTO) throws java.sql.SQLException {
            dao.ShopDAO.ShopManager.updateShop(shopDTO.toShop());
        }

        public static void persistUserDTO(UserDTO userDTO) throws java.sql.SQLException {
            dao.UserDAO.UserManager.updateUser(userDTO.toUser());
        }

        public static void persistResourceDTO(ResourceDTO resourceDTO) throws java.sql.SQLException {
            dao.ResourceDAO.editMapping(resourceDTO.toResourceMap());
        }

        public static void persistPromotionDTO(PromotionDTO promotionDTO) throws java.sql.SQLException {
            // TODO: IMPLEMENT
        }
    }
}
