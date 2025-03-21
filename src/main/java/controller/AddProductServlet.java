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
import java.util.List;
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
            response.sendRedirect(request.getContextPath() + "/shophome");
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
                    response.sendRedirect(request.getContextPath() + "/shophome");
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

            response.sendRedirect(request.getContextPath() + "/addproduct?action=selectVariation");

        } catch (SQLException e) {
            request.setAttribute("error", "db");
            request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "invalid_category_format");
            request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
        }
    }

    private void handleSelectVariation(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        try {
            Integer categoryId = (Integer) session.getAttribute("categoryId");
            if (categoryId == null) {
                response.sendRedirect(request.getContextPath() + "/shophome");
                return;
            }

            String variationName = request.getParameter("variationName");
            String variationOptions = request.getParameter("variationOptions");
            String unit = request.getParameter("unit");
            String datatype = request.getParameter("datatype");

            if (variationName == null || variationOptions == null || datatype == null
                    || variationName.trim().isEmpty() || variationOptions.trim().isEmpty() || datatype.trim().isEmpty()) {
                request.setAttribute("error", "missing_fields");
                request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
                return;
            }

            // Kiểm tra xem variation đã tồn tại chưa
            Integer variationId = dao.VariationDAO.VariationFetcher.getVariationIdByNameAndCategory(variationName, categoryId);

            // Nếu không tìm thấy, thêm mới variation
            if (variationId == null) {
                Variation variation = new Variation();
                variation.setCategoryId(new Category(categoryId));
                variation.setName(variationName);
                variation.setDatatype(datatype);
                variation.setUnit(unit);

                dao.VariationDAO.VariationManager.createVariation(variation);

                // Lấy lại variationId sau khi tạo mới
                variationId = dao.VariationDAO.VariationFetcher.getVariationIdByNameAndCategory(variationName, categoryId);
                if (variationId == null) {
                    request.setAttribute("error", "variation_creation_failed");
                    request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
                    return;
                }
            }

            session.setAttribute("variationId", variationId);

            // Xử lý danh sách variation values
            String[] optionsArray = variationOptions.split(",");
            List<VariationValue> variationValuesList = new ArrayList<>();
            for (String value : optionsArray) {
                value = value.trim();
                if (!value.isEmpty()) {
                    VariationValue existingValue = dao.VariationValueDAO.VariationValueFetcher.getVariationValueByValue(value);

                    if (existingValue == null) {
                        VariationValue variationValue = new VariationValue();
                        variationValue.setVariationId(new Variation(variationId));
                        variationValue.setValue(value);
                        dao.VariationValueDAO.VariationValueManager.createVariationValue(variationValue);

                        existingValue = dao.VariationValueDAO.VariationValueFetcher.getVariationValueByValue(value);
                    }
                    variationValuesList.add(existingValue);
                }
            }
            session.setAttribute("variationValues", variationValuesList);
            response.sendRedirect(request.getContextPath() + "/addproduct?action=setStockAndPrice");

        } catch (SQLException e) {
            request.setAttribute("error", "db_error");
            request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
        }
    }

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

        try {
            for (int i = 0; i < productItemIds.length; i++) {
                int productItemId = Integer.parseInt(productItemIds[i]);
                int stock = Integer.parseInt(stockValues[i]);
                BigDecimal price = new BigDecimal(priceValues[i]);

                // Lấy ProductItem từ DB
                ProductItem productItem = dao.ProductDAO.ProductFetcher.getProductItem(productItemId);
                if (productItem != null) {
                    productItem.setStock(stock);
                    productItem.setPrice(price);
                    dao.ProductDAO.ProductManager.addProductItem(productItem);

                    // Lấy danh sách ProductCustomization từ productItemId
                    List<ProductCustomization> customizations
                            = dao.ProductDAO.ProductFetcher.getCustomizations(productItemId);

                    // Chỉ thêm nếu có customization hợp lệ
                    if (customizations != null && !customizations.isEmpty()) {
                        dao.ProductDAO.ProductManager.addCustomizations(productItem.getProductId().getId(), customizations);
                    }
                }
            }

            response.sendRedirect("/shophome");
        } catch (Exception ex) {
            request.setAttribute("error", "Stock and price error");
            request.getRequestDispatcher(config.Config.JSPMapper.ERROR_JSP).forward(request, response);
        }
    }

}
