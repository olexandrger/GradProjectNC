<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Domains</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <!--<script src="/domains.js"></script>-->
</head>
<body>

<div class="container">
<#include "../resources/navbar.ftl"/>
    <div class="row">
        <div class="row">
            <div class="col-sm-10 col-sm-offset-1">
                <div id="new-domain-alert-place">
                </div>
            </div>
        </div>
        <div class="row">
            <div class=" col-sm-3 col-sm-offset-1">
                <div class="list-group" id="domain-list"></div>
                <div class="input-group">
                    <input type="text" class="form-control" placeholder="New domain" id="new-domain-name">
                    <span class="input-group-btn">
                <button type="button" onclick="addDomain()" class="btn btn-default"><span
                        class="glyphicon glyphicon-plus"></span>Add domain</button>
            </span>
                </div>
            </div>
            <div class="col-sm-7" id="domain-editor">

                <div class="row">
                    <div class="col-sm-12" id="domain">
                        <div class="form-group">
                            <label for="domain-name">Domain name</label>
                            <input type="text" class="form-control" name="domain-name" placeholder="Name"
                                   id="domain-name-input">
                        </div>

                        <div class="form-group">
                            <label for="domain-type">Domain type</label>
                            <select class="form-control" name="domain-type-value" id="domain-type-selector"
                                    onchange="changeDomainType()">
                                <option value="PRIVATE">PRIVATE</option>
                                <option value="CORPORATIVE">CORPORATIVE</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="domain-address">Address</label>
                            <div class="input-group region-input">
                                <span class="input-group-addon">Region</span>
                                <input type="text" class="form-control" name="region" placeholder="Region"
                                       id="domain-region-input">
                            </div>
                            <div class="input-group region-input">
                                <span class="input-group-addon">City</span>
                                <input type="text" class="form-control" name="info" placeholder="City"
                                       id="domain-info-input">
                            </div>
                            <div class="input-group region-input">
                                <span class="input-group-addon">Street</span>
                                <input type="text" class="form-control" name="street" placeholder="Street"
                                       id="domain-street-input">
                            </div>
                            <div class="input-group region-input">
                                <span class="input-group-addon">Building</span>
                                <input type="text" class="form-control" name="building" placeholder="Building"
                                       id="domain-building-input">
                            </div>
                            <div class="input-group region-input">
                                <span class="input-group-addon">Apartment number</span>
                                <input type="text" class="form-control" name="apartment" placeholder="Apartment"
                                       id="domain-apartment-input">
                            </div>
                        </div>
                        <div class="form-group hidden" id="user-editor">
                            <label>Users</label>
                            <input type="text" class="form-control" name="user-email" placeholder="E-mail"
                                   id="user-email-input">
                            <a class="btn btn-default" onclick="addUser()">
                                <span class="glyphicon glyphicon-plus"></span>Add
                            </a>
                            <div class="table-responsive" id="user-table">
                                <table class="table table-striped">
                                    <thead>
                                    <tr>
                                        <th>E-mail</th>
                                        <th>First name</th>
                                        <th>Last name</th>
                                        <th></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12" style="margin-top: 10px;">
                    <div class="col-xs-12 text-center">
                        <div class="form-group">
                            <a class="btn btn-success" onclick="saveDomain()">
                                <span class="glyphicon glyphicon-floppy-disk"></span>Save
                            </a>
                            <a class="btn btn-danger" onclick="deleteDomain()">
                                <span class="glyphicon glyphicon-remove "></span>Delete
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


</div>

