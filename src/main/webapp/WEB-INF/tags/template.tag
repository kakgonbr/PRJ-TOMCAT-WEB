<%@tag description="Template tag" pageEncoding="UTF-8"%>
<%@attribute name="footer" fragment="true" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
  </head>
  <body>
    <div id="pagefooter">
      <jsp:invoke fragment="footer"/>
    </div>
  </body>
</html>
