$(updateBooks());

function updateBooks() {
    $.ajax({
        url: URL_LOCALHOST + "/document/documents",
        type: "GET",
        headers: {
            'Authorization': window.localStorage.getItem("Authorization"),
        },
        dataType: "json", // by this property ajax will automatically parse json which we get from response

        success: function (books_json, status, xhr) {
            console.info("Responding json: ");
            console.info(books_json);

            let outer =
                "<h4 class=\"border-bottom border-gray pb-2 mb-0\">Books database</h4>\n" +
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
                    currentBook += "<dt>" + bookAttributes + "</dt>\n";
                    currentBook += "<dd>";

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
                        currentBook = currentBook.substring(0, currentBook.length - 1);
                        currentBook += "<dt>";
                        currentBook += listOfAuthors;
                    }

                    else if (bookAttributes === "publishingDate") {
                        let date = new Date(books_json[book]["publishingDate"]);
                        currentBook += date.toString();
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

                alert(books_json[book]["id"]);
                //close div for book
                currentBook +=
                    "</dl>" +
                    "<small class='d-block text-right mt-3 border-bottom border-gray pb-2'>\n" +
                    "<button class='btn btn-outline-danger my-2 my-sm-0' onclick='deleteBook(" +
                    books_json[book]["id"] +
                    ")' " +
                    "type='submit'>Delete" +
                    "</button>\n" +
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
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("failed in getting books");

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
    let price = $("#price").val();
    let isBestseller = $("#isBestseller").val();
    let isReference = $("#isReference").val();
    let type = $("#type").val();
    let authors = $("#authors").val().split(",");

    for(let author in authors){
        console.log(author);
    }

    let jsonData = JSON.stringify({
        'title': title,
        'edition': edition,
        'editor': editor,
        'publisher': publisher,
        'price': price,
        'isBestSeller': isBestseller,
        'isReference': isReference,
        'type': type

    });

    {
        $.ajax({
            url: URL_LOCALHOST + "/document/add",
            type: "POST", //onUpdate use put
            headers: {
                'Authorization': window.localStorage.getItem("Authorization"),
                'Content-Type': "application/json"
            },
            contentType: "application/json; charset=utf-8",
            data: jsonData,
            success: function (data, status, xhr) {
                alert("ura");
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
}

function deleteBook(id) {
    alert(id);
    $.ajax({
        url: URL_LOCALHOST + "/document/remove?id=" + id,
        type: "DELETE",
        headers: {
            'Authorization': window.localStorage.getItem("Authorization"),
        },
        success: function (users, status, xhr) {
            updateBooks();
            alert("Book deleted");
        },
        error: function (users, status, xhr) {
            alert("ne ura");
            console.error(status);
            console.error(xhr);
        }
    });
}