<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="en" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>Sing in to MyDictionary</title>
    <link rel="stylesheet" href="css/sing-in.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Comfortaa">
</head>
<body>
<div>
    <h1>Sign in</h1>
</div>
<div class="main-form">
    <form action="${pageContext.request.contextPath}/ControllerServlet?command=signIn" method="post">
        <p><label for="email">Email:</label>
            <input class="input__text" type="text" name="email" id="email" required/></p>
        <p><label for="password">Password:</label>
            <input class="input__text" type="password" name="password" id="password" required/></p>
        <p><input class="input__button" type="submit" value="Sign in"><br/></p>
    </form>
</div>
<div class="register-offer">
    <p>First time? <a href="${pageContext.request.contextPath}/ControllerServlet?command=forward&to=registration">Create an account.</a></p>
    <p>Forgot password? <a href="${pageContext.request.contextPath}/ControllerServlet?command=forward&to=restorePassword">Click here.</a></p>
</div>
</body>
</html>
