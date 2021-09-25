<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="shortcut_icon.jsp"/>
    <title>Exception</title>
</head>
<body>
<p>
  Error: ${requestScope.errorMessage}
</p>
</body>
</html>
