// Constants
var URL_LOCALHOST = "http://localhost:8080/";

var linkToUsers = URL_LOCALHOST + 'librarian/users.html';
var linkToBooks = URL_LOCALHOST + 'librarian/documents.html';
var linkToRequest = URL_LOCALHOST + 'librarian/requests.html';
var linkToCatalog = URL_LOCALHOST + 'catalog.html';
var linkToMyBooks = URL_LOCALHOST + 'myBooks.html';
var linkToConfirmation = URL_LOCALHOST + 'librarian/confirmation.html';
var linkToNotification = URL_LOCALHOST + 'notifications.html';

//We call it from login's body
function checkIfUserIsAlreadyLogged() {
    if (localStorage.getItem('Authorization') != null && localStorage.getItem('page') != null) {
        var tmp = localStorage.getItem('page');

        console.log(tmp);

        document.open();
        document.write(tmp);
        document.close();
    }
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

                window.localStorage.setItem('page', html_data);

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




