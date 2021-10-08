<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<html lang="en" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <jsp:include page="shortcut_icon.jsp"/>
    <title>Sing in to MyDictionary</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-/bQdsTh/da6pkI1MST/rWKFNjaCP5gBSY4sEBT38Q/9RBh9AH40zEOg7Hlq2THRZ"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="css/page-with-center-content.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Comfortaa">
</head>
<body>
<div class="container text-center w-100">
    <div class="mb-2">
        <h1>Sign in</h1>
    </div>
    <div class="col-md-3 mx-auto my-3" style="max-width: 330px">
        <form class="needs-validation" action="${pageContext.request.contextPath}/ControllerServlet?command=register"
              method="post">
            <div class="form-floating my-2">
                <input type="email" class="form-control <c:if test="${requestScope.EMAIL_INVALID!=null}">is-invalid</c:if>"
                       id="emailInput" name="email" placeholder="name@example.com"
                       <c:if test="${not empty requestScope.email}">value="${requestScope.email}"</c:if>>
                <label for="emailInput">Email address</label>
                <div class="invalid-feedback">
                    <c:if test="${requestScope.EMAIL_INVALID=='EMAIL_INVALID'}">Email invalid</c:if>
                    <c:if test="${requestScope.EMAIL_INVALID=='USER_WITH_EMAIL_ALREADY_EXISTS'}">User with this email
                        already exists</c:if>
                </div>
            </div>
            <div class="form-floating my-2">
                <input type="password" class="form-control <c:if test="${requestScope.PASSWORD_INVALID!=null}">
                is-invalid</c:if>" id="PasswordInput" name="password" placeholder="Password">
                <label for="PasswordInput">Password</label>
                <div class="invalid-feedback">
                    Password invalid(size should be >=8 and <=256)
                </div>
            </div>
            <div class="form-floating my-2">
                <input type="password" class="form-control <c:if test="${requestScope.PASSWORDS_NOT_MATCH!=null}">
                is-invalid</c:if>" id="PasswordRepeatInput" name="passwordRepeat"
                       placeholder="Repeat password">
                <label for="PasswordRepeatInput">Repeat password</label>
                <div class="invalid-feedback">
                    Passwords should be equals
                </div>
            </div>
            <button type="submit" class="btn btn-primary w-100">Register</button>
        </form>
    </div>
    <div>
        <div>Already have an account? <a class="link-primary"
                                         href="${pageContext.request.contextPath}/ControllerServlet?command=forward&to=signIn">
            Sign in.</a>
        </div>
    </div>
</div>
</body>
</html>