$(updateMyBooks());

function returnBook(id){
    $.ajax({
        url: URL_LOCALHOST + "/booking/return?id=" + id,
        type: "PUT",
        headers: {
            'Authorization': window.localStorage.getItem("Authorization"),
        },
        success: function (mybooks_json , status, xhr) {
            updateMyBooks();
            alert("Book has been returned");
        },
        error: function (mybooks_json, status, xhr) {
            alert("Book can't be returned");
            console.error(status);
            console.error(xhr);
        }
    });
}

function renewBook(id){
    $.ajax({
        url: URL_LOCALHOST + "/booking/renew?id=" + id,
        type: "PUT",
        headers: {
            'Authorization': window.localStorage.getItem("Authorization"),
        },
        success: function (mybooks_json , status, xhr) {
            updateMyBooks();
            alert("Book has been renewed");
        },
        error: function (mybooks_json, status, xhr) {
            alert("Failed");
            console.error(status);
            console.error(xhr);
        }
    });
}




function updateMyBooks(){
    $.ajax({
        url: URL_LOCALHOST + "/booking/findself",
        type: "GET",
        headers: {
            'Authorization': window.localStorage.getItem("Authorization"),
        },
        dataType: "json", // by this property ajax will automatically parse json which we get from response

        success: function (mybooks_json, status, xhr) {
            console.info("Responding json: ");
            console.info(mybooks_json);

            let outer =
                "<h4 class=\"border-bottom border-gray pb-2 mb-0\">My books database</h4>\n" +
                "\n" +
                "<div class=\"row mt-2 \\\">";

            //this list is not array, but list of <a>
            let listOfMybooks =
                "<div class=\"col-4\">\n" +
                "<div class=\"list-group\" id=\"list-tab\" role=\"tablist\">\n";

            let updatedMyooks = "<div class=\"col-8\">\n" +
                "<div class=\"tab-content\" id=\"nav-tabContent\">\n";

            let notFirst = false;
            for (let mybook in mybooks_json) {
                //add <a> (link to particular mybook) for listOfMybooks

                let currentMybook;

                if (notFirst) {
                    currentMybook =
                        "<div class='tab-pane fade' id=\'" + mybooks_json[mybook]["id"] +
                        "\' role='tabpanel' aria-labelledby='list" + mybooks_json[mybook]["id"] + "\'>\n";

                    listOfMybooks +=
                        "<a class=\"list-group-item list-group-item-action\" id=\"list" + mybooks_json[mybook]["id"] +
                        "\" data-toggle=\"list\"\n" +
                        "href=\"#" + mybooks_json[mybook]["id"] +
                        "\" role=\"tab\" aria-controls=\"list" + mybooks_json[mybook]["id"] + "\">" +
                        mybooks_json[mybook]["document"]["title"] + "</a>";
                }
                else {
                    currentMybook =
                        "<div class='tab-pane fade show active' id=\'" + mybooks_json[mybook]["id"] +
                        "\' role='tabpanel' aria-labelledby='list" + mybooks_json[mybook]["id"] + "\'>\n";


                    listOfMybooks += "<a class=\"list-group-item list-group-item-action active show\" id=\"list" + mybooks_json[mybook]["id"] +
                        "\" data-toggle=\"list\"\n" +
                        "href=\"#" + mybooks_json[mybook]["id"] +
                        "\" role=\"tab\" aria-controls=\"list" + mybooks_json[mybook]["id"] + "\">" +
                        mybooks_json[mybook]["document"]["title"] + "</a>";

                    notFirst = true;
                }

                currentMybook +=
                    "<dl class='dl-horizontal'>\n";


                //add all attributes
                for (let mybooksAttributes in mybooks_json[mybook]) {
                    currentMybook += "<dt>" + mybooksAttributes + "</dt>\n";
                    currentMybook += "<dd>";

                    if (mybooksAttributes === "document") {
                        let temp = mybooks_json[mybook]["document"]["title"];
                        currentMybook += temp;
                        currentMybook = currentMybook.replace("[object Object]","");
                    }
                    if (mybooksAttributes === "typeBooking") {
                        let temp = mybooks_json[mybook]["typeBooking"]["typeName"];
                        currentMybook += temp;
                        currentMybook = currentMybook.replace("[object Object]","");
                    }
                    if (mybooksAttributes === "user") {
                        let temp = mybooks_json[mybook]["user"]["name"] + " " + mybooks_json[mybook]["user"]["surname"];
                        currentMybook += temp;
                        currentMybook = currentMybook.replace("[object Object]","");

                    }

                    if (mybooksAttributes === "returnDate") {
                        let date = new Date(mybooks_json[mybook]["returnDate"]);
                        let day = date.getDate();
                        if(day.toString().length == 1)
                            day = "0" + day;
                        let month = date.getMonth();
                        if(month.toString().length == 1)
                            month = "0" + month;
                        let year = date.getFullYear();
                        currentMybook += year + "-" + month + "-" + day;
                        currentMybook = currentMybook.replace("[object Object]","");
                    }

                    else {
                        currentMybook += mybooks_json[mybook][mybooksAttributes];
                        currentMybook = currentMybook.replace("[object Object]","");
                    }
                    currentMybook = currentMybook.replace("[object Object]","");
                    currentMybook += "</dd>\n";
                }

                //close div for mybook
                currentMybook +=
                    "</dl>" +
                    "<small class='d-block text-right mt-3 border-bottom border-gray pb-2'>\n" +
                    "<button class='btn btn-outline-danger my-2 my-sm-0' onclick='returnBook(" + mybooks_json[mybook]["id"] + ")' " +
                    "type='submit'>Return book" +
                    "</button>\n" +
                    "<button class='btn btn-outline-success my-2 my-sm-0' onclick='renewBook(" + mybooks_json[mybook]["id"] + ")' " +
                    "type='submit'>Renew book" +
                    "</button>\n" +
                    "</small>\n" +
                    "</div>\n";


                updatedMyooks += currentMybook;

            }

            //close listOfMybooks block
            listOfMybooks +=
                " </div>\n" +
                "</div>\n";

            //close UsersBlock
            updatedMyooks +=
                "</div>\n" +
                "</div>\n";


            let updateButton = "<small class=\"d-block text-right mt-3\">\n" +
                "<button class=\"btn btn-outline-primary my-2 my-sm-0\" type=\"button\" onclick=\"updateMyBooks()\">Update</button>\n" +
                "</small>\n";

            outer += listOfMybooks;
            outer += updatedMyooks;
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




// function findbooking(id){
//     $.ajax({
//         url: URL_LOCALHOST + "/booking/find?id=" + "2",
//         type: "GET",
//         headers: {
//             'Authorization': window.localStorage.getItem("Authorization"),
//         },
//         success: function (mybooks_json, status, xhr) {
//             console.info("Responding json: ");
//             console.info(mybooks_json);
//
//         },
//         error: function (mybooks_json, status, xhr) {
//             alert("Book can't be returned");
//             console.error(status);
//             console.error(xhr);
//         }
//     });
// }

