$(document).ready(function () {


    $(document).on('click touchstart', '.translate-label', function () {
        let iElem = $(this).parent().children("i");
        if (iElem.hasClass("free-translate-icon") || iElem.hasClass("prepare-to-save-translate")) {
            iElem.toggleClass("bi-dash");
            iElem.toggleClass("free-translate-icon");
            iElem.toggleClass("bi-check2");
            iElem.toggleClass("prepare-to-save-translate");
            return;
        }
        if (iElem.hasClass("saved-translate-icon") || iElem.hasClass("prepare-to-delete-translate")) {
            iElem.toggleClass("bi-check2-all");
            iElem.toggleClass("saved-translate-icon");
            iElem.toggleClass("bi-trash");
            iElem.toggleClass("prepare-to-delete-translate");
            return;
        }
    });


    function changePrepareSavedToSavedIcon() {
        $('i.prepare-to-save-translate').each(function () {
            $(this).removeClass("bi-check2");
            $(this).removeClass("prepare-to-save-translate");
            $(this).addClass("bi-check2-all");
            $(this).addClass("saved-translate-icon");
        });
    }

    function changePrepareDeleteToFreeIcon() {
        $('i.prepare-to-delete-translate').each(function () {
            $(this).removeClass("bi-trash");
            $(this).removeClass("prepare-to-delete-translate");
            $(this).addClass("bi-dash");
            $(this).addClass("free-translate-icon");
        });
    }

    function handleAjaxError(xhr, textStatus, thrownError) {
        alert("Code error: " + xhr.status + "\nMessage: " + xhr.responseText);
        if (xhr.status === 401) {
            window.location.href = 'ControllerServlet?command=forward&to=signIn';
        }
    }


    /*
    function showLoading(button){
        let children = $(this).children("span").each(function (){
            if($(this).hasClass("regular-button-text")){
                $(this).hide();
            }else {
                $(this).show();
            }
        });
    }

    function hideLoading(button){
        let children = $(this).children("span").each(function (){
            if($(this).hasClass("regular-button-text")){
                $(this).show();
            }else {
                $(this).hide();
            }
        });
    }

     */

    function showButtonLoading(elem) {
        $(elem).attr("regular-button-text", $(elem).html());
        $(elem).prop("disabled", true);
        $(elem).html('<i class="spinner-border spinner-border-sm"></i> Loading...');
    }

    function showButtonReset(elem) {
        $(elem).prop("disabled", false);
        $(elem).html($(elem).attr("regular-button-text"));
    }


    /*
    function toggleLoading(button) {
        let children = button.children("span").each(function () {
            $(this).fadeToggle();
        });
    }

     */


    /**
     * Translate button event
     */
    $("#translateButton").click(function () {

        //toggleLoading($(this));
        //showLoading($(this));
        let button = $(this);
        showButtonLoading(button);
        //$.blockUI();

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
                let answersList = $("#answers-list");
                answersList.empty();
                $.each(response, function (key, value) {
                    if (value === false) {
                        answersList.append('<li class="input-group">'
                            + '<i class="input-group-text free-translate-icon bi bi-dash"></i>'
                            + '<a class="form-control translate-label">'
                            + key
                            + '</a>'
                            + '</li>');
                    } else {
                        answersList.append('<li class="input-group">'
                            + '<i class="input-group-text saved-translate-icon bi bi-check2-all"></i>'
                            + '<a class="form-control translate-label">'
                            + key
                            + '</a>'
                            + '</li>');
                    }
                });
            },
            complete: function () {
                //toggleLoading($(this));
                //hideLoading($(this));
                showButtonReset(button);
                //$.unblockUI();
            },
            error: handleAjaxError
        });
    });

    /**
     * sendChanges button event
     */
    $("#saveChangesButton").click(function () {

        //toggleLoading($(this));
        let button = $(this);
        showButtonLoading(button);
        //$.blockUI();

        let arrToSave = new Array(0);
        $('i.prepare-to-save-translate').each(function () {
            arrToSave.push($(this).parent().children("a").text());
        });

        let arrToDelete = new Array(0);
        $('i.prepare-to-delete-translate').each(function () {
            arrToDelete.push($(this).parent().children("a").text());
        });

        console.log(arrToSave + " ;;;; "+arrToDelete);

        $.ajax({
            url: "AjaxControllerServlet",
            type: "POST",
            dataType: "json",
            data: {
                command: "saveChoices",
                arrToSave: arrToSave,
                arrToDelete: arrToDelete
            },
            success: function (response) {
                changePrepareSavedToSavedIcon();
                changePrepareDeleteToFreeIcon();
                /*
                let historyTextArea = $("#historyTextArea");
                let date = new Date();
                let timeString = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
                historyTextArea.val(timeString + ": " + response + '\n' + historyTextArea.val());
                 */
            },
            complete: function () {
                //toggleLoading($(this));
                showButtonReset(button);
            },
            error: handleAjaxError
        });
    });
});