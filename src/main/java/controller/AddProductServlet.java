package controller;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Product;
import model.Shop;
import model.Category;
import model.Variation;
import model.VariationValue;
import dao.ShopDAO;
import dao.CategoryDAO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.ProductCustomization;
import model.ProductItem;

public class AddProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer shopId = (Integer) session.getAttribute("shopId");

        if (shopId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        switch (action != null ? action : "addProduct") {
            case "selectVariation":
                request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
                break;
            case "setStockAndPrice":
                request.getRequestDispatcher(config.Config.JSPMapper.SET_STOCK_AND_PRICE).forward(request, response);
                break;
            case "addProduct":
            default:
                request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");
            return;
        }

        try {
            switch (action) {
                case "addProduct":
                    handleAddProduct(request, response, session);
                    break;
                case "selectVariation":
                    handleSelectVariation(request, response, session);
                    break;
                case "setStockAndPrice":
                    handleSetStockAndPrice(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "db_error");
            request.getRequestDispatcher(config.Config.JSPMapper.ERROR_JSP).forward(request, response);
        }
    }

    private void handleAddProduct(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException, SQLException {
        Integer shopId = (Integer) session.getAttribute("shopId");

        if (shopId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String categoryIdString = request.getParameter("filter");
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        if (categoryIdString == null || name == null || description == null
                || categoryIdString.trim().isEmpty() || name.trim().isEmpty() || description.trim().isEmpty()) {
            request.setAttribute("error", "missing_fields");
            request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
            return;
        }
        int categoryId = Integer.parseInt(categoryIdString);
        session.setAttribute("categoryId", categoryId);
        try {
            Shop shop = ShopDAO.ShopFetcher.getShop(shopId);
            if (shop == null) {
                request.setAttribute("error", "invalid_shop");
                request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
                return;
            }

            Category category = CategoryDAO.CategoryFetcher.getCategoryDetails(categoryId);
            if (category == null) {
                request.setAttribute("error", "invalid_category");
                request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
                return;
            }

            Product product = new Product();
            product.setShopId(shop);
            product.setCategoryId(category);
            product.setName(name);
            product.setDescription(description);
            product.setImageStringResourceId(null);
            product.setStatus(true);
            dao.ProductDAO.ProductManager.addProduct(product);
            int productId = dao.ProductDAO.ProductFetcher.getProductByNameAndShop(name, shopId);
            session.setAttribute("productId", productId);
            List<Product> selectedProducts = (List<Product>) session.getAttribute("selectedProducts");
            if (selectedProducts == null) {
                selectedProducts = new ArrayList<>();
            }
            product.setId(productId);
            selectedProducts.add(product);
            session.setAttribute("selectedProducts", selectedProducts);

            response.sendRedirect(request.getContextPath() + "/sellercenter/addproduct?action=selectVariation");

        } catch (SQLException e) {
            request.setAttribute("error", "db");
            request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "invalid_category_format");
            request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
        }
    }

//    private void handleSelectVariation(HttpServletRequest request, HttpServletResponse response, HttpSession session)
//            throws ServletException, IOException {
//        try {
//            Integer productId = (Integer) session.getAttribute("productId");
//            Integer categoryId = (Integer) session.getAttribute("categoryId");
//            if (categoryId == null) {
//                response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");
//                return;
//            }
//
//            String[] variationNames = request.getParameterValues("variation");
//            String[] variationOptionsList = request.getParameterValues("variationValue");
//            String[] units = request.getParameterValues("unit");
//            String[] datatypes = request.getParameterValues("datatype");
//
//            if (variationNames == null || variationOptionsList == null
//                    || variationNames.length == 0 || variationOptionsList.length == 0) {
//                request.setAttribute("error", "Some required fields are missing. Please check and try again.");
//                request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
//                return;
//            }
//
//            List<List<VariationValue>> allVariationValues = new ArrayList<>();
//
//            for (int i = 0; i < variationNames.length; i++) {
//                String variationName = variationNames[i].trim();
//                String variationOptions = variationOptionsList[i].trim();
//                String unit = (units != null && units.length > i) ? units[i].trim() : null;
//                String datatype = (datatypes != null && datatypes.length > i) ? datatypes[i].trim() : null;
//
//                if (variationName.isEmpty() || variationOptions.isEmpty()) {
//                    request.setAttribute("error", "Some variation fields are empty. Please check and try again.");
//                    request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
//                    return;
//                }
//
//                Integer variationId = dao.VariationDAO.VariationFetcher.getVariationIdByNameAndCategory(variationName, categoryId);
//                if (variationId == null) {
//                    Variation variation = new Variation();
//                    variation.setCategoryId(new Category(categoryId));
//                    variation.setName(variationName);
//                    variation.setDatatype(datatype);
//                    variation.setUnit(unit);
//
//                    dao.VariationDAO.VariationManager.createVariation(variation);
//                    variationId = dao.VariationDAO.VariationFetcher.getVariationIdByNameAndCategory(variationName, categoryId);
//
//                    if (variationId == null) {
//                        request.setAttribute("error", "Failed to create variation. Please try again.");
//                        request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
//                        return;
//                    }
//                }
//
//                List<VariationValue> variationValues = new ArrayList<>();
//                String[] optionsArray = variationOptions.split("\\s*,\\s*");
//
//                for (String value : optionsArray) {
//                    value = value.trim();
//                    if (!value.isEmpty()) {
//                        VariationValue variationValue = dao.VariationValueDAO.VariationValueFetcher.getVariationValueByValueAndVariation(value, variationId);
//
//                        if (variationValue == null) {
//                            variationValue = new VariationValue();
//                            variationValue.setVariationId(new Variation(variationId));
//                            variationValue.setValue(value);
//                            dao.VariationValueDAO.VariationValueManager.createVariationValue(variationValue);
//
//                            variationValue = dao.VariationValueDAO.VariationValueFetcher.getVariationValueByValueAndVariation(value, variationId);
//                        }
//
//                        variationValues.add(variationValue);
//                    }
//                }
//
//                allVariationValues.add(variationValues);
//            }
//
//            // 🔥 Generate all possible combinations of variation values
//            List<List<VariationValue>> combinations = generateCombinations(allVariationValues);
//
//            List<ProductItem> productItemList = new ArrayList<>();
//
//            for (List<VariationValue> combination : combinations) {
//                ProductItem productItem = new ProductItem();
//                productItem.setProductId(new Product(productId));
//                productItem.setStock(0);
//                productItem.setPrice(BigDecimal.ZERO);
//                productItem = dao.ProductDAO.ProductManager.addProductItem(productItem);
//
//                int productItemId = productItem.getId();
//                productItem.setId(productItemId);
//
//                List<ProductCustomization> customizations = new ArrayList<>();
//
//                for (VariationValue variationValue : combination) {
//                    ProductCustomization productCustomization = new ProductCustomization();
//                    productCustomization.setProductItemId(productItem);
//                    productCustomization.setVariationValueId(variationValue);
//                    customizations.add(productCustomization);
//                }
//
//                dao.ProductDAO.ProductManager.addCustomizations(productId, customizations);
//                productItemList.add(productItem);
//            }
//            request.setAttribute("allVariationValues", allVariationValues);
//            request.setAttribute("combinations", combinations);
//            session.setAttribute("selectedProductItems", productItemList);
//            response.sendRedirect(request.getContextPath() + "/sellercenter/addproduct?action=setStockAndPrice");
//
//        } catch (SQLException e) {
//            request.setAttribute("error", "Unexpected database error: " + e.getMessage());
//            request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
//        }
//    }
    private void handleSelectVariation(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        try {
            Integer productId = (Integer) session.getAttribute("productId");
            Integer categoryId = (Integer) session.getAttribute("categoryId");
            if (categoryId == null) {
                response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");
                return;
            }

            String jsonData = request.getParameter("variation");
            if (jsonData == null || jsonData.trim().isEmpty()) {
                request.setAttribute("error", "No variation data received.");
                request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
                return;
            }

            jsonData = jsonData.replace("[", "").replace("]", "").trim();
            String[] variationArray = jsonData.split("\\},\\{");

            List<Map<String, Object>> variations = new ArrayList<>();

            for (String variationString : variationArray) {
                variationString = variationString.replace("{", "").replace("}", "");
                String[] keyValuePairs = variationString.split(",");

                Map<String, Object> variation = new HashMap<>();
                List<String> values = new ArrayList<>();

                for (String pair : keyValuePairs) {
                    pair = pair.trim().replace("\"", "");
                    String[] keyValue = pair.split(":");

                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim();
                        String value = keyValue[1].trim();

                        if (key.equals("values")) {
                            value = value.replace("[", "").replace("]", "");
                            String[] valueList = value.split(",");
                            for (String v : valueList) {
                                values.add(v.trim());
                            }
                            variation.put("values", values);
                        } else {
                            variation.put(key, value);
                        }
                    }
                }
                variations.add(variation);
            }

            List<ProductItem> productItemList = new ArrayList<>();
            List<List<String>> allVariationValues = new ArrayList<>();

            // Lặp qua từng biến thể (Variation)
            for (Map<String, Object> variation : variations) {
                String variationName = (String) variation.get("name");
                String datatype = (String) variation.get("datatype");
                String unit = (String) variation.get("unit");

                // Lấy danh sách "values" (danh sách object)
                List<Map<String, Object>> valueObjects = (List<Map<String, Object>>) variation.get("values");

                // Chuyển danh sách object thành danh sách String
                List<String> values = new ArrayList<>();
                for (Map<String, Object> valueObj : valueObjects) {
                    values.add((String) valueObj.get("value"));
                }

                if (variationName == null || values == null || values.isEmpty() || datatype == null || datatype.trim().isEmpty()) {
                    request.setAttribute("error", "Some required fields are missing. Please check and try again.");
                    request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
                    return;
                }

                // Kiểm tra xem Variation có tồn tại chưa
                Integer variationId = dao.VariationDAO.VariationFetcher.getVariationIdByNameAndCategory(variationName, categoryId);
                if (variationId == null) {
                    // Nếu chưa có, tạo mới
                    Variation newVariation = new Variation();
                    newVariation.setCategoryId(new Category(categoryId));
                    newVariation.setName(variationName);
                    newVariation.setDatatype(datatype);
                    newVariation.setUnit(unit);

                    dao.VariationDAO.VariationManager.createVariation(newVariation);
                    variationId = dao.VariationDAO.VariationFetcher.getVariationIdByNameAndCategory(variationName, categoryId);

                    if (variationId == null) {
                        request.setAttribute("error", "Failed to create variation.");
                        request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
                        return;
                    }
                }

                List<String> variationValuesIds = new ArrayList<>();
                for (String value : values) {
                    value = value.trim();
                    if (!value.isEmpty()) {
                        VariationValue variationValue = dao.VariationValueDAO.VariationValueFetcher.getVariationValueByValue(value);
                        if (variationValue == null) {
                            // Nếu chưa có giá trị biến thể, tạo mới
                            variationValue = new VariationValue();
                            variationValue.setVariationId(new Variation(variationId));
                            variationValue.setValue(value);

                            try {
                                dao.VariationValueDAO.VariationValueManager.createVariationValue(variationValue);
                            } catch (SQLException e) {
                                request.setAttribute("error", "Database error while saving variation value: " + e.getMessage());
                                request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
                                return;
                            }

                            variationValue = dao.VariationValueDAO.VariationValueFetcher.getVariationValueByValue(value);
                        }
                        variationValuesIds.add(value);
                    }
                }

                allVariationValues.add(variationValuesIds);
            }

            // Tạo tổ hợp từ các giá trị biến thể
            List<List<String>> combinations = generateCombinations(allVariationValues);

            // Duyệt qua từng tổ hợp để tạo ProductItem
            for (List<String> combination : combinations) {
                ProductItem productItem = new ProductItem();
                productItem.setProductId(new Product(productId));
                productItem.setStock(0);
                productItem.setPrice(BigDecimal.ZERO);

                productItem = dao.ProductDAO.ProductManager.addProductItem(productItem);
                int productItemId = productItem.getId();
                productItem.setId(productItemId);

                List<ProductCustomization> customizations = new ArrayList<>();
                for (String value : combination) {
                    VariationValue variationValue = dao.VariationValueDAO.VariationValueFetcher.getVariationValueByValue(value);
                    if (variationValue != null) {
                        ProductCustomization productCustomization = new ProductCustomization();
                        productCustomization.setProductItemId(new ProductItem(productItemId));
                        productCustomization.setVariationValueId(variationValue);
                        customizations.add(productCustomization);
                    }
                }

                dao.ProductDAO.ProductManager.addCustomizations(productId, customizations);
                productItemList.add(productItem);
            }

            session.setAttribute("selectedProductItems", productItemList);
            response.sendRedirect(request.getContextPath() + "/sellercenter/addproduct?action=setStockAndPrice");

        } catch (SQLException e) {
            request.setAttribute("error", "Unexpected database error: " + e.getMessage());
            request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
        }
    }

    private List<List<String>> generateCombinations(List<List<String>> lists) {
        List<List<String>> result = new ArrayList<>();

        if (lists == null || lists.isEmpty()) {
            return result;
        }

        backtrack(result, new ArrayList<>(), lists, 0);
        return result;
    }

    private void backtrack(List<List<String>> result, List<String> tempList, List<List<String>> lists, int index) {
        if (index == lists.size()) {
            result.add(new ArrayList<>(tempList));
            return;
        }

        for (String value : lists.get(index)) {
            tempList.add(value);
            backtrack(result, tempList, lists, index + 1);
            tempList.remove(tempList.size() - 1);
        }
    }

//    private void handleSelectVariation(HttpServletRequest request, HttpServletResponse response, HttpSession session)
//            throws ServletException, IOException {
//        try {
//            Integer productId = (Integer) session.getAttribute("productId");
//            Integer categoryId = (Integer) session.getAttribute("categoryId");
//            if (categoryId == null) {
//                response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");
//                return;
//            }
//
//            String variationName = request.getParameter("variation");
//            String variationOptions = request.getParameter("variationValue");
//            String unit = request.getParameter("unit");
//            String datatype = request.getParameter("datatype");
//
//            if (variationName == null || variationOptions == null || datatype == null
//                    || variationName.trim().isEmpty() || variationOptions.trim().isEmpty() || datatype.trim().isEmpty()) {
//                request.setAttribute("error", "Some required fields are missing. Please check and try again.");
//                // request.setAttribute("errorMessage", "Some required fields are missing. Please check and try again.");
//                request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
//                return;
//            }
//
//            Integer variationId = dao.VariationDAO.VariationFetcher.getVariationIdByNameAndCategory(variationName, categoryId);
//
//            if (variationId == null) {
//                Variation variation = new Variation();
//                variation.setCategoryId(new Category(categoryId));
//                variation.setName(variationName);
//                variation.setDatatype(datatype);
//                variation.setUnit(unit);
//
//                dao.VariationDAO.VariationManager.createVariation(variation);
//                variationId = dao.VariationDAO.VariationFetcher.getVariationIdByNameAndCategory(variationName, categoryId);
//
//                if (variationId == null) {
//                    request.setAttribute("error", "Failed to create variation. Please try again.");
//                    // request.setAttribute("errorMessage", "Failed to create variation. Please try again.");
//                    request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
//                    return;
//                }
//            }
//
//            session.setAttribute("variationId", variationId);
//
//            // Xử lý danh sách variation values
//            String[] optionsArray = variationOptions.split("\\s*,\\s*");
//            List<ProductItem> productItemList = new ArrayList<>();
//            for (String value : optionsArray) {
//                value = value.trim();
//                if (!value.isEmpty()) {
//                    VariationValue variationValue = dao.VariationValueDAO.VariationValueFetcher.getVariationValueByValue(value);
//
//                    if (variationValue == null) {
//                        variationValue = new VariationValue();
//                        variationValue.setVariationId(new Variation(variationId));
//                        variationValue.setValue(value);
//                        try {
//                            dao.VariationValueDAO.VariationValueManager.createVariationValue(variationValue);
//                        } catch (SQLException e) {
//                            request.setAttribute("error", "Database error while saving variation value: " + e.getMessage());
//                            request.setAttribute("errorMessage", "Database error while saving variation value: " + e.getMessage());
//                            request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
//                            return;
//                        }
//
//                        variationValue = dao.VariationValueDAO.VariationValueFetcher.getVariationValueByValue(value);
//                    }
//
//                    ProductItem productItem = new ProductItem();
//                    productItem.setProductId(new Product(productId));
//                    productItem.setStock(0);
//                    productItem.setPrice(BigDecimal.ZERO);
//
//                    productItem = dao.ProductDAO.ProductManager.addProductItem(productItem);
//                    int productItemId = productItem.getId();
//                    productItem.setId(productItemId);
//
//                    ProductCustomization productCustomization = new ProductCustomization();
//                    productCustomization.setProductItemId(new ProductItem(productItemId));
//                    productCustomization.setVariationValueId(variationValue);
//
//                    List<ProductCustomization> customizations = new ArrayList<>();
//                    customizations.add(productCustomization);
//
//                    dao.ProductDAO.ProductManager.addCustomizations(productId, customizations);
//
//                    productItemList.add(productItem);
//                }
//            }
//            session.setAttribute("selectedProductItems", productItemList);
//            response.sendRedirect(request.getContextPath() + "/sellercenter/addproduct?action=setStockAndPrice");
//
//        } catch (SQLException e) {
//            request.setAttribute("error", "Unexpected database error: " + e.getMessage());
//            // request.setAttribute("errorMessage", "Unexpected database error: " + e.getMessage());
//            request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
//        }
//    }
    private void handleSetStockAndPrice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] productItemIds = request.getParameterValues("productItemId");
        String[] stockValues = request.getParameterValues("stock");
        String[] priceValues = request.getParameterValues("price");

        if (productItemIds == null || stockValues == null || priceValues == null
                || productItemIds.length != stockValues.length || productItemIds.length != priceValues.length) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid data");
            return;
        }

        List<ProductItem> updatedItems = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        try {
            for (int i = 0; i < productItemIds.length; i++) {
                try {
                    int productItemId = Integer.parseInt(productItemIds[i]);
                    int stock = Integer.parseInt(stockValues[i]);
                    BigDecimal price = new BigDecimal(priceValues[i]);

                    if (stock < 0) {
                        errors.add("Stock for product item ID " + productItemId + " cannot be negative.");
                    }
                    if (price.compareTo(BigDecimal.ZERO) < 0) {
                        errors.add("Price for product item ID " + productItemId + " cannot be negative.");
                    }

                    if (!errors.isEmpty()) {
                        continue;
                    }

                    ProductItem productItem = new ProductItem();
                    productItem.setId(productItemId);
                    productItem.setStock(stock);
                    productItem.setPrice(price);
                    updatedItems.add(productItem);

                } catch (NumberFormatException e) {
                    errors.add("Invalid number format for stock or price at item " + (i + 1));
                }
            }

            if (!errors.isEmpty()) {
                request.setAttribute("errorMessages", errors);

                request.setAttribute("productItemIds", productItemIds);
                request.setAttribute("stockValues", stockValues);
                request.setAttribute("priceValues", priceValues);

                request.setAttribute("error", errors);
                return;
            }

            int productId = (Integer) request.getSession().getAttribute("productId");
            dao.ProductDAO.ProductManager.updateMultipleProductItems(productId, updatedItems);

            response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");

        } catch (Exception ex) {
            request.setAttribute("error", "Stock and price update error");
            request.getRequestDispatcher(config.Config.JSPMapper.ERROR_JSP).forward(request, response);
        }
    }

}
