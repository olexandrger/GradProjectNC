function saveChange() {
    var user = {};
    user.firstName = $("#mf1").val();
    user.lastName = $("#mf2").val();
    user.email = $("#mf3").val();
    user.phoneNumber = $("#mf4").val();
    $.ajax({
        type: 'POST',
        url: '/api/profile/update',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        processData: false,
        contentType: 'application/json',
        data: JSON.stringify(user),
        success: function (data) {
            var alert;
            if (data.status == 'success') {
                console.log("Update success! " + JSON.stringify(data));
                alert = $('<div id="update-profile-alert" class="alert alert-success" role="alert">' +
                    data.message + "</div>");
                var account = $('#navbar-account-button');
                account.text("Welcome, " + user.firstName + " " + user.lastName + "!");
            } else {
                console.log("Error! " + JSON.stringify(data));
                alert = $('<div id="update-profile-alert" class="alert alert-danger" role="alert">' +
                    data.message + '</div>');
            }
            $("#update-profile-alert").replaceWith(alert);
        },
        error: function (data) {
            console.error("Error" + JSON.stringify(data));
            var alert = $('<div id="update-profile-alert" class="alert alert-danger" role="alert">' +
                "Error</div>");
            $("#update-profile-alert").replaceWith(alert);
        }
    });
}

function changePassword() {
    var currentPassword = $("#currentPassword").val();
    var newPassword = $("#newPassword").val();
    $.ajax({
        type: 'POST',
        url: '/api/profile/changePassword',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        data: {
            currentPassword : currentPassword,
            newPassword : newPassword
        },
        success: function (data) {
            var alert;
            if (data.status == 'success') {
                console.log("Update success! " + JSON.stringify(data));
                alert = $('<div id="change-password-alert" class="alert alert-success" role="alert">' +
                    data.message + "</div>");
            } else {
                console.log("Error! " + JSON.stringify(data));
                alert = $('<div id="change-password-alert" class="alert alert-danger" role="alert">' +
                    data.message + '</div>');
            }
            $("#change-password-alert").replaceWith(alert);
        },
        error: function (data) {
            console.error("Error" + JSON.stringify(data));
            var alert = $('<div id="change-password-alert" class="alert alert-danger" role="alert">' +
                "Error </div>");
            $("#change-password-alert").replaceWith(alert);
        }
    });
}

function loadInfo() {
    $.ajax({
            url: '/api/profile/get',
            success: function (data) {
                showInfo(data);
            },
            error: function (data) {
                showInfo(data);
            }
        }
    );
}

function showInfo(data) {
    var showDomainButton = false;
    for (var i = 0; i < data.roles.length; i++) {
        var role = data.roles[i].roleName
        if (role === "ROLE_CLIENT") {
            showDomainButton = true;
            break;
        }
    }
    if (showDomainButton) {
        $('#changeDomain').show();
    }
    $("#mf1").val(data.firstName);
    $("#mf2").val(data.lastName);
    $("#mf3").val(data.email);
    $("#mf4").val(data.phoneNumber);
}


$(document).ready(function () {
    loadInfo();
});