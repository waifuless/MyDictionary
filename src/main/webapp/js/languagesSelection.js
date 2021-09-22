$(document).ready(function () {

    $("#toggleButton").click(function () {
        let originSelectedText = $("#originLanguageSelect option:selected").val();
        let destinationSelectedText = $("#destinationLanguageSelect option:selected").val();
        $("#originLanguageSelect").val(destinationSelectedText);
        $("#destinationLanguageSelect").val(originSelectedText);
    });
});