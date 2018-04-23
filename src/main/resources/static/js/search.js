function search() {

    let searchType = $("#searchType").val();
    let searchQuery = $("#search").val();



    let jsonData = JSON.stringify({
        "searchType": searchType,
        "searchQuery": searchQuery
    });

    $.ajax({
        url: URL_LOCALHOST + "/search",
        type: "GET",
        headers: {
            'Authorization': window.localStorage.getItem("Authorization"),
            'Content-Type': "application/json"
        },
        dataType: "json", // by this property ajax will automatically parse json which we get from response
        data: jsonData,
        success: function (books_json, status, xhr) {
            console.info("Responding json: ");
            console.info(books_json);

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
                    pushNewBookAttribute(bookAttributes)

                    if (bookAttributes === "publishingDate") {
                        currentBook += "<dt>Publishing Date</dt>\n";
                    }
                    else{
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
                        if(day.toString().length == 1)
                            day = "0" + day;
                        let month = date.getMonth();
                        if(month.toString().length == 1)
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
                // currentBook +=
                //     "</dl>" +
                //     "<small class='d-block text-right mt-3 border-bottom border-gray pb-2'>\n" +
                //
                //     "<button class='btn btn-outline-primary my-2 my-sm-0' data-toggle='modal' data-target='#queueList' onclick='showQueue(" +
                //     books_json[book]["id"] +
                //     ")'>Show Queue" +
                //     "</button>\n" +
                //
                //     "<button class='btn btn-outline-primary my-2 my-sm-0' data-toggle='modal' data-target='#myModal' onclick='setCurrentBook(" +
                //     books_json[book]["id"] +
                //     ")'>Modify" +
                //     "</button>\n" +
                //
                //     "<button class='btn btn-outline-danger my-2 my-sm-0' onclick='deleteBook(" +
                //     books_json[book]["id"] +
                //     ")' type='submit'>Delete" +
                //     "</button>\n" +
                //
                //     "</small>\n" +
                //     "</div>\n";

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
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("failed in getting books");

            console.error(jqXHR);
            console.error(textStatus);
            console.error(errorThrown);
        }
    });
}