</body>
<script type="text/javascript">
    var domains = [];
    var allUsers = [];

    var selected = -1;
    var numberOfAdded = 0;

    function addDomain() {
        var $newDomainName = $("#new-domain-name");
        var domainName = $newDomainName.val();
        var $list = $("#domain-list");
        $("#new-domain-alert").remove();
        if (domainName == "") {
            $('<div id="new-domain-alert" class="alert alert-danger" role="alert">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                    'Can not add empty name </div>').insertAfter($list);
        } else {
            var $domain = document.createElement("a");
            //need get id from DB
            var id = -(++numberOfAdded);
            $domain.appendChild(document.createTextNode(domainName));
            $domain.className = "list-group-item";
            var index = domains.length;
            $domain.onclick = function () {
                selectItem(getIndexOfDomainByName(domainName));
            };
            $list.append($domain);


            domains.push({
                id: id,
                name: domainName,
                type: "",
                region: "",
                info: "",
                street: "",
                building: "",
                apartment: "",
                users: []
            });
            selectItem(index);
        }
        $newDomainName.val("");
    }

    function addUser() {
        var $userEmailInput = $("#user-email-input");
        var email = $userEmailInput.val();
        $userEmailInput.val("");
        var user = getUserByEmail(email);
        $("#new-user-alert").remove();
        if (user != null) {
            $addUser(user);
            domains[selected].users.push(user);
        } else {
            $('<div id="new-user-alert" class="alert alert-danger" role="alert">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                    'Can not find user with this e-mail </div>').insertBefore($('table'));
        }
    }

    function $addUser(user) {
        var $userTableBody = $("#user-table>table>tbody");
        $userTableBody.append($('<tr class="user-info">\n\
                                    <td class="email" value="' + user.email + '">' + user.email + '</td>\n\
                                    <td>' + user.firstName + '</td>\n\
                                    <td>' + user.lastName + '</td>\n\
                                    <td><div class="from-group"><a class="btn btn-danger" onclick="removeUser(this)">\n\
                                        <span class="glyphicon glyphicon-remove "></span>Remove user\n\
                                    </a></div></td>\n\
                                </tr>'));
    }

    function removeUser($element) {
        var email = "satan";
        email = $element.parentElement.parentElement.parentElement.firstElementChild.getAttribute("value");
        $element.parentNode.parentNode.parentNode.remove();
        domains[selected].users.forEach(function (user, i) {
            if (email == user.email) {
                domains[selected].users.splice(i, 1);
                return;
            }
        })
    }

    function getUserByEmail(email) {
        var user;
        //for test
//        allUsers.forEach(function (item, i) {
//            if (email == item.email) {
//                user = item;
//            }
//        });
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/api/client/domains/get/user",
            dataType: 'json',
            success: function (data) {
                console.log("getUser() success");
                user = data;
            },
            error: function (e) {
                console.log("getUser() error");
                user = null;
            }
        });
        return user;
    }

    function getIndexOf$Domain($domain) {
        var $list = $("#domain-list");
        return $list.index($domain);
    }

    function getIndexOfDomainByName(name) {
        var index;
        domains.forEach(function (domain, i) {
            if (name == domain.name) {
                index = i;
            }
        });
        return index;
    }

    function get$DomainByIndex(index) {
        var $list = $("#domain-list");
        return $list.find("a:nth-child(" + (index + 1) + ")");
    }

    function changeDomainType() {
        //need changes
        var $domainTypeSelector = $("#domain-type-selector");
        domains[selected].type = $domainTypeSelector.val();
    }

    function selectItem(index) {
        console.log("Selected " + selected);
        console.log("index" + index);
        if (selected == -1) {
            $("#domain-editor").removeClass("hidden");
        }
        if (index == -1) {
            $("#domain-editor").addClass("hidden");
        }
        $(".user-info").remove();

        var $domain = get$DomainByIndex(selected);
        console.log($domain != null);
        if ($domain != null) {
            $domain.removeClass("active");
            $domain = get$DomainByIndex(index);
            $domain.addClass("active");
            //save prev active domain
            if (selected != -1) {
                domains[selected].name = $("#domain-name-input").val();
                domains[selected].type = $("#domain-type-selector").val();
                domains[selected].region = $("#domain-region-input").val();
                domains[selected].info = $("#domain-info-input").val();
                domains[selected].street = $("#domain-street-input").val();
                domains[selected].building = $("#domain-building-input").val();
                domains[selected].apartment = $("#domain-apartment-input").val();
                saveUsers(selected);
            }
        }

        //load new active domain
        $("#domain-name-input").val(domains[index].name);
        $("#domain-type-selector").val(domains[index].type);
        $("#domain-region-input").val(domains[index].region);
        $("#domain-info-input").val(domains[index].info);
        $("#domain-street-input").val(domains[index].street);
        $("#domain-building-input").val(domains[index].building);
        $("#domain-apartment-input").val(domains[index].apartment);
        loadUsers(index);

        selected = index;
        if (selected == -1) {
            $("#user-editor").addClass("hidden");
        } else {
            $("#user-editor").removeClass("hidden");
        }
    }

    function saveUsers(index) {
        var $usersIds = $('[name = "user-id"]');
        for (var i = 0; i < $usersIds.length; i++) {
            domain[index].users[i].push(getUserById($usersIds.val()));
        }
    }

    function loadUsers(index) {
        if (domains[index].users != undefined) {
            for (var i = 0; i < domains[index].users.length; i++) {
                $addUser(domains[index].users[i]);
            }
        }
    }

    function saveDomain() {
        var domain = domains[selected];
        var frontendDomain = {
            domainId: domain.id,
            domainName: domain.name,
            regionId: domain.region,
            address: {
                info: domain.info,
                street: domain.street,
                building: domain.building,
                apartment: domain.apartment
            },
            domainType: {
                categoryId: 6,
                categoryName: domain.type
            },
            users: domain.users
        };
        $.ajax({
            type: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            contentType: "application/json",
            url: "/api/client/domains/update",
            data: JSON.stringify(frontendDomain),
            dataType: 'json',
            cache: false,
            timeout: 600000,
            success: function (data) {
                console.log("saveDomain() succes");
            },
            error: function (e) {
                console.log("saveDomain() error");
            }
        });
    }

    function deleteDomain() {
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/api/client/domains/delete",
            data: JSON.stringify({id: domains[selected].id}),
            dataType: 'json',
            cache: false,
            timeout: 600000,
            success: function (data) {
                delete$Domain();
                console.log("deleteDomain() succes");
            },
            error: function (e) {
                console.log("deleteDomain() error");
            }
        });
    }

    function delete$Domain() {
        domains.splice(selected, 1);
        $("#domain-list>.active").remove();
        selected = -1;
        selectItem(domains.length - 1);
    }

    function loadUserDomains() {
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/api/client/domains/get/all",
            dataType: 'json',
            success: function (data) {
                console.log("loadUserDomains() success");
                data.forEach(function (domain, i) {
                    domains.push({
                        id: domain.domainId,
                        name: domain.domainName,
                        type: domain.domainType.categoryName,
                        region: domain.regionId,
                        info: domain.address.info,
                        street: domain.address.street,
                        building: domain.address.building,
                        apartment: domain.address.apartment,
                        users: domain.users != null ? domain.users : []
                    });
                    numberOfAdded++;
                    //add to html
                    //var $newDomainName = $("#new-domain-name");
                    //var domainName = $newDomainName.val();
                    var $list = $("#domain-list");
                    var $domain = document.createElement("a");
                    //need get id from DB
                    var id = -(++numberOfAdded);
                    var domainName = domains[i].name;
                    $domain.appendChild(document.createTextNode(domainName));
                    $domain.className = "list-group-item";
                    var index = domains.length;
                    $domain.onclick = function () {
                        selectItem(getIndexOfDomainByName(domainName));
                    };
                    $list.append($domain);
                })
            },
            error: function (e) {
                console.log("loadUserDomains() error");
            }
        });
    }


    //don't needmj only for tests
    function loadAllUsers() {
        allUsers.push({
            id: 29,
            email: "melnyk@gmail.com",
            firstName: "andrey",
            lastName: "melnyk"
        });

        allUsers.push({
            id: 666,
            email: "pupkin@gmail.com",
            firstName: "vasya",
            lastName: "pupkin"
        });
    }

    $(document).ready(function () {
        loadAllUsers();
        loadUserDomains();
    });
</script>
</html>