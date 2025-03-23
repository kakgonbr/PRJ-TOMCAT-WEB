<%@tag description="Template tag" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="error" required="false"%>

<c:if test="${error != null && error != ''}">
  <div id="error-container" style="display: block;background-color: red;color: #ddd;position: relative;padding: 2vh;" class="rounded text-center col-12 error-container">
  <button style="position: absolute;right: 0;top: 0;" class="btn" onclick="closeError()">X</button>
        <p class="fs-4">Error: ${error}</p>
    </div>
</c:if>