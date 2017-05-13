/**
 * Created by Alex on 5/8/2017.
 */

var userData;
var userRoleData;
var numberOfAdded = 0;
var selected = -1;
function registerByAdmin() {
    var form = $("#registration-form1");
    var form2 = $("#product-type-values2");
    var firstName = form.find('input[name="firstName"]').val();
    var lastName = form.find('input[name="lastName"]').val();
    var email = form.find('input[name="email"]').val();
    var password = form.find('input[name="password"]').val();
    var phone = form.find('input[name="phone"]').val();
    var address = form2.find('input[name="domain-address-input2"]').val();
    var aptNumber = form2.find('input[name="domain-address-apt-input2"]').val();
    var domainName = form2.find('input[name="domain-name-input2"]').val();
    var domainType = form2.find('input[name="domain-type-input2"]').val();
    var roles = [];
    var domains = [];


    for (var characteristic in userRoleData) {
        console.log("adding property " + characteristic);
        if($("#adminchbx").is(':checked') && characteristic == 1){
            roles.push({roleId: characteristic, roleName: userRoleData[characteristic]});
        }
        if($("#client").is(':checked') && characteristic == 2 ){
            console.log(characteristic );
            roles.push({roleId: characteristic, roleName: userRoleData[characteristic]});
        }
        if($("#csrchbx").is(':checked') && characteristic == 3){
            console.log(characteristic );
            roles.push({roleId: characteristic, roleName: userRoleData[characteristic]});
        }
        if($("#pmgchbx").is(':checked') && characteristic == 4){
            console.log(characteristic );
            roles.push({roleId: characteristic, roleName: userRoleData[characteristic]});
        }
    }

    console.log(JSON.stringify($("#client").is(':checked')));

    //user-roles-checkboxes
    var _csrf = $('meta[name=_csrf]').attr("content");



    $.ajax({
            type: 'GET',
            url: '/api/admin/domains/addDomain?address='+address+'&aptNum='+aptNumber+'&type='+domainType+'&name='+domainName,
            headers: {

                'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
            },

            //data: (address: address,aptNum:aptNumber,type:domainType,name:domainName),
            dataType: 'json',

            success: function (data) {
                domains.push(data);
                console.log(JSON.stringify(data));


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
                        roles: roles,
                        domains: domains
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


            },
            error: function (data) {


            }

        }
    );



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

                clearFields();



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

                    if( userData.roles[characteristic].roleId == 1){
                        $("#adminM").attr('checked', true);

                    }
                    if(userData.roles[characteristic].roleId == 2 ){
                        console.log(characteristic.roleId );
                        $("#clientM").attr('checked', true);

                    }
                    if(userData.roles[characteristic].roleId == 3){

                        $("#csrM").attr('checked', true);
                        //$("#pmgM").attr('checked', true);

                    }
                    if(userData.roles[characteristic].roleId == 4){
                        console.log(characteristic );
                        $("#pmgM").attr('checked', true);

                    }
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



    for (var characteristic in userRoleData) {
        console.log();
        if($("#adminM").is(':checked') && characteristic == 1){
            userData1.roles.push({roleId: characteristic, roleName: userRoleData[characteristic]});
        }
        if($("#clientM").is(':checked') && characteristic == 2 ){
            console.log(characteristic );
            userData1.roles.push({roleId: characteristic, roleName: userRoleData[characteristic]});
        }
        if($("#csrM").is(':checked') && characteristic == 3){
            console.log(characteristic );
            userData1.roles.push({roleId: characteristic, roleName: userRoleData[characteristic]});
        }
        if($("#pmgM").is(':checked') && characteristic == 4){
            console.log(characteristic );
            userData1.roles.push({roleId: characteristic, roleName: userRoleData[characteristic]});
        }
    }

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
                alert = $('<div id="search-alert" class="alert alert-success" role="alert">' +
                    "User " + userData1.email+" updated!" + "</div>");
            } else {
                console.log("Update error! " + JSON.stringify(data));
                alert = $('<div id="search-alert" class="alert alert-danger" role="alert">' +
                    data.message + '</div>');
            }


            $("#search-alert").replaceWith(alert);
        },
        error: function (data) {
            console.error("Update error! " + JSON.stringify(data));
            var alert = $('<div id="search-alert" class="alert alert-danger" role="alert">' +
                "Update failed</div>");
            $("#search-alert").replaceWith(alert);
        }
    });
    userData = {};

    clearFields();


}
function clearFields() {

    $("#roles-values").empty();
    $("#searchAlert").empty();
    $("#list1").empty();
    //$("#update-alert").empty();
    $("#mf1").val("");
    $("#mf2").val("");
    $("#mf3").val("");
    $("#mf4").val("");
    $("#mf5").val("");
    $("#mf6").val("");
    $("#mf7").val("");


    document.getElementById("user-roles-checkboxesM").reset();

    $("#domain-editor").addClass("hidden");

    $("#adminM").removeAttr('checked');

    $("#clientM").removeAttr('checked');

    $("#csrM").removeAttr('checked');

    $("#pmgM").removeAttr('checked');



}

function addNewDomain() {

    var node =$("#list2");
    $("#domain-editor2").removeClass("hidden");


    ///var html =
      ///  '<a href="#" class="list-group-item"'+' name="'+id+'"'+  ' >'+name+ '<input type="checkbox" class="pull-right"></a>';

    ///node.append($(html));


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

            console.log(JSON.stringify(data));
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