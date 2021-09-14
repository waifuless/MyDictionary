<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>Registration in MyDictionary</title>
    <link rel="stylesheet" href="css/registration.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Comfortaa">
</head>
<body>
<div>
    <h1>Registration</h1>
</div>
<div class="main-form">
    <form action="${pageContext.request.contextPath}/ControllerServlet?command=register" method="post">
        <p><label for="email">Email:</label>
            <input class="input__text" type="text" name="email" id="email" required/></p>
        <p><label for="password">Password:</label>
            <input class="input__text" type="password" name="password" id="password" required/></p>
        <p><label for="passwordRepeat">Repeat password:</label>
            <input class="input__text" type="password" name="passwordRepeat" id="passwordRepeat" required/></p>
        <p><input class="input__button" type="submit" value="Register"><br/></p>
    </form>
</div>
<div class="register-offer">
    <p>Already have an account? <a href="${pageContext.request.contextPath}/ControllerServlet?command=forward&to=signIn">Sign in.</a></p>
</div>
</body>
</html>