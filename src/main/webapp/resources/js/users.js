jQuery(document).ready(function () {
    $(".set_friend").ajaxForm(function (data) {
        location.reload();
    });

    $(".set_itineraries").ajaxForm(function (data) {
        location.reload();
    });

    $("#addPerson").ajaxForm(function (data) {
        location.reload();
    });

    $('body').on("click", ".add_point_submit", function () {
        var add_point = $(this).parent();
        $.ajax({
            url: "/addPoint",
            type: "POST",
            data: {"userRoutId" : getUrlVars()["userRoutId"],
                    "X" : add_point.find("input[name='x']").val(),
                    "Y" : add_point.find("input[name='y']").val()},
            success: function (data) {
                location.reload();
            },
            error: function () {
                alert("#ERROR");
            }
        });
    });

});

//получение значения параметров из адресной строки
function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (m, key, value) {
        vars[key] = value;
    });
    return vars;
}