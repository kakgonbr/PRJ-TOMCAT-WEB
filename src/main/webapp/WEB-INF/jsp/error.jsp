<%-- POSSIBLE REQUEST ATTRIBUTES:  code(int) --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@ page isErrorPage="true" %>

<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
</head>
<body>
    <h2>Oops! Something went wrong.</h2>
    <h2>Error code: ${code}</h2>
    <p>Go back to <a href="${pageContext.request.contextPath}/home">home</a>.</p>
</body>
</html>
