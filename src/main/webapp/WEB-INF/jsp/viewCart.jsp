<%@page import="model.CartItem"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.CartItem" %> <!-- Thay thế bằng package thực tế của bạn -->

<html>
<head>
    <title>Cart Items</title>
</head>
<body>
<h1>Danh Sách Cart Items</h1>
<table border="1">
    <tr>
        <th>Item ID</th>
        <th>Name</th>
        <th>Quantity</th>
        <th>Price</th>
    </tr>
    <%
        List<CartItem> cartItems = (List<CartItem>) request.getAttribute("cartItems");
        for (CartItem item : cartItems) {
    %>
    <tr>
       
        <td><%= item.getQuantity() %></td>
        <td><%= item.getProductItemId() != null ? item.getProductItemId().getName() : "N/A" %></td>
        <td><%= item.getQuantity() %></td>
        <td><%= item.getProductItemId() != null ? item.getProductItemId().getPrice() : "N/A" %></td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>