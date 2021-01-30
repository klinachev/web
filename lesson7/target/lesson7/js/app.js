window.notify = function (message) {
    $.notify(message, {
        position: "right bottom",
        className: "success"
    });
};

window.ajax = function (data, successFunc) {
    $.ajax({
        dataType: "json",
        type: "POST",
        data: data,

        success: function(response) {
            successFunc(response);
            if (response["redirect"]) {
                location.href = response["redirect"];
            }
        }
    });
};
