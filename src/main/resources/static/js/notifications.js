$(updateNotifications());

function updateNotifications() {
    $.ajax({
        url: URL_LOCALHOST + "/notification/findself",
        type: "GET",
        headers: {
            'Authorization': window.localStorage.getItem("Authorization"),
        },
        dataType: "json", // by this property ajax will automatically parse json which we get from response

        success: function (notifications_json, status, xhr) {
            console.info("Responding json: ");
            console.info(notifications_json);

            let outer =
                "<h4 class=\"border-bottom border-gray pb-2 mb-0\">Notifications</h4>\n" +
                "\n" +
                "<div class=\"row mt-2 \\\">";

            //this list is not array, but list of <a>
            let listOfConfirmation =
                "<div class=\"col-4\">\n" +
                "<div class=\"list-group\" id=\"list-tab\" role=\"tablist\">\n";

            let updatedConfirmation = "<div class=\"col-8\">\n" +
                "<div class=\"tab-content\" id=\"nav-tabContent\">\n";

            let notFirst = false;
            for (let notification in notifications_json) {
                let currentConfirmation;

                if (notFirst) {
                    currentConfirmation =
                        "<div class='tab-pane fade' id=\'" + notifications_json[notification]["id"] +
                        "\' role='tabpanel' aria-labelledby='list" + notifications_json[notification]["id"] + "\'>\n";

                    listOfConfirmation +=
                        "<a class=\"list-group-item list-group-item-action\" id=\"list" + notifications_json[notification]["id"] +
                        "\" data-toggle=\"list\"\n" +
                        "href=\"#" + notifications_json[notification]["id"] +
                        "\" role=\"tab\" aria-controls=\"list" + notifications_json[notification]["id"] + "\">" +
                        notifications_json[notification]["id"] + "</a>";
                }
                else {
                    currentConfirmation =
                        "<div class='tab-pane fade show active' id=\'" + notifications_json[notification]["id"] +
                        "\' role='tabpanel' aria-labelledby='list" + notifications_json[notification]["id"] + "\'>\n";


                    listOfConfirmation += "<a class=\"list-group-item list-group-item-action active show\" id=\"list" + notifications_json[notification]["id"] +
                        "\" data-toggle=\"list\"\n" +
                        "href=\"#" + notifications_json[notification]["id"] +
                        "\" role=\"tab\" aria-controls=\"list" + notifications_json[notification]["id"] + "\">" +
                        notifications_json[notification]["id"] + "</a>";

                    notFirst = true;
                }

                currentConfirmation +=
                    "<dl class='dl-horizontal'>\n";


                //add all attributes
                for (let notificationAttribute in notifications_json[notification]) {
                    currentConfirmation += "<dt>" + notificationAttribute + "</dt>\n";
                    currentConfirmation += "<dd>";

                    if (notificationAttribute === "document") {
                        let temp = notifications_json[notification]["document"]["title"];
                        currentConfirmation += temp;
                      //  currentConfirmation = currentConfirmation.replace("[object Object]","");
                    }
                    if (notificationAttribute === "typeBooking") {
                        let temp = notifications_json[notification]["typeBooking"]["typeName"];
                        currentConfirmation += temp;
                        //currentConfirmation = currentConfirmation.replace("[object Object]","");
                    }
                    if (notificationAttribute === "user") {
                        let temp = notifications_json[notification]["user"]["name"] + " " + notifications_json[notification]["user"]["surname"];
                        currentConfirmation += temp;
                       // currentConfirmation = currentConfirmation.replace("[object Object]","");

                    }

                    if (notificationAttribute === "returnDate") {
                        let date = new Date(notifications_json[notification]["returnDate"]);
                        currentConfirmation += date.toString();
                       // currentConfirmation = currentConfirmation.replace("[object Object]","");
                    }

                    else {
                        currentConfirmation += notifications_json[notification][notificationAttribute];
                       // currentConfirmation = currentConfirmation.replace("[object Object]","");
                    }
                   // currentConfirmation = currentConfirmation.replace("[object Object]","");
                    currentConfirmation += "</dd>\n";
                }

                //close div for notification
                currentConfirmation +=
                    "</dl>" +
                    "<small class='d-block text-right mt-3 border-bottom border-gray pb-2'>\n" +
                    "<button class='btn btn-outline-success my-2 my-sm-0' onclick='acceptConfirmation(" + notifications_json[notification]["id"] + ")' " +
                    "type='submit'>Accept" +
                    "</button>\n" +
                    "</small>\n" +
                    "</div>\n";


                updatedConfirmation += currentConfirmation;
            }

            //close listOfConfirmation block
            listOfConfirmation +=
                " </div>\n" +
                "</div>\n";

            //close UsersBlock
            updatedConfirmation +=
                "</div>\n" +
                "</div>\n";

            let updateButton = "<small class=\"d-block text-right mt-3\">\n" +
                "<button class=\"btn btn-outline-primary my-2 my-sm-0\" type=\"button\" onclick=\"updateNotifications()\">Update</button>\n" +
                "</small>\n";

            outer += listOfConfirmation;
            outer += updatedConfirmation;
            outer += "</div>\n";
            outer += updateButton;

            //Final load in html. It replace everything inside <div id = "database'> which is container for our database.
            $("#database").html(outer);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("failed in getting requests");

            console.error(jqXHR);
            console.error(textStatus);
            console.error(errorThrown);
        }
    });
}

