<%-- A GENERIC PAGE WITH AN EDITABLE HEAD, BODY, FOOTER --%>
<%-- To use, include the directory this tag is in with a directive --%>

<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@attribute name="head" fragment="true" %>
<%@attribute name="header" fragment="true" %>
<%@attribute name="body" fragment="true" %>
<%@attribute name="footer" fragment="true" %>
<%@attribute name="title" required="true"%>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>${title} - BM Market</title>

    <jsp:invoke fragment="head"/>  <%-- RESOURCES HERE --%>
  </head>
  <body>
    <div id="pageheader"> 
      <jsp:invoke fragment="header"/>
    </div>
    <div id="body">
      <jsp:invoke fragment="body"/>
    </div>
    <div id="pagefooter">
      <jsp:invoke fragment="footer"/>
    </div>
  </body>
</html>
