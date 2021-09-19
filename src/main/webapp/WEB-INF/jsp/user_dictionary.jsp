<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>MyDictionary</title>
    <script src="js/jquery-3.6.0.js"></script>
    <script src="js/languagesSelection.js"></script>
    <script src="js/userDictionary.js"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Comfortaa">
    <link rel="stylesheet" href="css/normalize.css">
    <link rel="stylesheet" href="css/main-page.css">
    <link rel="stylesheet" href="css/user-dictionary.css">
</head>
<body>

<jsp:include page="header.jsp"/>

<jsp:include page="languagesSelection.jsp"/>

<div class="div__received-button">
    <button class="receive-button" id="receiveTranslatesButton">Receive saved words</button>
</div>

<div class="div__saved-words-table">
    <table class="saved-words-table" id="savedWordsTable">
        <tr>
            <th>Word</th>
            <th>Translations</th>
        </tr>
    </table>
</div>

</body>
</html>
