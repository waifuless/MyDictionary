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

    function showButtonRecover(elem) {
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
                            + '<div class="input-group-text">'
                            + '<i class="free-translate-icon bi bi-dash"></i>'
                            + '</div>'
                            + '<a class="form-control translate-label">'
                            + key
                            + '</a>'
                            + '</li>');
                    } else {
                        answersList.append('<li class="input-group my-2">'
                            + '<div class="input-group-text">'
                            + '<i class="saved-translate-icon bi bi-suit-heart-fill"></i>'
                            + '</div>'
                            + '<a class="form-control translate-label">'
                            + key
                            + '</a>'
                            + '</li>');
                    }
                });
            },
            complete: function () {
                showButtonRecover(button);
            },
            error: handleAjaxError
        });
    });


    function toggleLoadingAndIcon(iElem, action){
        iElem.toggleClass("spinner-border");
        iElem.toggleClass("spinner-border-sm");
        iElem.toggleClass("bi");
        if(action.localeCompare("delete")===0){
            iElem.toggleClass("saved-translate-icon");
            iElem.toggleClass("bi-suit-heart-fill");
        }else{
            iElem.toggleClass("free-translate-icon");
            iElem.toggleClass("bi-dash");
        }
    }

    function updateIconAfterSuccessSave(iElem, madeAction){
        iElem.removeClass("spinner-border");
        iElem.removeClass("spinner-border-sm");
        iElem.addClass("bi");
        if(madeAction.localeCompare("save")===0){
            iElem.addClass("saved-translate-icon");
            iElem.addClass("bi-suit-heart-fill");
        }else{
            iElem.addClass("free-translate-icon");
            iElem.addClass("bi-dash");
        }
    }


    /**
     * Translate label event
     */
    $(document).on('click', 'li.input-group', function () {
        let elemParent = $(this);
        let iElem = elemParent.children("div").children("i");
        let translation = elemParent.children("a").text();
        let action;
        if (iElem.hasClass("free-translate-icon")) {
            action = "save";
        } else {
            action = "delete";
        }
        sendSaveChangesAjax(elemParent, iElem, translation, action);
    });


    function sendSaveChangesAjax(elemParent, iElem, translation, action) {
        let translations = new Array(0);
        translations.push(translation);
        elemParent.prop('disabled',true);
        toggleLoadingAndIcon(iElem, action);
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
                updateIconAfterSuccessSave(iElem, action);
            },
            complete: function (){
                elemParent.prop('disabled',false);
            },
            error: function (xhr, textStatus, thrownError){
                toggleLoadingAndIcon(iElem, action);
                handleAjaxError(xhr, textStatus, thrownError);
            }
        });
    }
});