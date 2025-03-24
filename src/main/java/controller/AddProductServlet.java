package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AddProductServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer shopId = (Integer) session.getAttribute("shopId");

        if (shopId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Example parameter: (adding)
        // action	"addProduct"
        // name	"hello"
        // description	"world"
        // filter	"83"
        // variation	[…]
        //     0	"33"
        //     1	"34"
        // stock	[…]
        //     0	"5"
        //     1	"7"
        // price	[…]
        //     0	"213413131"
        //     1	"21313131231312"
        // 33	[…]
        //     0	"12"
        //     1	"128"
        // 34	[…]
        //     0	"white"
        //     1	"black"
        HttpSession session = request.getSession();
        Integer shopId = (Integer) session.getAttribute("shopId");
        
        if (shopId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        // if an exception is thrown here, it is the user's fault, they have malformed the input data
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        Integer categoryId = Integer.parseInt(request.getParameter("filter"));
        java.util.List<Integer> variationIds = Arrays.asList(request.getParameterValues("variation")).stream().map(Integer::parseInt).toList();
        String[] stocks = request.getParameterValues("stock");
        String[] prices = request.getParameterValues("price");
        java.util.Map<model.Variation, String[]> variationValues = new java.util.HashMap<>();

        try {
            // chjeck if the order is correct
            for (final Integer variationId : variationIds) {
                model.Variation variation = dao.VariationDAO.VariationFetcher.getVariation(variationId);
                if (!variation.getCategoryId().getId().equals(categoryId)) {
                    throw new java.sql.SQLException("Variation is not from the correct category");
                }

                String[] values = request.getParameterValues(variationId.toString());
                for (final String value : values) {
                    if (variation.getDatatype().equals("integer")) {
                        Integer.parseInt(value);
                    } else if (variation.getDatatype().equals("float")) {
                        Double.parseDouble(value);
                    }
                }

                variationValues.put(variation, values);
            }
            
            if (stocks.length != prices.length || stocks.length < 1 || prices.length < 1 || variationIds.size() < 1) {
                throw new java.sql.SQLException("MALFORMED INPUT, NUMBER OF INPUTED STOCK AND PRICE MUST MATCH, THERE MUST BE ATLEAST ONE PRODUCT ITEM, EACH CONTAINIGN ATLEAST ONE CUSTOMIZATION");
            }
            service.Logging.logger.info("name: {}", name);
            service.Logging.logger.info("description: {}", description);
            service.Logging.logger.info("category: {}", categoryId);
            service.Logging.logger.info("varations: {}", variationIds);
            service.Logging.logger.info("stock: {}", (Object[]) stocks);
            service.Logging.logger.info("price: {}", (Object[]) prices);
            service.Logging.logger.info("variation values: {}", variationValues);
            
            model.Product product = new model.Product();
            product.setShopId(dao.ShopDAO.ShopFetcher.getShop(shopId));
            product.setCategoryId(dao.CategoryDAO.CategoryFetcher.getCategory(categoryId));
            product.setName(name);
            product.setDescription(description);
            // TODO: Add image here
            product.setImageStringResourceId(null);
            product.setStatus(true);

            java.util.List<model.ProductItem> productItems = new java.util.ArrayList<>();

            for (int i = 0; i < stocks.length; ++i) {
                model.ProductItem productItem = new model.ProductItem();
                productItem.setPrice(BigDecimal.valueOf(Long.parseLong(prices[i])));
                productItem.setStock(Integer.parseInt(stocks[i]));

                // go over variations, each product item must contain the same amount
                // i is the index that represents the value of a productitem's variation
                java.util.List<model.ProductCustomization> customizations = new java.util.ArrayList<>();
                for (final java.util.Map.Entry<model.Variation, String[]> entry : variationValues.entrySet()) {
                    model.ProductCustomization customization = new model.ProductCustomization();
                    model.VariationValue variationValue = new model.VariationValue();
                    variationValue.setVariationId(entry.getKey());
                    variationValue.setValue(entry.getValue()[i]);
                    variationValue.setProductCustomizationList(customizations);
                    
                    customization.setProductItemId(productItem);
                    customization.setVariationValueId(variationValue);

                    customizations.add(customization);
                }
                productItem.setProductCustomizationList(customizations);

                service.Logging.logger.info("Adding product item: stock {}, price {}, customization list {}", productItem.getStock(), productItem.getPrice(), productItem.getProductCustomizationList());

                productItem.setProductId(product);

                productItems.add(productItem);
            }

            product.setProductItemList(productItems);

            dao.ProductDAO.ProductManager.addProduct(product);
        } catch (java.sql.SQLException | NumberFormatException e) {
            service.Logging.logger.warn("FAILED TO ADD PRODUCT, REASON: {}", e.getMessage());
            service.Logging.logger.warn("StackTrace: ", (Object[]) e.getStackTrace());
            request.setAttribute("error", e.getMessage());

            doGet(request, response);
        }
    }

    // @Override
    // protected void doGet(HttpServletRequest request, HttpServletResponse response)
    //         throws ServletException, IOException {
    //     HttpSession session = request.getSession();
    //     Integer shopId = (Integer) session.getAttribute("shopId");

    //     if (shopId == null) {
    //         response.sendRedirect(request.getContextPath() + "/login");
    //         return;
    //     }

    //     String action = request.getParameter("action");

    //     switch (action != null ? action : "addProduct") {
    //         case "selectVariation":
    //             request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
    //             break;
    //         case "setStockAndPrice":
    //             request.getRequestDispatcher(config.Config.JSPMapper.SET_STOCK_AND_PRICE).forward(request, response);
    //             break;
    //         case "addProduct":
    //         default:
    //             request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
    //             break;
    //     }
    // }

    // @Override
    // protected void doPost(HttpServletRequest request, HttpServletResponse response)
    //         throws ServletException, IOException {
    //     HttpSession session = request.getSession();
    //     String action = request.getParameter("action");

    //     if (action == null) {
    //         response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");
    //         return;
    //     }

    //     try {
    //         switch (action) {
    //             case "addProduct":
    //                 handleAddProduct(request, response, session);
    //                 break;
    //             case "selectVariation":
    //                 handleSelectVariation(request, response, session);
    //                 break;
    //             case "setStockAndPrice":
    //                 handleSetStockAndPrice(request, response);
    //                 break;
    //             default:
    //                 response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");
    //         }
    //     } catch (SQLException e) {
    //         request.setAttribute("error", "db_error");
    //         request.getRequestDispatcher(config.Config.JSPMapper.ERROR_JSP).forward(request, response);
    //     }
    // }

    // private void handleAddProduct(HttpServletRequest request, HttpServletResponse response, HttpSession session)
    //         throws ServletException, IOException, SQLException {
    //     Integer shopId = (Integer) session.getAttribute("shopId");

    //     if (shopId == null) {
    //         response.sendRedirect(request.getContextPath() + "/login");
    //         return;
    //     }

    //     String categoryIdString = request.getParameter("filter");
    //     String name = request.getParameter("name");
    //     String description = request.getParameter("description");

    //     if (categoryIdString == null || name == null || description == null
    //             || categoryIdString.trim().isEmpty() || name.trim().isEmpty() || description.trim().isEmpty()) {
    //         request.setAttribute("error", "missing_fields");
    //         request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
    //         return;
    //     }
    //     int categoryId = Integer.parseInt(categoryIdString);
    //     session.setAttribute("categoryId", categoryId); // ?????????????
    //     try {
    //         Shop shop = ShopDAO.ShopFetcher.getShop(shopId);
    //         if (shop == null) {
    //             request.setAttribute("error", "invalid_shop");
    //             request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
    //             return;
    //         }

    //         Category category = CategoryDAO.CategoryFetcher.getCategoryDetails(categoryId);
    //         if (category == null) {
    //             request.setAttribute("error", "invalid_category");
    //             request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
    //             return;
    //         }

    //         Product product = new Product();
    //         product.setShopId(shop);
    //         product.setCategoryId(category);
    //         product.setName(name);
    //         product.setDescription(description);
    //         product.setImageStringResourceId(null);
    //         product.setStatus(true);
    //         dao.ProductDAO.ProductManager.addProduct(product);
    //         int productId = dao.ProductDAO.ProductFetcher.getProductByNameAndShop(name, shopId);
    //         session.setAttribute("productId", productId);
    //         List<Product> selectedProducts = (List<Product>) session.getAttribute("selectedProducts"); // session?????????
    //         if (selectedProducts == null) {
    //             selectedProducts = new ArrayList<>();
    //         }
    //         product.setId(productId);
    //         selectedProducts.add(product);
    //         session.setAttribute("selectedProducts", selectedProducts);

    //         response.sendRedirect(request.getContextPath() + "/sellercenter/addproduct?action=selectVariation");

    //     } catch (SQLException e) {
    //         request.setAttribute("error", "db");
    //         request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
    //     } catch (NumberFormatException e) {
    //         request.setAttribute("error", "invalid_category_format");
    //         request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
    //     }
    // }

    // private void handleSelectVariation(HttpServletRequest request, HttpServletResponse response, HttpSession session)
    //         throws ServletException, IOException {
    //     try {
    //         Integer productId = (Integer) session.getAttribute("productId");
    //         Integer categoryId = (Integer) session.getAttribute("categoryId");
    //         if (categoryId == null) {
    //             response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");
    //             return;
    //         }

    //         String[] variationNames = request.getParameterValues("variation");
    //         String[] variationOptionsList = request.getParameterValues("variationValue");
    //         String[] units = request.getParameterValues("unit");
    //         String[] datatypes = request.getParameterValues("datatype");

    //         if (variationNames == null || variationOptionsList == null
    //                 || variationNames.length == 0 || variationOptionsList.length == 0) {
    //             request.setAttribute("error", "Some required fields are missing. Please check and try again.");
    //             request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
    //             return;
    //         }

    //         List<ProductItem> productItemList = new ArrayList<>();

    //         for (int i = 0; i < variationNames.length; i++) {
    //             String variationName = variationNames[i].trim();
    //             String variationOptions = variationOptionsList[i].trim();
    //             String unit = (units != null && units.length > i) ? units[i].trim() : null;
    //             String datatype = (datatypes != null && datatypes.length > i) ? datatypes[i].trim() : null;

    //             if (variationName.isEmpty() || variationOptions.isEmpty()) {
    //                 request.setAttribute("error", "Some variation fields are empty. Please check and try again.");
    //                 request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
    //                 return;
    //             }

    //             Integer variationId = dao.VariationDAO.VariationFetcher.getVariationIdByNameAndCategory(variationName,
    //                     categoryId);
    //             if (variationId == null) {
    //                 Variation variation = new Variation();
    //                 variation.setCategoryId(new Category(categoryId));
    //                 variation.setName(variationName);
    //                 variation.setDatatype(datatype);
    //                 variation.setUnit(unit);

    //                 dao.VariationDAO.VariationManager.createVariation(variation);
    //                 variationId = dao.VariationDAO.VariationFetcher.getVariationIdByNameAndCategory(variationName,
    //                         categoryId);

    //                 if (variationId == null) {
    //                     request.setAttribute("error", "Failed to create variation. Please try again.");
    //                     request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request,
    //                             response);
    //                     return;
    //                 }
    //             }

    //             session.setAttribute("variationId", variationId);

    //             // Xử lý danh sách variation values
    //             String[] optionsArray = variationOptions.split("\\s*,\\s*");
    //             for (String value : optionsArray) {
    //                 value = value.trim();
    //                 if (!value.isEmpty()) {
    //                     VariationValue variationValue = dao.VariationValueDAO.VariationValueFetcher
    //                             .getVariationValueByValue(value);

    //                     if (variationValue == null) {
    //                         variationValue = new VariationValue();
    //                         variationValue.setVariationId(new Variation(variationId));
    //                         variationValue.setValue(value);
    //                         try {
    //                             dao.VariationValueDAO.VariationValueManager.createVariationValue(variationValue);
    //                         } catch (SQLException e) {
    //                             request.setAttribute("error",
    //                                     "Database error while saving variation value: " + e.getMessage());
    //                             request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request,
    //                                     response);
    //                             return;
    //                         }

    //                         variationValue = dao.VariationValueDAO.VariationValueFetcher
    //                                 .getVariationValueByValue(value);
    //                     }

    //                     ProductItem productItem = new ProductItem();
    //                     productItem.setProductId(new Product(productId));
    //                     productItem.setStock(0);
    //                     productItem.setPrice(BigDecimal.ZERO);

    //                     productItem = dao.ProductDAO.ProductManager.addProductItem(productItem);
    //                     int productItemId = productItem.getId();
    //                     productItem.setId(productItemId);

    //                     ProductCustomization productCustomization = new ProductCustomization();
    //                     productCustomization.setProductItemId(new ProductItem(productItemId));
    //                     productCustomization.setVariationValueId(variationValue);

    //                     List<ProductCustomization> customizations = new ArrayList<>();
    //                     customizations.add(productCustomization);

    //                     dao.ProductDAO.ProductManager.addCustomizations(productId, customizations);

    //                     productItemList.add(productItem);
    //                 }
    //             }
    //         }

    //         session.setAttribute("selectedProductItems", productItemList);
    //         response.sendRedirect(request.getContextPath() + "/sellercenter/addproduct?action=setStockAndPrice");

    //     } catch (SQLException e) {
    //         request.setAttribute("error", "Unexpected database error: " + e.getMessage());
    //         request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
    //     }
    // }

    // private void handleSetStockAndPrice(HttpServletRequest request, HttpServletResponse response)
    //         throws ServletException, IOException {
    //     String[] productItemIds = request.getParameterValues("productItemId");
    //     String[] stockValues = request.getParameterValues("stock");
    //     String[] priceValues = request.getParameterValues("price");

    //     if (productItemIds == null || stockValues == null || priceValues == null
    //             || productItemIds.length != stockValues.length || productItemIds.length != priceValues.length) {
    //         response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid data");
    //         return;
    //     }

    //     List<ProductItem> updatedItems = new ArrayList<>();
    //     List<String> errors = new ArrayList<>();

    //     try {
    //         for (int i = 0; i < productItemIds.length; i++) {
    //             try {
    //                 int productItemId = Integer.parseInt(productItemIds[i]);
    //                 int stock = Integer.parseInt(stockValues[i]);
    //                 BigDecimal price = new BigDecimal(priceValues[i]);

    //                 if (stock < 0) {
    //                     errors.add("Stock for product item ID " + productItemId + " cannot be negative.");
    //                 }
    //                 if (price.compareTo(BigDecimal.ZERO) < 0) {
    //                     errors.add("Price for product item ID " + productItemId + " cannot be negative.");
    //                 }

    //                 if (!errors.isEmpty()) {
    //                     continue;
    //                 }

    //                 ProductItem productItem = new ProductItem();
    //                 productItem.setId(productItemId);
    //                 productItem.setStock(stock);
    //                 productItem.setPrice(price);
    //                 updatedItems.add(productItem);

    //             } catch (NumberFormatException e) {
    //                 errors.add("Invalid number format for stock or price at item " + (i + 1));
    //             }
    //         }

    //         if (!errors.isEmpty()) {
    //             request.setAttribute("errorMessages", errors);

    //             request.setAttribute("productItemIds", productItemIds);
    //             request.setAttribute("stockValues", stockValues);
    //             request.setAttribute("priceValues", priceValues);

    //             request.setAttribute("error", errors);
    //             return;
    //         }

    //         int productId = (Integer) request.getSession().getAttribute("productId");
    //         dao.ProductDAO.ProductManager.updateMultipleProductItems(productId, updatedItems);

    //         response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");

    //     } catch (Exception ex) {
    //         request.setAttribute("error", "Stock and price update error");
    //         request.getRequestDispatcher(config.Config.JSPMapper.ERROR_JSP).forward(request, response);
    //     }
    // }

}

