$(document).ready(function(){

    function handleAjaxError(xhr, textStatus, thrownError){
        alert("Code error: "+xhr.status +"\nMessage: "+ xhr.responseText);
        if(xhr.status===401){
            window.location.href = 'ControllerServlet?command=forward&to=signIn';
        }
    }

    /**
     * Translate button event
     */
    $("#translateButton").click(function(){

        $(".div__loading").show();

        $.ajax({
            url: "AjaxControllerServlet",
            type: "POST",
            dataType: "json",
            data: {
                command: "translate",
                originWord: $("#originWord").val(),
                originLanguage : $("#originLanguageSelect option:selected").val(),
                translateLanguage : $("#destinationLanguageSelect option:selected").val()
            },
            success: function(response) {
                let answersList = $("#answers-list");
                answersList.empty();
                let i=0;
                $.each(response, function(key, value){
                    if(value===false){
                        answersList.append('<li><input hidden type="checkbox" name="translateCheckbox" id="'
                            +i+'" value="'+key+'"> <label for="'+i+'">'+key+'</label></li>');
                    }else{
                        answersList.append('<li><input hidden type="checkbox" name="translateCheckbox" id="'
                            +i+'" value="'+key+'" checked> <label for="'+i+'">'+key+'</label></li>');
                    }
                    i++;
                });
            },
            complete: function () {
                $(".div__loading").hide();
            },
            error: handleAjaxError
        });
    });

    /**
     * sendChoices button event
     */
    $("#sendChoicesButton").click(function(){

        $(".div__loading").show();

        let arrChecked = new Array(0);
        $('input[name="translateCheckbox"]:checked').each(function() {
            arrChecked.push((this.value));
        });

        let arrUnchecked = new Array(0);
        $('input[name="translateCheckbox"]:not(:checked)').each(function() {
            arrUnchecked.push((this.value));
        });

        $.ajax({
            url: "AjaxControllerServlet",
            type: "POST",
            dataType: "json",
            data: {
                command: "sendChoices",
                arrChecked: arrChecked,
                arrUnchecked: arrUnchecked
            },
            success: function(response) {
                alert(response);
                let historyTextArea = $("#historyTextArea");
                let date = new Date();
                let timeString = date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                historyTextArea.val(timeString+": "+response+'\n'+historyTextArea.val());
            },
            complete: function () {
                $(".div__loading").hide();
            },
            error: handleAjaxError
        });
    });
});