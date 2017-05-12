/**
 * Created by Alex on 5/8/2017.
 */

var userData;
var userRoleData;
var numberOfAdded = 0;
var selected = -1;
function registerByAdmin() {
    var form = $("#registration-form1");
    var firstName = form.find('input[name="firstName"]').val();
    var lastName = form.find('input[name="lastName"]').val();
    var email = form.find('input[name="email"]').val();
    var password = form.find('input[name="password"]').val();
    var phone = form.find('input[name="phone"]').val();
    var address = form.find('input[name="address"]').val();
    var aptNumber = form.find('input[name="aptNumber"]').val();
    var roles = [];
    $("#roles-values-register").find(".product-characteristic-input").each(function (element) {
        roles.push({
            roleId: $(this).find("select").val(),
            roleName: userRoleData[$(this).find("select").val()],
        });


    });
    var _csrf = $('meta[name=_csrf]').attr("content");

    $.ajax({
        type: 'POST',
        url: '/api/admin/users/register',
        headers: {

            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        processData: false,
        contentType: 'application/json',
        data: JSON.stringify({
            firstName: firstName,
            lastName: lastName,
            email: email,
            password: password,
            phoneNumber: phone,
            address: address,
            aptNumber: aptNumber,
            roles: roles
        }),
        success: function (data) {
            var alert;
            if (data.status == 'success') {
                console.log("Registration success! " + JSON.stringify(data));
                alert = $('<div id="registration-header-alert1" class="alert alert-success" role="alert">' +
                    "User registered successfully" + "</div>");
            } else {
                console.log("Registration error! " + JSON.stringify(data));
                alert = $('<div id="registration-header-alert1" class="alert alert-danger" role="alert">' +
                    data.message + '</div>');
            }

            $("#registration-header-alert1").replaceWith(alert);

        },
        error: function (data) {
            console.error("Registration error! " + JSON.stringify(data));
            var alert = $('<div id="registration-header-alert1" class="alert alert-danger" role="alert">' +
                "Registration failed</div>");
            $("#registration-header-alert1").replaceWith(alert);
        }
    });

}

function getUser() {

    var form = $("#userSearch");
    var userDataForm = $("#userDataForm");
    var email = form.find('input[name="email"]').val();
    var _csrf = $('meta[name=_csrf]').attr("content");

    var node = $("#roles-values");
    var domainNode =$("#list1");

    clearFields();


    $.ajax({
            type: 'GET',
            url: '/api/admin/users/get',
            headers: {

                'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
            },

            data: ({name: email}),
            dataType: 'json',

            success: function (data) {
                userData = data;

                console.log("User found! " + data.email);

                $("#mf1").val(data.firstName);
                $("#mf2").val(userData.lastName);
                $("#mf3").val(userData.email);
                $("#mf4").val(userData.password);
                $("#mf5").val(userData.address);
                $("#mf6").val(userData.aptNumber);
                $("#mf7").val(userData.phoneNumber);


                for (var characteristic in userData.roles) {
                    console.log("adding property " + characteristic);
                    addUserRole(
                        node,
                        userData.roles[characteristic].roleId,
                        userData.roles[characteristic].roleName,
                        userData.roles[characteristic].roleId
                    );
                }
                for (var characteristic in userData.domains) {
                    console.log("adding domains " + characteristic);
                    addUserDomain(
                        domainNode,
                        userData.domains[characteristic].domainId,
                        userData.domains[characteristic].domainName

                    );
                }
                var alert = $('<div id="search-alert" class="alert alert-success" role="alert">User found</div>');
                $("#search-alert").replaceWith(alert);
            },
            error: function (data) {
                console.error("User does not exists! ");

                var alert1 = $('<div id="search-alert" class="alert alert-danger" role="alert">User does not exists</div>');
                $("#search-alert").replaceWith(alert1);

            }

        }
    );



}
function saveUser() {
    var userData1 = {};
    userData1.roles = [];
    userData1.userId = userData.userId;
    var tmp = [];

    userData1.firstName = $("#mf1").val();
    userData1.lastName = $("#mf2").val();
    userData1.email = $("#mf3").val();

    if (userData.password != $("#mf4").val()) {
        userData1.password = $("#mf4").val();
    }
    else {
        userData1.password = null;
    }
    userData1.address = $("#mf5").val();
    userData1.aptNumber = $("#mf6").val();
    userData1.phoneNumber = $("#mf7").val();

    var _csrf = $('meta[name=_csrf]').attr("content");

    $("#roles-values").find(".product-characteristic-input").each(function (element) {
        userData1.roles.push({

            roleId: $(this).find("select").val(),
            roleName: userRoleData[$(this).find("select").val()],

        });

        tmp.push($(this).find("select").val())
    });

    $.ajax({
        type: 'POST',
        url: '/api/admin/users/update',
        headers: {

            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        processData: false,
        contentType: 'application/json',
        data: JSON.stringify(userData1),
        success: function (data) {
            var alert;
            if (data.status == 'success') {
                console.log("Update success! " + JSON.stringify(tmp));
                alert = $('<div id="update-alert" class="alert alert-success" role="alert">' +
                    "User " + userData1.email+" updated!" + "</div>");
            } else {
                console.log("Update error! " + JSON.stringify(data));
                alert = $('<div id="update-alert" class="alert alert-danger" role="alert">' +
                    data.message + '</div>');
            }
            clearFields();

            $("#update-alert").replaceWith(alert);
        },
        error: function (data) {
            console.error("Update error! " + JSON.stringify(data));
            var alert = $('<div id="update-alert" class="alert alert-danger" role="alert">' +
                "Update failed</div>");
            $("#update-alert").replaceWith(alert);
        }
    });
    userData = {};


}
function clearFields() {

    $("#roles-values").empty();
    $("#searchAlert").empty();
    $("#list1").empty();
    $("#update-alert").empty();
    $("#mf1").val("");
    $("#mf2").val("");
    $("#mf3").val("");
    $("#mf4").val("");
    $("#mf5").val("");
    $("#mf6").val("");
    $("#mf7").val("");

    $("#domain-editor").addClass("hidden");


}


function addUserRole(node,id, name, dataType) {

    var options = "";

    for (var dataTypeId in userRoleData) {
        if (userRoleData.hasOwnProperty(dataTypeId)) {
            options += '<option value="' + dataTypeId + '"';

            if (dataTypeId == dataType) {
                options += " selected ";
            }
            options += '>' + userRoleData[dataTypeId] + '</option>';
        }
    }

    if (name == undefined) name = "";

    if (dataType == undefined) dataType = "";
    if (id == undefined) id = -1;

    var html =
        '<div class="input-group product-characteristic-input">' +
        '<input type="hidden" name="characteristic-id" value="' + id + '"/>' +
        '<span class="input-group-addon">Role</span>' +
        '<select class="form-control" name="characteristic-dataTypeId">' +
        options +
        '</select>' +
        '<span class="input-group-addon" style="background-color: #d9534f; cursor: pointer" onclick="removeRole(this)">' +
        '<span class="glyphicon glyphicon-remove bg-danger" style="color: white; background-color: #d9534f; cursor: pointer"></span>' +
        '</span>' +
        '</div>';

    //$("#roles-values").append($(html));

    node.append($(html));
}
function addUserDomain(node,id, name, city) {

    if (name == undefined) name = "";
    if (id == undefined) id = -1;
    var html =
        '<a href="#" class="list-group-item"'+' name="'+id+'"'+  ' onclick="displayDomain(this)">'+name+ '<input type="checkbox" class="pull-right"></a>';

    node.append($(html));


}
function displayDomain(element) {
    $("#domain-editor").removeClass("hidden");
    for (var characteristic in userData.domains) {

        if(userData.domains[characteristic].domainId == element.name){
            var address = userData.domains[characteristic].address;

            console.log("YEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEYYYYYYYYYYY");
            $("#domain-id-input").val(userData.domains[characteristic].domainId);
            $("#domain-name-input").val(userData.domains[characteristic].domainName);
            $("#domain-type-input").val(userData.domains[characteristic].domainType.categoryName);
            $("#domain-region-input").val(userData.domains[characteristic].regionId);
            $("#domain-address-input").val(address.city +", "+ address.street+", "+address.building );
            $("#domain-address-apt-input").val(address.apartment);


        }
    }



}




function addUserRoleRegister() {
    var nodeRegister = $("#roles-values-register");
    addUserRole(nodeRegister);

}
function addUserRoleModify() {
    var nodeModify = $("#roles-values");
    addUserRole(nodeModify);

}

function loadInfo() {
    $.ajax({
        url: "/api/admin/users/userRoles",
        success: function (data) {
            userRoleData = data;
            // loadProductTypes();
        },
        error: function () {
            console.error("Cannot load dataTypes");
        }
    });
}
function removeRole(element) {
    console.log(element);
    element.parentNode.parentNode.removeChild(element.parentNode)
}

$(document).ready(function () {
    loadInfo();
});