$(updateUsers());  // it will be called when document will be loaded
                   // P.S. document - it is html.

function updateUsers() {
    $.ajax({
        url: URL_LOCALHOST + "/user/users",
        type: "GET",
        headers: {
            'Authorization': window.localStorage.getItem("Authorization"),
        },
        dataType: "json", // by this property ajax will automatically parse json which we get from response

        success: function (users, status, xhr) {
            console.info("Responding json: ");
            console.info(users);

            let outer =
                "<h4 class=\"border-bottom border-gray pb-2 mb-0\">Users database</h4>\n" +
                "\n" +
                "<div class=\"row mt-2 \\\">";

            //this list is not array, but list of <a>
            let listOfUsers =
                "<div class=\"col-4\">\n" +
                "<div class=\"list-group\" id=\"list-tab\" role=\"tablist\">\n";

            let updatedUsers = "<div class=\"col-8\">\n" +
                "<div class=\"tab-content\" id=\"nav-tabContent\">\n";

            let notFirst = false;
            for (let user in users) {
                //add <a> (link to particular user) for listOfUsers

                let currentUser;

                //open div for user
                if (notFirst) {
                    currentUser =
                        "<div class='tab-pane fade' id=\'" + users[user]["id"] +
                        "\' role='tabpanel' aria-labelledby='list" + users[user]["id"] + "\'>\n";

                    listOfUsers += "<a class=\"list-group-item list-group-item-action\" id=\"list" + users[user]["id"] +
                        "\" data-toggle=\"list\"\n" +
                        "href=\"#" + users[user]["id"] +
                        "\" role=\"tab\" aria-controls=\"list" + users[user]["id"] + "\" aria-selected=\"false\">" +
                        users[user]["username"] + "</a>";

                }
                else {
                    currentUser =
                        "<div class='tab-pane fade show active' id=\'" + users[user]["id"] +
                        "\' role='tabpanel' aria-labelledby='list" + users[user]["id"] + "\'>\n";

                    listOfUsers += "<a class=\"list-group-item list-group-item-action active show\" id=\"list" + users[user]["id"] +
                        "\" data-toggle=\"list\"\n" +
                        "href=\"#" + users[user]["id"] +
                        "\" role=\"tab\" aria-controls=\"list" + users[user]["id"] + "\" aria-selected=\"true\">" +
                        users[user]["username"] + "</a>";

                    notFirst = true;
                }

                currentUser +=
                    "<dl class='dl-horizontal'>\n";

                //add all attributes
                for (let userAttribute in users[user]) {
                    currentUser += "<dt>" + userAttribute + "</dt>\n";
                    currentUser += "<dd>";

                    if (userAttribute === "role") { // here we need "if" because role has 2 (key : value) pairs, not only one
                        currentUser += users[user]["role"]["name"]
                    }
                    else {
                        currentUser += users[user][userAttribute]
                    }

                    currentUser += "</dd>\n";
                }


                //close div for user
                currentUser +=
                    "</dl>" +
                    "<small class='d-block text-right mt-3 border-bottom border-gray pb-2'>\n" +
                    "<button class='btn btn-outline-danger my-2 my-sm-0' onclick='deleteUser(" + users[user]["id"] + ")' " +
                    "type='submit'>Delete" +
                    "</button>\n" +
                    "</small>\n" +
                    "</div>\n";

                updatedUsers += currentUser;
            }

            //close listOfUsers block
            listOfUsers +=
                " </div>\n" +
                "</div>\n";

            //close UsersBlock
            updatedUsers +=
                "</div>\n" +
                "</div>\n";


            let updateButton = "<small class=\"d-block text-right mt-3\">\n" +
                "<button class=\"btn btn-outline-primary my-2 my-sm-0\" type=\"button\" onclick=\"updateUsers()\">Update</button>\n" +
                "</small>\n";

            outer += listOfUsers;
            outer += updatedUsers;
            outer += "</div>\n";
            outer += updateButton;


            //Final load in html. It replace everything inside <div id = "database'> which is container for our database.
            $("#database").html(outer);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("failed in getting users");

            console.error(jqXHR);
            console.error(textStatus);
            console.error(errorThrown);
        }
    });
}

// When we modify(update) we must put id when add we do not obligated
function addUser() {
    let firstName = $("#firstName").val();
    let lastName = $("#lastName").val();
    let username = $("#username").val();
    let password = $("#password").val();
    let phone = $("#phone").val();
    let address = $("#address").val();
    let role = $("#role").val();


    let jsonData = JSON.stringify({
        "name": firstName,
        "surname": lastName,
        "address": address,
        "phone": phone,
        "username": username,
        "login": username,
        "password": password,
        "role": role
    });

    {
        $.ajax({
            url: URL_LOCALHOST + "/user/add",
            type: "POST", //onUpdate use put
            headers: {
                'Authorization': window.localStorage.getItem("Authorization"),
                'Content-Type': "application/json"
            },
            contentType: "application/json; charset=utf-8",
            data: jsonData,
            success: function (data, status, xhr) {
                alert("User added");
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert("Fail User addition");
                console.error(jqXHR);
                console.error(textStatus);
                console.error(errorThrown);
            }
        });

    }
}

function deleteUser(id) {
    alert(id);
    $.ajax({
        url: URL_LOCALHOST + "/user/remove?id=" + id,
        type: "DELETE",
        headers: {
            'Authorization': window.localStorage.getItem("Authorization"),
        },
        success: function (users, status, xhr) {
            updateUsers();
            alert("ura");
        },
        error: function (users, status, xhr) {
            alert("ne ura");
            console.error(status);
            console.error(xhr);
        }
    });
}



