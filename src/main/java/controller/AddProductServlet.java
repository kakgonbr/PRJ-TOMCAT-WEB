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
import dao.ShopDAO;
import dao.CategoryDAO;
import dao.ProductDAO;
import dao.VariationDAO;
import model.VariationValue;

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
                default:
                    response.sendRedirect(request.getContextPath() + "/shophome");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "db_error");
            request.getRequestDispatcher(config.Config.JSPMapper.ERROR_JSP).forward(request, response);
        }
    }

    private void handleAddProduct(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException, SQLException {
        Integer shopIdValue = (Integer) session.getAttribute("shopId");

        if (shopIdValue == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Lấy thông tin từ request
        String categoryIdValue = request.getParameter("filter");
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        // Kiểm tra dữ liệu đầu vào
        if (categoryIdValue == null || name == null || description == null
                || categoryIdValue.trim().isEmpty() || name.trim().isEmpty() || description.trim().isEmpty()) {
            request.setAttribute("error", "missing_fields");
            request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
            return;
        }

        try {
            // Lấy đối tượng Shop từ ID
            Shop shop = ShopDAO.ShopFetcher.getShop(shopIdValue);
            if (shop == null) {
                request.setAttribute("error", "invalid_shop");
                request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
                return;
            }

            // Lấy đối tượng Category từ ID
            int categoryIdInt = Integer.parseInt(categoryIdValue);
            Category category = CategoryDAO.CategoryFetcher.getCategoryDetails(categoryIdInt);
            if (category == null) {
                request.setAttribute("error", "invalid_category");
                request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
                return;
            }

            // Tạo sản phẩm mới với status = 1 (KHÔNG xử lý ảnh)
            Product product = new Product();
            product.setShopId(shop);
            product.setCategoryId(category);
            product.setName(name);
            product.setDescription(description);
            product.setImageStringResourceId(null); // Không xử lý ảnh
            product.setStatus(true); // Luôn đặt status là 1

            // Thêm sản phẩm vào database
            dao.ProductDAO.ProductManager.addProduct(product);

            // Chuyển hướng đến trang danh sách sản phẩm sau khi thêm thành công
            response.sendRedirect(request.getContextPath() + "/shophome");

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
            String variationOptions = request.getParameter("variationOptions"); // Các giá trị options
            String unit = request.getParameter("unit");
            String datatype = request.getParameter("datatype");

            if (variationName == null || variationOptions == null || unit == null || datatype == null
                    || variationName.trim().isEmpty() || variationOptions.trim().isEmpty() || unit.trim().isEmpty() || datatype.trim().isEmpty()) {
                request.setAttribute("error", "missing_fields");
                request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
                return;
            }

            Variation variation = new Variation();
            variation.setCategoryId(new Category(categoryId));
            variation.setName(variationName);
            variation.setDatatype(datatype);
            variation.setUnit(unit);

            dao.VariationDAO.VariationManager.createVariation(variation);

            int variationId = dao.VariationDAO.VariationFetcher.getVariationIdByNameAndCategory(variationName, categoryId);

            String[] optionsArray = variationOptions.split(",");
            for (String value : optionsArray) {
                value = value.trim();
                if (!value.isEmpty()) {
                    VariationValue variationValue = new VariationValue();
                    variationValue.setVariationId(new Variation(variationId));
                    variationValue.setValue(value);
                    dao.VariationValueDAO.VariationValueManager.createVariationValue(variationValue);
                }
            }

            response.sendRedirect(request.getContextPath() + "/shophome");

        } catch (SQLException e) {
            request.setAttribute("error", "db_error");
            request.getRequestDispatcher(config.Config.JSPMapper.SELECT_VARIATION).forward(request, response);
        }
    }

}

