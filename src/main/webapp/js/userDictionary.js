$(document).ready(function(){

    function handleAjaxError(xhr, textStatus, thrownError){
        alert("Code error: "+xhr.status +"\nMessage: "+ xhr.responseText);
        if(xhr.status===401){
            window.location.href = 'ControllerServlet?command=forward&to=signIn';
        }
    }

    $("#receiveTranslatesButton").click(function (){

        $(".div__loading").show();
        $(".div__saved-words-table").hide();

        $.ajax({
            url: "AjaxControllerServlet",
            type: "POST",
            dataType: "json",
            data: {
                command: "receiveAllTranslationsByLanguage",
                originLanguage : $("#originLanguageSelect option:selected").val(),
                translateLanguage : $("#destinationLanguageSelect option:selected").val()
            },
            success: function(response) {
                $("#savedWordsTable").find("tr:gt(0)").remove();
                $.each(response, function(key, value){
                    let translationsString = value.join('; ');
                    $('#savedWordsTable tr:last').after('<tr><td>'+key+'</td><td>'+translationsString+'</td></tr>');
                    $(".div__saved-words-table").show();
                });
            },
            complete: function () {
                $(".div__loading").hide();
            },
            error: handleAjaxError
        });
    });
});