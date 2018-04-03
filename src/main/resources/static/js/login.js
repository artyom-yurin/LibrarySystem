$(checkIfUserIsAlreadyLogged());

function checkIfUserIsAlreadyLogged() {
    alert('checkBegin');
    if (localStorage.getItem('Authorization') != null) {
        alert("checkTrue");
        document.open();
        document.write(localStorage.getItem('lastPage'));
        document.close();
    }
    alert("checkEND");
}

function sendLoginAndPassword() {
    let user = $("#inputUsername").val();
    let pass = $("#inputPassword").val();

    let userData = JSON.stringify({
        username: user,
        password: pass

    });

    $.ajax({
            url: URL_LOCALHOST + "user/login",
            type: "POST",
            headers: {
                'Content-Type': "application/json"
            },
            contentType: "application/json; charset=utf-8",
            async: false,
            data: userData,

            success: function (html_data, status, xhr) {
                let token = xhr.getResponseHeader("Authorization");

                window.localStorage.setItem("Authorization", token);

                document.open();
                document.write(html_data);
                document.close();
            },

            error: function (data, status, xhr) {
                console.error('Error_data: ', data);
                console.error('Error_status: ', status);
                console.error('Error_xhr: ', xhr);
            }

        }
    );
}

// Constants
var URL_LOCALHOST = "http://localhost:8080/";

var linkToUsers = URL_LOCALHOST + 'admin/users.html';
var linkToBooks = URL_LOCALHOST + 'admin/documents.html';
var linkToRequest = URL_LOCALHOST + 'admin/requests.html';
var linkToCatalog = URL_LOCALHOST + 'catalog.html';
var linkToMyBooks = URL_LOCALHOST + 'myBooks.html';
var linkToConfirmation = URL_LOCALHOST + 'admin/confirmation.html';
var linkToNotification = URL_LOCALHOST + 'notifications.html';

window.onbeforeunload = function () {
    localStorage.setItem('lastPage', $("html")[0]);
};



