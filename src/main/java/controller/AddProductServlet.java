package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
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
            // service.Logging.logger.info("name: {}", name);
            // service.Logging.logger.info("description: {}", description);
            // service.Logging.logger.info("category: {}", categoryId);
            // service.Logging.logger.info("varations: {}", variationIds);
            // service.Logging.logger.info("stock: {}", (Object[]) stocks);
            // service.Logging.logger.info("price: {}", (Object[]) prices);
            // service.Logging.logger.info("variation values: {}", variationValues);
            
            model.Product product = new model.Product();
            product.setShopId(dao.ShopDAO.ShopFetcher.getShop(shopId));
            product.setCategoryId(dao.CategoryDAO.CategoryFetcher.getCategory(categoryId));
            product.setName(name);
            product.setDescription(description);
            // TODO: Add image here
            product.setImageStringResourceId(null);
            product.setStatus(true);

            java.util.List<model.ProductItem> productItems = new java.util.ArrayList<>();

            // jpa jpa jpa jpa jpa jpa jpa jpa jpa jpa jpa jpa jpa jpa jpa jpa jpa jpa jpa jpa jpa jpa jpa jpa
            // how does object persisting work? who knows
            // perks of high level programming
            // you never know what breaks, whether its yours or theirs
            for (int i = 0; i < stocks.length; ++i) {
                model.ProductItem productItem = new model.ProductItem();
                productItem.setPrice(BigDecimal.valueOf(Long.parseLong(prices[i])));
                productItem.setStock(Integer.parseInt(stocks[i]));

                // go over variations, each product item must contain the same amount
                // i is the index that represents the value of a productitem's variation
                java.util.List<model.ProductCustomization> customizations = new java.util.ArrayList<>();
                for (final java.util.Map.Entry<model.Variation, String[]> entry : variationValues.entrySet()) {
                    model.ProductCustomization customization = new model.ProductCustomization();
                    model.VariationValue variationValue = dao.VariationValueDAO.VariationValueFetcher.getVariationValueByValueAndVariation(entry.getValue()[i], entry.getKey().getId());
                    variationValue = variationValue == null ? new model.VariationValue() : variationValue;
                    variationValue.setVariationId(entry.getKey());
                    variationValue.setValue(entry.getValue()[i]);
                    // variationValue.setProductCustomizationList(customizations);
                    
                    customization.setProductItemId(productItem);
                    customization.setVariationValueId(variationValue);

                    customizations.add(customization);
                }
                productItem.setProductCustomizationList(customizations);

                // service.Logging.logger.info("Adding product item: stock {}, price {}, customization list {}", productItem.getStock(), productItem.getPrice(), productItem.getProductCustomizationList());

                productItem.setProductId(product);

                productItems.add(productItem);
            }

            product.setProductItemList(productItems);

            product = dao.ProductDAO.ProductManager.addProduct(product);

            // service.Logging.logger.info("product from database: {}", product.getId());


            for (final model.ProductItem productItem : productItems) {
                dao.ProductDAO.ProductManager.addProductItem(productItem);

                for (final model.ProductCustomization customization : productItem.getProductCustomizationList()) {
                    // service.Logging.logger.info("adding variation value, variation : {}, value: {}, references {}", customization.getVariationValueId().getVariationId(), customization.getVariationValueId().getValue(), customization.getVariationValueId().getVariationId().getId());
                    try {
                        dao.VariationValueDAO.VariationValueManager.createVariationValue(customization.getVariationValueId());
                    } catch (java.sql.SQLException e) {
                        // service.Logging.logger.warn("Failed to add variation value, reason: {}", e.getMessage());
                        dao.VariationValueDAO.VariationValueManager.updateVariationValue(customization.getVariationValueId());
                    }
                }
                // service.Logging.logger.info("Adding customizations {}", productItem.getProductCustomizationList());
                dao.ProductDAO.ProductManager.addCustomizations(productItem.getProductCustomizationList());
            }

            // // nesting, nesting, more nesting
            // for (final model.ProductItem productItem : product.getProductItemList()) {
            //     service.Logging.logger.info("adding product item: stocl: {}, price: {}", productItem.getStock(), productItem.getPrice());
            //     dao.ProductDAO.ProductManager.addProductItem(productItem);
            //     service.Logging.logger.info("adding customizations");
            //     dao.ProductDAO.ProductManager.addCustomizations(product.getId(), productItem.getProductCustomizationList());
                
            //     for (final model.ProductCustomization customization : productItem.getProductCustomizationList()) {
            //         service.Logging.logger.info("adding variation value, variation : {}, value: {}", customization.getVariationValueId().getVariationId(), customization.getVariationValueId().getValue());
            //         try {
            //             dao.VariationValueDAO.VariationValueManager.createVariationValue(customization.getVariationValueId());
            //         } catch (java.sql.SQLException e) {
            //             dao.VariationValueDAO.VariationValueManager.updateVariationValue(customization.getVariationValueId());
            //         }
            //     }
            // }
        } catch (java.sql.SQLException | NumberFormatException e) {
            service.Logging.logger.warn("FAILED TO ADD PRODUCT, REASON: {}", e.getMessage());
            service.Logging.logger.warn("StackTrace: ", (Object[]) e.getStackTrace());
            request.setAttribute("error", e.getMessage());

            doGet(request, response);
        }

        response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");
    }
}
