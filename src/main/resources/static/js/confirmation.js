$(updateConfirmation());

function updateConfirmation() {
    $.ajax({
        url: URL_LOCALHOST + "/booking/findavailable",
        type: "GET",
        headers: {
            'Authorization': window.localStorage.getItem("Authorization"),
        },
        dataType: "json", // by this property ajax will automatically parse json which we get from response

        success: function (confirmation_json, status, xhr) {
            console.info("Responding json: ");
            console.info(confirmation_json);

            let outer =
                "<h4 class=\"border-bottom border-gray pb-2 mb-0\">Requests to checkout</h4>\n" +
                "\n" +
                "<div class=\"row mt-2 \\\">";

            //this list is not array, but list of <a>
            let listOfConfirmation =
                "<div class=\"col-4\">\n" +
                "<div class=\"list-group\" id=\"list-tab\" role=\"tablist\">\n";

            let updatedConfirmation = "<div class=\"col-8\">\n" +
                "<div class=\"tab-content\" id=\"nav-tabContent\">\n";

            let notFirst = false;
            for (let confirmation in confirmation_json) {
                //add <a> (link to particular confirmation) for listOfConfirmation


                let currentConfirmation;

                if (notFirst) {
                    currentConfirmation =
                        "<div class='tab-pane fade' id=\'" + confirmation_json[confirmation]["id"] +
                        "\' role='tabpanel' aria-labelledby='list" + confirmation_json[confirmation]["id"] + "\'>\n";

                    listOfConfirmation +=
                        "<a class=\"list-group-item list-group-item-action\" id=\"list" + confirmation_json[confirmation]["id"] +
                        "\" data-toggle=\"list\"\n" +
                        "href=\"#" + confirmation_json[confirmation]["id"] +
                        "\" role=\"tab\" aria-controls=\"list" + confirmation_json[confirmation]["id"] + "\">" +
                        confirmation_json[confirmation]["document"]["title"] + "</a>";
                }
                else {
                    currentConfirmation =
                        "<div class='tab-pane fade show active' id=\'" + confirmation_json[confirmation]["id"] +
                        "\' role='tabpanel' aria-labelledby='list" + confirmation_json[confirmation]["id"] + "\'>\n";


                    listOfConfirmation += "<a class=\"list-group-item list-group-item-action active show\" id=\"list" + confirmation_json[confirmation]["id"] +
                        "\" data-toggle=\"list\"\n" +
                        "href=\"#" + confirmation_json[confirmation]["id"] +
                        "\" role=\"tab\" aria-controls=\"list" + confirmation_json[confirmation]["id"] + "\">" +
                        confirmation_json[confirmation]["document"]["title"] + "</a>";

                    notFirst = true;
                }

                currentConfirmation +=
                    "<dl class='dl-horizontal'>\n";


                //add all attributes
                for (let confirmationAttributes in confirmation_json[confirmation]) {
                    currentConfirmation += "<dt>" + confirmationAttributes + "</dt>\n";
                    currentConfirmation += "<dd>";

                    if (confirmationAttributes === "document") {
                        let temp = confirmation_json[confirmation]["document"]["title"];
                        currentConfirmation += temp;
                        currentConfirmation = currentConfirmation.replace("[object Object]","");
                    }
                    if (confirmationAttributes === "typeBooking") {
                        let temp = confirmation_json[confirmation]["typeBooking"]["typeName"];
                        currentConfirmation += temp;
                        currentConfirmation = currentConfirmation.replace("[object Object]","");
                    }
                    if (confirmationAttributes === "user") {
                        let temp = confirmation_json[confirmation]["user"]["name"] + " " + confirmation_json[confirmation]["user"]["surname"];
                        currentConfirmation += temp;
                        currentConfirmation = currentConfirmation.replace("[object Object]","");

                    }

                    if (confirmationAttributes === "returnDate") {
                        let date = new Date(confirmation_json[confirmation]["returnDate"]);
                        let day = date.getDate();
                        if(day.toString().length == 1)
                            day = "0" + day;
                        let month = date.getMonth();
                        if(month.toString().length == 1)
                            month = "0" + month;
                        let year = date.getFullYear();
                        currentConfirmation += year + "-" + month + "-" + day;
                        currentConfirmation = currentConfirmation.replace("[object Object]","");
                    }

                    else {
                        currentConfirmation += confirmation_json[confirmation][confirmationAttributes];
                        currentConfirmation = currentConfirmation.replace("[object Object]","");
                    }
                    currentConfirmation = currentConfirmation.replace("[object Object]","");
                    currentConfirmation += "</dd>\n";
                }

                //close div for confirmation
                currentConfirmation +=
                    "</dl>" +
                    "<small class='d-block text-right mt-3 border-bottom border-gray pb-2'>\n" +
                    "<button class='btn btn-outline-success my-2 my-sm-0' onclick='acceptConfirmation(" + confirmation_json[confirmation]["id"] + ")' " +
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
                "<button class=\"btn btn-outline-primary my-2 my-sm-0\" type=\"button\" onclick=\"updateConfirmation()\">Update</button>\n" +
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

function acceptConfirmation(id){
    $.ajax({
        url: URL_LOCALHOST + "/booking/take?id=" + id,
        type: "PUT",
        headers: {
            'Authorization': window.localStorage.getItem("Authorization"),
        },
        success: function (requests_json, status, xhr) {
            updateConfirmation();
        },
        error: function (requests_json, status, xhr) {
            alert("Failed");
            console.error(status);
            console.error(xhr);
        }
    });
}
