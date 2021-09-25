<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <jsp:include page="shortcut_icon.jsp"/>
    <title>MyDictionary</title>
    <script src="js/jquery-3.6.0.js"></script>
    <script src="js/main.js"></script>
    <script src="js/languagesSelection.js"></script>
    <script src="js/handleException.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-/bQdsTh/da6pkI1MST/rWKFNjaCP5gBSY4sEBT38Q/9RBh9AH40zEOg7Hlq2THRZ"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="css/main-page.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Comfortaa">
</head>
<body>
<div class="container-xl">

    <header class="row">
        <nav class="nav nav-tabs text-center">
            <a class="nav-link col-3"
               href="${pageContext.request.contextPath}/ControllerServlet?command=forward&to=userDictionary">
                My dictionary
            </a>
            <a class="nav-link active col-4" href="#">
                Main page
            </a>
            <a class="nav-link col-3" href="#">Remember game</a>
            <a class="nav-link dropdown-toggle col-2" data-bs-toggle="dropdown" href="#" role="button"
               aria-expanded="false">
                Options
            </a>
            <ul class="dropdown-menu">
                <li><a class="dropdown-item" href="#">View history</a></li>
                <li><a class="dropdown-item" href="#">Change password</a></li>
                <li>
                    <hr class="dropdown-divider">
                </li>
                <li><a class="dropdown-item"
                       href="${pageContext.request.contextPath}/ControllerServlet?command=signOut">Sign out</a></li>
            </ul>
        </nav>
    </header>


    <div class="row mt-5">
        <div class="col-5">
            <div class="input-group mb-3">
                <label class="input-group-text" for="originLanguageSelect">From</label>
                <select size="1" class="form-select" id="originLanguageSelect">
                    <option disabled>Choose language</option>
                    <option value="ru">Russian</option>
                    <option value="en">English</option>
                </select>
            </div>
        </div>
        <div class="col-2 col-auto text-center">
            <button class="btn btn-primary" id="toggleButton">
                <i class="bi bi-arrow-left-right"></i>
            </button>
        </div>
        <div class="col-5">
            <div class="input-group mb-3">
                <select size="1" class="form-select" id="destinationLanguageSelect">
                    <option disabled>Choose language</option>
                    <option value="en">English</option>
                    <option value="ru">Russian</option>
                </select>
                <label class="input-group-text" for="destinationLanguageSelect">To</label>
            </div>
        </div>
    </div>

    <div class="row my-5 text-center" style="max-height: 250px;">

        <div class="col-xl-5 my-2 col-12">
            <textarea class="form-control w-100 h-100 mb-3" placeholder="Print word to translate here" name="originWord"
                      id="originWord"></textarea>
        </div>

        <div class="col-xl-2 col-auto">
        </div>

        <div class="col-xl-5 my-2 col-12 h-100">
            <ul class="list-unstyled" id="answers-list" style="overflow: auto; min-height: 100px;">
            </ul>
        </div>
    </div>

    <div class="row justify-content-center">
        <div class="col-3 text-center">
            <button class="btn btn-primary w-100" id="translateButton">
                <span class="regular-button-text">Translate</span>
            </button>
        </div>
    </div>
</div>
</body>
</html>
