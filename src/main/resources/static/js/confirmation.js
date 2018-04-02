$(updateRequests());

function updateRequests() {
    $.ajax({
        url: URL_LOCALHOST + "/booking/find",
        type: "GET",
        headers: {
            'Authorization': window.localStorage.getItem("Authorization"),
        },
        dataType: "json", // by this property ajax will automatically parse json which we get from response

        success: function (requests_json, status, xhr) {
            console.info("Responding json: ");
            console.info(requests_json);

            let outer =
                "<h4 class=\"border-bottom border-gray pb-2 mb-0\">Requests database</h4>\n" +
                "\n" +
                "<div class=\"row mt-2 \\\">";

            //this list is not array, but list of <a>
            let listOfRequests =
                "<div class=\"col-4\">\n" +
                "<div class=\"list-group\" id=\"list-tab\" role=\"tablist\">\n";

            let updatedRequests = "<div class=\"col-8\">\n" +
                "<div class=\"tab-content\" id=\"nav-tabContent\">\n";

            let notFirst = false;
            for (let request in requests_json) {
                //add <a> (link to particular request) for listOfRequests


                let currentRequest;

                if (notFirst) {
                    currentRequest =
                        "<div class='tab-pane fade' id=\'" + requests_json[request]["id"] +
                        "\' role='tabpanel' aria-labelledby='list" + requests_json[request]["id"] + "\'>\n";

                    listOfRequests +=
                        "<a class=\"list-group-item list-group-item-action\" id=\"list" + requests_json[request]["id"] +
                        "\" data-toggle=\"list\"\n" +
                        "href=\"#" + requests_json[request]["id"] +
                        "\" role=\"tab\" aria-controls=\"list" + requests_json[request]["id"] + "\">" +
                        requests_json[request]["id"] + "</a>";
                }
                else {
                    currentRequest =
                        "<div class='tab-pane fade show active' id=\'" + requests_json[request]["id"] +
                        "\' role='tabpanel' aria-labelledby='list" + requests_json[request]["id"] + "\'>\n";


                    listOfRequests += "<a class=\"list-group-item list-group-item-action active show\" id=\"list" + requests_json[request]["id"] +
                        "\" data-toggle=\"list\"\n" +
                        "href=\"#" + requests_json[request]["id"] +
                        "\" role=\"tab\" aria-controls=\"list" + requests_json[request]["id"] + "\">" +
                        requests_json[request]["id"] + "</a>";

                    notFirst = true;
                }

                currentRequest +=
                    "<dl class='dl-horizontal'>\n";


                //add all attributes
                for (let requestAttributes in requests_json[request]) {
                    currentRequest += "<dt>" + requestAttributes + "</dt>\n";
                    currentRequest += "<dd>";

                    if (requestAttributes === "document") {
                        let temp = requests_json[request]["document"]["title"];
                        currentRequest += temp;
                        currentRequest = currentRequest.replace("[object Object]","");
                    }
                    if (requestAttributes === "typeBooking") {
                        let temp = requests_json[request]["typeBooking"]["typeName"];
                        currentRequest += temp;
                        currentRequest = currentRequest.replace("[object Object]","");
                    }
                    if (requestAttributes === "user") {
                        let temp = requests_json[request]["user"]["name"] + " " + requests_json[request]["user"]["surname"];
                        currentRequest += temp;
                        currentRequest = currentRequest.replace("[object Object]","");

                    }

                    if (requestAttributes === "returnDate") {
                        let date = new Date(requests_json[request]["returnDate"]);
                        currentRequest += date.toString();
                        currentRequest = currentRequest.replace("[object Object]","");
                    }

                    else {
                        currentRequest += requests_json[request][requestAttributes];
                        currentRequest = currentRequest.replace("[object Object]","");
                    }
                    currentRequest = currentRequest.replace("[object Object]","");
                    currentRequest += "</dd>\n";
                }

                //close div for request
                currentRequest +=
                    "</dl>" +
                    "<small class='d-block text-right mt-3 border-bottom border-gray pb-2'>\n" +
                    "<button class='btn btn-outline-success my-2 my-sm-0' onclick='acceptRequest(" + requests_json[request]["id"] + ")' " +
                    "type='submit'>Accept" +
                    "</button>\n" +
                    "</small>\n" +
                    "</div>\n";


                updatedRequests += currentRequest;

            }

            //close listOfRequests block
            listOfRequests +=
                " </div>\n" +
                "</div>\n";

            //close UsersBlock
            updatedRequests +=
                "</div>\n" +
                "</div>\n";


            let updateButton = "<small class=\"d-block text-right mt-3\">\n" +
                "<button class=\"btn btn-outline-primary my-2 my-sm-0\" type=\"button\" onclick=\"updateRequests()\">Update</button>\n" +
                "</small>\n";

            outer += listOfRequests;
            outer += updatedRequests;
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