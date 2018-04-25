$(updateBooks());
init();
// $("#addBookForm").submit(function(e) {
//     e.preventDefault();
// });
// $("#modifyBookForm").submit(function(e) {
//     e.preventDefault();
// });


var currentBookId = -1;
var bookAttributesWeHave = [];

function pushNewBookAttribute(attr) {
    for (let i in bookAttributesWeHave) {
        if (attr === bookAttributesWeHave[i])
            return false;
    }
    bookAttributesWeHave.push(attr);
}

function setCurrentBook(id) {
    currentBookId = id;
    fillInputsInBookModify();
}

function makeCapital(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

function updateBooks() {
    $.ajax({
        url: URL_LOCALHOST + "/document/documents",
        type: "GET",
        headers: {
            'Authorization': window.localStorage.getItem("Authorization"),
        },
        dataType: "json", // by this property ajax will automatically parse json which we get from response

        success: function (books_json, status, xhr) {
            displayBooks(books_json);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("failed in getting books");

            console.error(jqXHR);
            console.error(textStatus);
            console.error(errorThrown);
        }
    });
}

function displayBooks(books_json) {
    console.info("Responding json: ");
    console.info(books_json);

    let title = $("#htitle")[0].innerHTML;
    let outer =
        "<h4 class=\"border-bottom border-gray pb-2 mb-0\">Library</h4>\n" +
        "\n" +
        "<div class=\"row mt-2 \\\">";

    //this list is not array, but list of <a>
    let listOfBooks =
        "<div class=\"col-4\">\n" +
        "<div class=\"list-group\" id=\"list-tab\" role=\"tablist\">\n";

    let updatedBooks = "<div class=\"col-8\">\n" +
        "<div class=\"tab-content\" id=\"nav-tabContent\">\n";

    let notFirst = false;
    for (let book in books_json) {
        //add <a> (link to particular book) for listOfBooks

        let currentBook;

        //open div for book
        if (notFirst) {
            currentBook =
                "<div class='tab-pane fade' id=\'" + books_json[book]["id"] +
                "\' role='tabpanel' aria-labelledby='list" + books_json[book]["id"] + "\'>\n";

            listOfBooks +=
                "<a class=\"list-group-item list-group-item-action\" id=\"list" + books_json[book]["id"] +
                "\" data-toggle=\"list\"\n" +
                "href=\"#" + books_json[book]["id"] +
                "\" role=\"tab\" aria-controls=\"list" + books_json[book]["id"] + "\">" +
                books_json[book]["title"] + "</a>";
        }
        else {
            currentBook =
                "<div class='tab-pane fade show active' id=\'" + books_json[book]["id"] +
                "\' role='tabpanel' aria-labelledby='list" + books_json[book]["id"] + "\'>\n";


            listOfBooks += "<a class=\"list-group-item list-group-item-action active show\" id=\"list" + books_json[book]["id"] +
                "\" data-toggle=\"list\"\n" +
                "href=\"#" + books_json[book]["id"] +
                "\" role=\"tab\" aria-controls=\"list" + books_json[book]["id"] + "\">" +
                books_json[book]["title"] + "</a>";

            notFirst = true;
        }

        currentBook +=
            "<dl class='dl-horizontal'>\n";


        //add all attributes
        for (let bookAttributes in books_json[book]) {
            pushNewBookAttribute(bookAttributes);

            if (bookAttributes === "publishingDate") {
                currentBook += "<dt>Publishing Date</dt>\n";
            }
            else {
                currentBook += "<dt>" + makeCapital(bookAttributes) + "</dt>\n";
            }


            currentBook += "<dd id ='" + bookAttributes + books_json[book]["id"] + "d'>";

            if (bookAttributes === "publisher") {
                currentBook += books_json[book]["publisher"]["publisherName"]
            }

            else if (bookAttributes === "type") {
                currentBook += books_json[book]["type"]["typeName"]
            }

            else if (bookAttributes === "authors") {
                let listOfAuthors = "";
                for (let particular_author in books_json[book]["authors"]) {
                    listOfAuthors += books_json[book]["authors"][particular_author]["firstName"] + " " + books_json[book]["authors"][particular_author]["lastName"] + ',';
                }
                listOfAuthors = listOfAuthors.trim().substring(0, listOfAuthors.length - 1);
                currentBook += listOfAuthors;
            }

            else if (bookAttributes === "publishingDate") {
                let date = new Date(books_json[book]["publishingDate"]);
                let day = date.getDate();
                if (day.toString().length == 1)
                    day = "0" + day;
                let month = date.getMonth();
                if (month.toString().length == 1)
                    month = "0" + month;
                let year = date.getFullYear();
                currentBook += year + "-" + month + "-" + day;
            }

            else if (bookAttributes === "tags") {
                let listOfTags = "";
                for (let particular_tag in books_json[book]["tags"]) {
                    listOfTags += books_json[book]["tags"][particular_tag]["tagName"];
                }
                currentBook += listOfTags;
            }

            else {
                currentBook += books_json[book][bookAttributes]
            }

            currentBook += "</dd>\n";
        }


        //close div for book
        currentBook +=
            "</dl>" +
            "<small class='d-block text-right mt-3 border-bottom border-gray pb-2'>\n" +

            "<button class='btn btn-outline-primary my-2 my-sm-0' data-toggle='modal' data-target='#queueList' onclick='showQueue(" +
            books_json[book]["id"] +
            ")'>Show Queue" +
            "</button>\n" +

            "<button class='btn btn-outline-primary my-2 my-sm-0' data-toggle='modal' data-target='#myModal' onclick='setCurrentBook(" +
            books_json[book]["id"] +
            ")'>Modify" +
            "</button>\n";

        if (title === 'Control Documents3') {
            currentBook += "<button class='btn btn-outline-danger my-2 my-sm-0' onclick='deleteBook(" +
                books_json[book]["id"] +
                ")' type='submit'>Delete" +
                "</button>\n"
        }
        currentBook +=
            "</small>\n" +
            "</div>\n";

        updatedBooks += currentBook;
    }

    //close listOfBooks block
    listOfBooks +=
        " </div>\n" +
        "</div>\n";

    //close UsersBlock
    updatedBooks +=
        "</div>\n" +
        "</div>\n";


    let updateButton = "<small class=\"d-block text-right mt-3\">\n" +
        "<button class=\"btn btn-outline-primary my-2 my-sm-0\" type=\"button\" onclick=\"updateBooks()\">Update</button>\n" +
        "</small>\n";

    outer += listOfBooks;
    outer += updatedBooks;
    outer += "</div>\n";
    outer += updateButton;

    //Final load in html. It replace everything inside <div id = "database'> which is container for our database.
    $("#database").html(outer);
}

function modifyCurrentBook() {

    let title = $("#titleModify").val();
    let edition = $("#editionModify").val();
    let editor = $("#editorModify").val();
    let publisher = $("#publisherModify").val();
    let publishingDate = $("#publishingDateModify").val();
    let price = $("#priceModify").val();
    let count = $("#countModify").val();
    let isBestseller = $("#isBestsellerModify").val();
    let isReference = $("#isReferenceModify").val();
    let type = $("#typeModify").val();
    let authors = $("#authorsModify").val().trim().split(',');
    let tags = $("#tagsModify").val().trim().split(',');

    let jsonAuthors = [];

    for (let index in authors) {
        let tmp = authors[index].split(' ');
        jsonAuthors.push({
            "firstName": (tmp[0].trim()),
            "lastName": (tmp[1].trim())
        });
    }

    publishingDate = Date.parse(publishingDate);
    console.log(publishingDate);
    let jsonData = JSON.stringify({
        "title": title,
        "edition": edition,
        "editor": editor,
        "publisherName": publisher,
        "price": price,
        "count": count,
        "bestseller": isBestseller,
        "reference": isReference,
        "publishingDate": publishingDate,
        "type": type,
        "authors": jsonAuthors,
        "tags": tags
    });

    console.info(jsonData);
    $.ajax({
        url: URL_LOCALHOST + "/document/update",
        type: "PUT",
        headers: {
            'Authorization': window.localStorage.getItem("Authorization"),
            'Content-Type': "application/json"
        },
        contentType: "application/json",
        data: jsonData,
        success: function (data, status, xhr) {
            updateUsers();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("Fail book modifying");
            console.error(jqXHR);
            console.error(textStatus);
            console.error(errorThrown);
        }
    });

}

function addBook() {
    let title = $("#title").val();
    let edition = $("#edition").val();
    let editor = $("#editor").val();
    let publisher = $("#publisher").val();
    let publishingDate = $("#publishingDate").val();
    let price = $("#price").val();
    let count = $("#count").val();
    let isBestseller = $("#isBestseller").val();
    let isReference = $("#isReference").val();
    let type = $("#type").val();
    let authors = $("#authors").val().trim().split(',');
    let tags = $("#tags").val().trim().split(',');

    let jsonAuthors = [];

    for (let index in authors) {
        let tmp = authors[index].split(' ');
        jsonAuthors.push({
            "firstName": (tmp[0].trim()),
            "lastName": (tmp[1].trim())
        });
    }

    publishingDate = Date.parse(publishingDate);

    let jsonData = JSON.stringify({
        "title": title,
        "edition": edition,
        "editor": editor,
        "publisherName": publisher,
        "price": price,
        "count": count,
        "bestseller": isBestseller,
        "reference": isReference,
        "publishingDate": publishingDate,
        "type": type,
        "authors": jsonAuthors,
        "tags": tags
    });

    console.log(jsonData);

    $.ajax({
        url: URL_LOCALHOST + "/document/add",
        type: "POST",
        headers: {
            'Authorization': window.localStorage.getItem("Authorization"),
            'Content-Type': "application/json"
        },
        contentType: "application/json; charset=utf-8",
        data: jsonData,
        success: function (data, status, xhr) {
            updateBooks();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("ne ura");
            console.error(jqXHR);
            console.error(textStatus);
            console.error(errorThrown);
        }
    });


}

function deleteBook(id) {

    $.ajax({
        url: URL_LOCALHOST + "/document/remove?id=" + id,
        type: "DELETE",
        headers: {
            'Authorization': window.localStorage.getItem("Authorization"),
        },
        success: function (books_json, status, xhr) {
            updateBooks();
        },
        error: function (books_json, status, xhr) {
            alert("ne ura");
            console.error(status);
            console.error(xhr);
        }
    });
}

function fillInputsInBookModify() {
    console.log("attributes begin");
    for (let index in bookAttributesWeHave) {
        if (bookAttributesWeHave[index] === "id")
            continue;

        console.log(bookAttributesWeHave[index]);

        let toReplace = "#" + map.get(bookAttributesWeHave[index]) + "Modify";
        let replaceWith = $("#" + bookAttributesWeHave[index] + currentBookId + 'd')[0].innerHTML;
        $(toReplace).val(replaceWith);
    }
    console.log("attributes end");
}

var map;

function init() {
    map = new Map();


    map.set("title", "title");
    map.set("edition", "edition");
    map.set("editor", "editor");
    map.set("publisher", "publisher");
    map.set("price", "price");
    map.set("count", "count");
    map.set("publishingDate", "publishingDate");
    map.set("bestseller", "isBestseller");
    map.set("reference", "isReference");
    map.set("type", "type");
    map.set("authors", "authors");
    map.set("tags", "tags");

}

function showQueue(id) {
    $.ajax({
        url: URL_LOCALHOST + "/booking/queue?id=" + id,
        type: "GET",
        headers: {
            'Authorization': window.localStorage.getItem("Authorization"),
        },
        dataType: "json", // by this property ajax will automatically parse json which we get from response

        success: function (queue_json, status, xhr) {
            console.info("Responding json: ");
            console.info(queue_json);

            let outer =
                "<div class=\"modal-dialog\">" +
                "<div class=\"modal-content\">" +
                "<div class=\"modal-header\">" +
                "<h4 class=\"modal-title\">Queue for current book</h4>" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"modal\">×</button>"
                + "</div>";


            outer += "<div class=\"row mt-2 \">";

            //this list is not array, but list of <a>
            let listOfQueue =
                "<div class=\"col-4\">\n" +
                "<div class=\"list-group\" id=\"new-list-tab\" role=\"tablist\">\n";

            let updatedQueue = "<div class=\"col-8\">\n" +
                "<div class=\"tab-content\" id=\"new-nav-tabContent\">\n";

            let notFirst = false;
            for (let queue in queue_json) {
                //add <a> (link to particular queue) for listOfQueue


                let currentQueue;

                if (notFirst) {
                    currentQueue =
                        "<div class='tab-pane fade' id=\'" + queue_json[queue]["id"] +
                        "\' role='tabpanel' aria-labelledby='list" + queue_json[queue]["id"] + "\'>\n";

                    listOfQueue +=
                        "<a class=\"list-group-item list-group-item-action\" id=\"list" + queue_json[queue]["id"] +
                        "\" data-toggle=\"list\"\n" +
                        "href=\"#" + queue_json[queue]["id"] +
                        "\" role=\"tab\" aria-controls=\"list" + queue_json[queue]["id"] + "\">" +
                        queue_json[queue]["document"]["title"] + "</a>";
                }
                else {
                    currentQueue =
                        "<div class='tab-pane fade show active' id=\'" + queue_json[queue]["id"] +
                        "\' role='tabpanel' aria-labelledby='list" + queue_json[queue]["id"] + "\'>\n";


                    listOfQueue += "<a class=\"list-group-item list-group-item-action active show\" id=\"list" + queue_json[queue]["id"] +
                        "\" data-toggle=\"list\"\n" +
                        "href=\"#" + queue_json[queue]["id"] +
                        "\" role=\"tab\" aria-controls=\"list" + queue_json[queue]["id"] + "\">" +
                        queue_json[queue]["document"]["title"] + "</a>";

                    notFirst = true;
                }


                //add all attributes
                for (let queueAttributes in queue_json[queue]) {
                    currentQueue += "<dt>" + queueAttributes + "</dt>\n";
                    currentQueue += "<dd>";

                    if (queueAttributes === "document") {
                        let temp = queue_json[queue]["document"]["title"];
                        currentQueue += temp;
                        currentQueue = currentQueue.replace("[object Object]", "");
                    }
                    if (queueAttributes === "typeBooking") {
                        let temp = queue_json[queue]["typeBooking"]["typeName"];
                        currentQueue += temp;
                        currentQueue = currentQueue.replace("[object Object]", "");
                    }
                    if (queueAttributes === "user") {
                        let temp = queue_json[queue]["user"]["name"] + " " + queue_json[queue]["user"]["surname"];
                        currentQueue += temp;
                        currentQueue = currentQueue.replace("[object Object]", "");

                    }

                    if (queueAttributes === "returnDate") {
                        let date = new Date(queue_json[queue]["returnDate"]);
                        let day = date.getDate();
                        if (day.toString().length == 1)
                            day = "0" + day;
                        let month = date.getMonth();
                        if (month.toString().length == 1)
                            month = "0" + month;
                        let year = date.getFullYear();
                        currentQueue += year + "-" + month + "-" + day;
                        currentQueue = currentQueue.replace("[object Object]", "");
                    }

                    else {
                        currentQueue += queue_json[queue][queueAttributes];
                        currentQueue = currentQueue.replace("[object Object]", "");
                    }
                    currentQueue = currentQueue.replace("[object Object]", "");
                    currentQueue += "</dd>\n";
                }

                //close div for queue
                currentQueue += "</dl>" +
                    "<small class=\"d-block text-right mt-3\">\n" +
                    "<button class=\"btn btn-outline-danger mb-3 my-2 my-sm-0\" type=\"button\" onclick=\"makeOutstanding(" + queue_json[queue]["id"] + "," + id + ")\">Make Outstanding</button>\n" +
                    "</small>\n" +
                    "</div>\n";


                updatedQueue += currentQueue;

            }

            //close listOfQueue block
            listOfQueue +=
                " </div>\n" +
                "</div>\n";

            //close UsersBlock
            updatedQueue +=
                "</div>\n" +
                "</div>\n";

            outer += listOfQueue;
            outer += updatedQueue;
            outer += "</div>\n";
            outer += "</div>\n";
            outer += "</div>\n";

            //Final load in html. It replace everything inside <div id = "database'> which is container for our database.
            $("#queueList").html(outer);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("failed in getting requests");

            console.error(jqXHR);
            console.error(textStatus);
            console.error(errorThrown);
        }
    });
}

function makeOutstanding(id, idd) {
    $.ajax({
        url: URL_LOCALHOST + "/booking/outstanding?id=" + id,
        type: "PUT",
        headers: {
            'Authorization': window.localStorage.getItem("Authorization"),
        },
        success: function (requests_json, status, xhr) {
            showQueue(idd);
        },
        error: function (requests_json, status, xhr) {
            alert("Failed");
            console.error(status);
            console.error(xhr);
        }
    });
}


