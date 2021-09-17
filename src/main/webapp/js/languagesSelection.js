$(document).ready(function () {

    $("#toggleButton").click(function () {
        let originSelectedText = $("#originLanguageSelect option:selected").text();
        let destinationSelectedText = $("#destinationLanguageSelect option:selected").text();
        $("#originLanguageSelect").val(destinationSelectedText);
        $("#destinationLanguageSelect").val(originSelectedText);
    });
});