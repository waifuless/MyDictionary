$(document).ready(function () {

    function handleAjaxError(xhr, textStatus, thrownError) {
        alert("Code error: " + xhr.status + "\nMessage: " + xhr.responseText);
        if (xhr.status === 401) {
            window.location.href = 'ControllerServlet?command=forward&to=signIn';
        }
    }

    function showButtonLoading(elem) {
        $(elem).attr("regular-button-text", $(elem).html());
        $(elem).prop("disabled", true);
        $(elem).html('<i class="spinner-border spinner-border-sm"></i> Loading...');
    }

    function showButtonReset(elem) {
        $(elem).prop("disabled", false);
        $(elem).html($(elem).attr("regular-button-text"));
    }

    /**
     * Translate button event
     */
    $("#translateButton").click(function () {

        let button = $(this);
        showButtonLoading(button);
        let answersList = $("#answers-list");
        answersList.empty();

        $.ajax({
            url: "AjaxControllerServlet",
            type: "POST",
            dataType: "json",
            data: {
                command: "translate",
                originWord: $("#originWord").val(),
                originLanguage: $("#originLanguageSelect option:selected").val(),
                translateLanguage: $("#destinationLanguageSelect option:selected").val()
            },
            success: function (response) {
                $.each(response, function (key, value) {
                    if (value === false) {
                        answersList.append('<li class="input-group my-2">'
                            + '<i class="input-group-text free-translate-icon bi bi-dash"></i>'
                            + '<a class="form-control translate-label">'
                            + key
                            + '</a>'
                            + '</li>');
                    } else {
                        answersList.append('<li class="input-group my-2">'
                            + '<i class="input-group-text saved-translate-icon bi bi-suit-heart-fill"></i>'
                            + '<a class="form-control translate-label">'
                            + key
                            + '</a>'
                            + '</li>');
                    }
                });
            },
            complete: function () {
                showButtonReset(button);
            },
            error: handleAjaxError
        });
    });


    function toggleIcon(iElem) {
        iElem.toggleClass("bi-dash");
        iElem.toggleClass("free-translate-icon");
        iElem.toggleClass("bi-suit-heart-fill");
        iElem.toggleClass("saved-translate-icon");
    }

    /**
     * Translate label event
     */
    let count = 0;
    $(document).on('click', 'li.input-group', function () {
        console.log("Count: "+count++);
        let elemParent = $(this);
        let iElem = elemParent.children("i");
        let action;
        if(iElem.hasClass("free-translate-icon")){
            action = "save";
        }else {
            action = "delete";
        }
        sendSaveChangesAjax(action, elemParent);
    });

    function sendSaveChangesAjax(action, elemParent) {

        let translationElem = elemParent.children("a").text();
        let translations = new Array(0);
        translations.push(translationElem);
        $.ajax({
            url: "AjaxControllerServlet",
            type: "POST",
            dataType: "json",
            data: {
                command: "saveChoices",
                action: action,
                translations: translations
            },
            success: function (response) {
                toggleIcon(elemParent.children("i"));
            },
            error: handleAjaxError
        });
    }
});