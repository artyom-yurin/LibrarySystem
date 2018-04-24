$(updateLog());

function updateLog() {
    $.ajax({
        url: URL_LOCALHOST + "/log",
        type: "GET",
        headers: {
            'Authorization': window.localStorage.getItem("Authorization"),
        },
        dataType: "json", // by this property ajax will automatically parse json which we get from response

        success: function (log_json, status, xhr) {
            console.info("Responding json: ");
            console.info(log_json);

            let outer =
                "<h4 class=\"border-bottom border-gray pb-2 mb-0\">History of Activity</h4>\n" +
                "\n" +
                "<div class=\"row mt-2 \\\">";

            //this list is not array, but list of <a>
            let listOfLogs =
                "<div class=\"col-4\">\n" +
                "<div class=\"list-group\" id=\"list-tab\" role=\"tablist\">\n";

            let updatedLog = "<div class=\"col-8\">\n" +
                "<div class=\"tab-content\" id=\"nav-tabContent\">\n";

            let notFirst = false;
            for (let logs in log_json) {
                let currentLog;

                if (notFirst) {
                    currentLog =
                        "<div class='tab-pane fade' id=\'" + log_json[logs]["id"] +
                        "\' role='tabpanel' aria-labelledby='list" + log_json[logs]["id"] + "\'>\n";

                    listOfLogs +=
                        "<a class=\"list-group-item list-group-item-action\" id=\"list" + log_json[logs]["id"] +
                        "\" data-toggle=\"list\"\n" +
                        "href=\"#" + log_json[logs]["id"] +
                        "\" role=\"tab\" aria-controls=\"list" + log_json[logs]["id"] + "\">" +
                        log_json[logs]["id"] + "</a>";
                }
                else {
                    currentLog =
                        "<div class='tab-pane fade show active' id=\'" + log_json[logs]["id"] +
                        "\' role='tabpanel' aria-labelledby='list" + log_json[logs]["id"] + "\'>\n";


                    listOfLogs += "<a class=\"list-group-item list-group-item-action active show\" id=\"list" + log_json[logs]["id"] +
                        "\" data-toggle=\"list\"\n" +
                        "href=\"#" + log_json[logs]["id"] +
                        "\" role=\"tab\" aria-controls=\"list" + log_json[logs]["id"] + "\">" +
                        log_json[logs]["id"] + "</a>";

                    notFirst = true;
                }

                currentLog +=
                    "<dl class='dl-horizontal'>\n";


                //add all attributes
                for (let logsAttribute in log_json[logs]) {
                    currentLog += "<dt>" + logsAttribute + "</dt>\n";
                    currentLog += "<dd>";

                    if (logsAttribute === "user") {
                        let temp = log_json[logs]["user"]["name"] + " " + log_json[logs]["user"]["surname"];
                        currentLog += temp;
                        currentLog = currentLog.replace("[object Object]","");

                    }
                    if (logsAttribute === "logDate") {
                        let date = new Date(log_json[logs]["logDate"]);
                        let day = date.getDate();
                        if(day.toString().length == 1)
                            day = "0" + day;
                        let month = date.getMonth();
                        if(month.toString().length == 1)
                            month = "0" + month;
                        let year = date.getFullYear();
                        currentLog += year + "-" + month + "-" + day;
                    }

                    else {
                        currentLog += log_json[logs][logsAttribute];
                        currentLog = currentLog.replace("[object Object]","");
                    }
                    currentLog = currentLog.replace("[object Object]","");
                    currentLog += "</dd>\n";
                }

                //close div for logs
                currentLog +=
                    // "</dl>" +
                    // "<small class='d-block text-right mt-3 border-bottom border-gray pb-2'>\n" +
                    // "<button class='btn btn-outline-success my-2 my-sm-0' onclick='acceptConfirmation(" + log_json[logs]["id"] + ")' " +
                    // "type='submit'>Accept" +
                    // "</button>\n" +
                    // "</small>\n" +
                    "</div>\n";


                updatedLog += currentLog;
            }

            //close listOfLogs block
            listOfLogs +=
                " </div>\n" +
                "</div>\n";

            //close UsersBlock
            updatedLog +=
                "</div>\n" +
                "</div>\n";

            let updateButton = "<small class=\"d-block text-right mt-3\">\n" +
                "<button class=\"btn btn-outline-primary my-2 my-sm-0\" type=\"button\" onclick=\"updateLog()\">Update</button>\n" +
                "</small>\n";

            outer += listOfLogs;
            outer += updatedLog;
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