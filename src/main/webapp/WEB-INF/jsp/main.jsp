<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>MyDictionary</title>
    <script src="js/jquery-3.6.0.js"></script>
    <script src="js/main.js"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Comfortaa">
    <link rel="stylesheet" href="css/normalize.css">
    <link rel="stylesheet" href="css/main-page.css">
</head>
<body>
<header class="general-header">
    <div>
        <div class="header__item-wrapper">
            <a class="header__item" href="">My dictionary</a>
        </div>
        <div class="header__item-wrapper">
            <a class="header__item" href="#">Main page</a>
        </div>
        <div class="header__item-wrapper">
            <a class="header__item"
               href="${pageContext.request.contextPath}/ControllerServlet?command=signOut">Sign out</a>
        </div>
    </div>
    <div class="clr"></div>
</header>

<div class="div__boxes-parent">
    <div class="div__width-box div__language-box__select">
        <p class="language-element">
            <label for="originLanguageSelect">From</label><br/>
            <select size="1" id="originLanguageSelect" name="originLanguage">
                <option disabled>Choose language</option>
                <option>ru</option>
                <option>en</option>
            </select>
        </p>
    </div>
    <div class="div__width-box div__language-box__button">
        <button class="language-element toggle-button">Toggle</button>
    </div>
    <div class="div__width-box div__language-box__select">
        <p class="language-element">
            <label for="destinationLanguageSelect">To</label><br/>
            <select size="1" id="destinationLanguageSelect" name="translateLanguage">
                <option disabled>Choose language</option>
                <option>en</option>
                <option>ru</option>
            </select>
        </p>
    </div>
</div>

<div class="div__boxes-parent">
    <div class="div__width-box div__textArea">
        <textarea class="textArea" placeholder="Print word to translate here" name="originWord" id="originWord"></textarea>
    </div>
    <div class="div__width-box div__textArea">
        <!--div just to make space in center-->
        <div id="div__modal-window">
            <div id="modal-window">
                <img src="images/giveBanan2.jpg"><br />
                <a href="#" class="close__modal-window">Взять банан</a>
            </div>
        </div>
        <a href="#div__modal-window"><img style="height: 100%; width: 100%;" src="images/banan.png"></a>
    </div>
    <div class="div__width-box div__textArea">
        <div class="answers-list-form" id="div-translation">
        <ul class="answers-list" id="answers-list">
        </ul>
        </div>
        <button class="send-answers-button" type="submit" id="sendChoicesButton">send choices</button>
    </div>
</div>

<div class="div__translate-button">
        <button id="translateButton" class="translate-button">Translate</button>
</div>
</body>
</html>
