<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>MyDictionary</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Comfortaa">
    <link rel="stylesheet" href="css/normalize.css">
    <link rel="stylesheet" href="css/main-page.css">
</head>
<body>

<div>
    <ul class="nav">
        <li class="nav__button">
            <a href="${pageContext.request.contextPath}/ControllerServlet">My dictionary</a>
        </li>
        <li class="nav__button nav__button--center">
            <a href="${pageContext.request.contextPath}/ControllerServlet">Main
                page</a>
        </li>
        <li class="nav__button">
            <a href="${pageContext.request.contextPath}/ControllerServlet?command=signOut">Sign
                out</a>
        </li>
    </ul>
</div>
<div class="div__-boxes-parent">
    <div class="div__width-box div__language-box__select">
        <p class="language-element">
            <label for="originLanguageSelect">From</label><br/>
            <select size="1" id="originLanguageSelect" form="translateForm">
                <option disabled>Choose language</option>
                <option>russian</option>
                <option>english</option>
            </select>
        </p>
    </div>
    <div class="div__width-box div__language-box__button">
        <button class="language-element toggle-button">Toggle</button>
    </div>
    <div class="div__width-box div__language-box__select">
        <p class="language-element">
            <label for="destinationLanguageSelect">To</label><br/>
            <select size="1" id="destinationLanguageSelect" form="translateForm">
                <option disabled>Choose language</option>
                <option>english</option>
                <option>russian</option>
            </select>
        </p>
    </div>
</div>
<div class="div__-boxes-parent">
    <div class="div__width-box div__textArea">
        <textarea class="textArea" form="translateForm" placeholder="Print word to translate here"></textarea>
    </div>
    <div class="div__width-box div__textArea">
        <!--div just to make space in center-->
    </div>
    <div class="div__width-box div__textArea">
        <textarea class="textArea" readonly placeholder="There will be result"></textarea>
    </div>
</div>
<div class="div__translate-button">
    <form id="translateForm" method="post" action="${pageContext.request.contextPath}/ControllerServlet">
        <button class="translate-button">Translate</button>
    </form>
</div>
</body>
</html>
