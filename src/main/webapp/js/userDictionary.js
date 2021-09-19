$(document).ready(function(){

    $("#receiveTranslatesButton").click(function (){
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
                    /*
                    $.each(value, function (valueElem){
                        translationsString+=(valueElem+'; ');
                    });
                     */
                    $('#savedWordsTable tr:last').after('<tr><td>'+key+'</td><td>'+translationsString+'</td></tr>');
                });
            }
        });
    });
});