// private void handleSelectVariation(HttpServletRequest request,
// HttpServletResponse response, HttpSession session)
// throws ServletException, IOException {
// try {
// Integer productId = (Integer) session.getAttribute("productId");
// Integer categoryId = (Integer) session.getAttribute("categoryId");
// if (categoryId == null) {
// response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");
// return;
// }
//
// String variationName = request.getParameter("variation");
// String variationOptions = request.getParameter("variationValue");
// String unit = request.getParameter("unit");
// String datatype = request.getParameter("datatype");
//
// if (variationName == null || variationOptions == null || datatype == null
// || variationName.trim().isEmpty() || variationOptions.trim().isEmpty() ||
// datatype.trim().isEmpty()) {
// request.setAttribute("error", "Some required fields are missing. Please check
// and try again.");
// // request.setAttribute("errorMessage", "Some required fields are missing.
// Please check and try again.");
// request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request,
// response);
// return;
// }
//
// Integer variationId =
// dao.VariationDAO.VariationFetcher.getVariationIdByNameAndCategory(variationName,
// categoryId);
//
// if (variationId == null) {
// Variation variation = new Variation();
// variation.setCategoryId(new Category(categoryId));
// variation.setName(variationName);
// variation.setDatatype(datatype);
// variation.setUnit(unit);
//
// dao.VariationDAO.VariationManager.createVariation(variation);
// variationId =
// dao.VariationDAO.VariationFetcher.getVariationIdByNameAndCategory(variationName,
// categoryId);
//
// if (variationId == null) {
// request.setAttribute("error", "Failed to create variation. Please try
// again.");
// // request.setAttribute("errorMessage", "Failed to create variation. Please
// try again.");
// request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request,
// response);
// return;
// }
// }
//
// session.setAttribute("variationId", variationId);
//
// // Xử lý danh sách variation values
// String[] optionsArray = variationOptions.split("\\s*,\\s*");
// List<ProductItem> productItemList = new ArrayList<>();
// for (String value : optionsArray) {
// value = value.trim();
// if (!value.isEmpty()) {
// VariationValue variationValue =
// dao.VariationValueDAO.VariationValueFetcher.getVariationValueByValue(value);
//
// if (variationValue == null) {
// variationValue = new VariationValue();
// variationValue.setVariationId(new Variation(variationId));
// variationValue.setValue(value);
// try {
// dao.VariationValueDAO.VariationValueManager.createVariationValue(variationValue);
// } catch (SQLException e) {
// request.setAttribute("error", "Database error while saving variation value: "
// + e.getMessage());
// request.setAttribute("errorMessage", "Database error while saving variation
// value: " + e.getMessage());
// request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request,
// response);
// return;
// }
//
// variationValue =
// dao.VariationValueDAO.VariationValueFetcher.getVariationValueByValue(value);
// }
//
// ProductItem productItem = new ProductItem();
// productItem.setProductId(new Product(productId));
// productItem.setStock(0);
// productItem.setPrice(BigDecimal.ZERO);
//
// productItem = dao.ProductDAO.ProductManager.addProductItem(productItem);
// int productItemId = productItem.getId();
// productItem.setId(productItemId);
//
// ProductCustomization productCustomization = new ProductCustomization();
// productCustomization.setProductItemId(new ProductItem(productItemId));
// productCustomization.setVariationValueId(variationValue);
//
// List<ProductCustomization> customizations = new ArrayList<>();
// customizations.add(productCustomization);
//
// dao.ProductDAO.ProductManager.addCustomizations(productId, customizations);
//
// productItemList.add(productItem);
// }
// }
// session.setAttribute("selectedProductItems", productItemList);
// response.sendRedirect(request.getContextPath() +
// "/sellercenter/addproduct?action=setStockAndPrice");
//
// } catch (SQLException e) {
// request.setAttribute("error", "Unexpected database error: " +
// e.getMessage());
// // request.setAttribute("errorMessage", "Unexpected database error: " +
// e.getMessage());
// request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request,
// response);
// }
// }
