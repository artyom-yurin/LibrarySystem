$(window).on("load", function () {
        //I am lazy enough
        $('#' + 'linkUsers').attr("href", "#");
        $('#' + 'linkCatalog').attr("href", "#");
        $('#' + 'linkMyBooks').attr("href", "#");
        $('#' + 'linkLogin').attr("href", "#");
        $('#' + 'linkRequest').attr("href", "#");
        $('#' + 'linkBooks').attr("href", "#");
        $('#' + 'linkConfirmation').attr("href", "#");
        $('#' + 'linkNotifications').attr("href", "#");
        $('#' + 'linkLoging').attr("href", "#");
        $('#' + 'linkLibrarians').attr("href", "#");
    }
);

$('#' + 'linkUsers').on("click", function () {
    sendToken(linkToUsers);
});
$('#' + 'linkLibrarians').on("click", function () {
    sendToken(linkToLibrarians);
});
$('#' + 'linkLoging').on("click", function () {
    sendToken(linkToLoging);
});
$('#' + 'linkRequest').on("click", function () {
    sendToken(linkToRequest);
});
$('#' + 'linkConfirmation').on("click", function () {
    sendToken(linkToConfirmation);
});
$('#' + 'linkNotifications').on("click", function () {
    sendToken(linkToNotification);
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
    console.log("All keys successfully deleted from localStorage");

    window.location.href = URL_LOCALHOST;
});

function sendToken(path) {
    $.ajax({
            url: path,
            type: "GET",
            headers: {
                'Authorization': window.localStorage.getItem("Authorization")
            },
            async: false,
            success: function (html_data, status, xhr) {
                window.localStorage.setItem('page', html_data);

                document.open();
                document.write(html_data);
                document.close();
            },

            error: function (html_data, status, xhr) {
                alert("Fail on token post-request");
                console.error('Error_status: \n' + status);
                console.error('Error_xhr: \n' + xhr);
            }
        }
    );
}


