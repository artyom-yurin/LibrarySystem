$(window).on("load", function () {
        //I am lazy enough
        $('#' + 'linkUsers').attr("href", "#");
        $('#' + 'linkCatalog').attr("href", "#");
        $('#' + 'linkMyBooks').attr("href", "#");
        $('#' + 'linkLogin').attr("href", "#");
        $('#' + 'linkRequest').attr("href", "#");
        $('#' + 'linkBooks').attr("href", "#");
        $('#' + 'linkToConfirmation').attr("href", "#");
    }
);

$('#' + 'linkUsers').on("click", function () {
    sendToken(linkToUsers);
});
$('#' + 'linkRequest').on("click", function () {
    sendToken(linkToRequest);
});
$('#' + 'linkConfirmation').on("click", function () {
    sendToken(linkToConfirmation);
});
$('#' + 'linkBooks').on("click", function () {
    sendToken(linkToBooks);
});

$('#' + 'linkCatalog').on("click", function () {
    sendToken(linkToCatalog);
});
$('#' + 'linkMyBooks').on("click", function () {
    sendToken(linkToMyBooks);
});

$('#' + 'linkLogin').on("click", function () {
    window.localStorage.clear();
    alert("All keys successfully deleted from localStorage");
    window.location.href = URL_LOCALHOST;
});

function sendToken(path) {
    $.ajax({
            url: path,
            type: "GET",
            headers: {
                'Authorization': window.localStorage.getItem("Authorization")
            },
            //async:false,
            success: function (html_data, status, xhr) {
                document.open();
                document.write(html_data);
                document.close();
            },

            error: function (html_data, status, xhr) {
                alert("Fail on token post-request");
                console.info("Token sent was : \n");
                console.info(window.localStorage.getItem("Authorization"));
                alert('Error_data: \n' + html_data.responseJSON + "\n See details(responseJSON) in console");
                console.error(html_data.responseJSON);
                console.error('Error_status: \n' + status);
                console.error('Error_xhr: \n' + xhr);
            }
        }
    );
}
