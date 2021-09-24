function handleAjaxError(xhr, textStatus, thrownError) {
    switch (xhr.status) {
        case 401:
            window.location.href = 'ControllerServlet?command=forward&to=signIn&error=401';
            break;
        default:
            alert("Code error: " + xhr.status + "\nMessage: " + xhr.responseText);
            break;
    }
}
