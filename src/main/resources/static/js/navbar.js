function displayAnonymousNavigationBar() {
    $("#navbar-login-button").removeClass("hidden");
    $("#navbar-registration-button").removeClass("hidden");
    $("#navbar-account-button").addClass("hidden");
}

function displayAuthenticatedNavigationBar(data) {

    var account = $('#navbar-account-button');
    account.text("Welcome, " + data.name + "!");
    account.append($("<span class='caret'></span>"));

    var list = $("#navbar-account").find("ul");
    list.empty();

    data.profileLinks.forEach(function(item) {

            console.log("Adding " + item.name);

            var ref = document.createElement("a");
            ref.appendChild(document.createTextNode(item.name));
            ref.href = item.reference;
            /*
            ref.onclick = function () {
                selectRegion(item);
            };
*/
            var li = document.createElement("li");
            li.appendChild(ref);
            list.append(li);
    });

    console.log("Adding logout");
    var ref = document.createElement("a");
    ref.appendChild(document.createTextNode("Logout"));
    ref.onclick = logout;
    var li = document.createElement("li");
    li.appendChild(ref);
    list.append(li);

    account.removeClass("hidden");
    $("#navbar-login-button").addClass("hidden");
    $("#navbar-registration-button").addClass("hidden");
}

function getAccountInformation() {
    var _csrf = $('meta[name=_csrf]').attr("content");

    $.ajax({
        type: 'GET',
        url: "/api/user/account",
        headers: {
            'X-CSRF-TOKEN': _csrf
        },
        success: function (data) {
            console.log("Account information successfully fetched! " + JSON.stringify(data));
            if (data.authenticated == "true") {
                displayAuthenticatedNavigationBar(data);
            }  else {
                displayAnonymousNavigationBar();
            }
        },
        error: function(data) {
            console.error("Fetching information abound account failed! " + JSON.stringify(data));
        }
    });
}

function logout() {
    //TODO if user is not on allowed page, redirect him to main page

    console.log("Logout");
    var _csrf = $('meta[name=_csrf]').attr("content");
    $.ajax({
        type: 'POST',
        url: '/logout',
        headers: {
            'X-CSRF-TOKEN': _csrf
        },
        success: function (data) {
            console.log("Logout success! " + JSON.stringify(data));
            location.reload();
        },
        error: function (data) {
            console.error("Logout failure! " + JSON.stringify(data));
        }
    });
}

function login() {
    var form = $("#login-form");
    var email = form.find('input[name="email"]').val();
    var password = form.find('input[name="password"]').val();

    var _csrf = $('meta[name=_csrf]').attr("content");

    $.ajax({
        type: 'POST',
        url: '/login',
        headers: {
            'X-CSRF-TOKEN': _csrf
        },
        data: {
            email: email,
            password: password
        },
        success: function (data) {
            console.log("Login success! " + JSON.stringify(data));
            if (data.status == "success") {
                $('#login-modal').modal('toggle');
                location.reload();
            } else {
                var alert = $('<div id="login-header-alert" class="alert alert-danger" role="alert">' +
                    "Login failed</div>");
                $("#login-header-alert").replaceWith(alert);
            }
        },
        error: function (data) {
            console.error("Login failure! " + JSON.stringify(data));
        }
    });
}

function register() {
    var form = $("#registration-form");
    var firstName = form.find('input[name="firstName"]').val();
    var lastName = form.find('input[name="lastName"]').val();
    var email = form.find('input[name="email"]').val();
    var password = form.find('input[name="password"]').val();
    var repeatPassword = form.find('input[name="repeatPassword"]').val();
    var phone = form.find('input[name="phone"]').val();

    var _csrf = $('meta[name=_csrf]').attr("content");

    $.ajax({
        type: 'POST',
        url: '/register',
        headers: {
            'X-CSRF-TOKEN': _csrf
        },
        processData: false,
        contentType: 'application/json',
        data: JSON.stringify({
            firstName: firstName,
            lastName: lastName,
            email: email,
            password: password,
            phone: phone
        }),
        success: function (data) {
            var alert;
            if (data.status == 'success') {
                console.log("Registration success! " + JSON.stringify(data));
                alert = $('<div id="registration-header-alert" class="alert alert-success" role="alert">' +
                    data.message + "</div>");
            } else {
                console.log("Registration error! " + JSON.stringify(data));
                alert = $('<div id="registration-header-alert" class="alert alert-danger" role="alert">' +
                    data.message + '</div>');
            }

            $("#registration-header-alert").replaceWith(alert);
        },
        error: function (data) {
            console.error("Registration error! " + JSON.stringify(data));
            var alert = $('<div id="registration-header-alert" class="alert alert-danger" role="alert">' +
                "Registration failed</div>");
            $("#registration-header-alert").replaceWith(alert);
        }
    });
}

function selectRegion(regionName) {
    var selectedRegion = $('#region-selected');
    selectedRegion.text(regionName);
    selectedRegion.append($("<span class='caret'></span>"));
    localStorage.setItem("region", regionName);
}

function loadRegions() {
    $.ajax({
        url: "/api/user/regions/all",
        success: function(data) {

            var list = $("#region-select").find("ul");

            data.forEach(function(item) {
                console.log(item + " loaded");

                var ref = document.createElement("a");
                ref.appendChild(document.createTextNode(item));
                ref.onclick = function () {
                    selectRegion(item);
                };

                var li = document.createElement("li");
                li.appendChild(ref);
                list.append(li);
            });

            var lastChosen = localStorage.getItem("region");
            if (data.indexOf(lastChosen) < 0) {
                console.log("No saved region");
                lastChosen = data[0];
            }
            console.log("Choosing " + lastChosen);
            selectRegion(lastChosen);
        },
        error: function () {
            console.error("Cannot load list of regions");
        }
    });
}

$(document).ready(function () {
        getAccountInformation();
        loadRegions();
    }
);