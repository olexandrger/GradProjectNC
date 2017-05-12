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
                                <input type="text" class="form-control" name="city" placeholder="City"
                                       id="domain-city-input">
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
                            <select class="form-control" id="user-selector">
                            </select>
                            <a class="btn btn-default" onclick="addUser()">
                                <span class="glyphicon glyphicon-plus"></span>Add
                            </a>
                        </div>
                        <div class="list-group" id="user-list"></div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12" style="margin-top: 10px;">
                        <div class="col-xs-12 text-center">
                            <div class="form-group">
                                <a class="btn btn-success" onclick="saveSelected()">
                                    <span class="glyphicon glyphicon-floppy-disk"></span>Save
                                </a>
                                <a class="btn btn-danger" onclick="deleteSelected()">
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
                selectItem(index);
            };
            $list.append($domain);
            domains.push({
                id: id,
                name: domainName,
                type: "",
                region: "",
                city: "",
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
        var id = $("#user-selector").val();
        var user = getUserById(id);
        $addUser(user);
        domains[selected].users.push(user);
    }

    function $addUser(user) {
        var $userList = $("#user-list");
        var id = user.id;
        var fullName = user.firstName + user.lastName;
        var $userItem = $(
                '<div class="form-inline user-container">\n\
                        <div class="form-group"><input type="text" class="form-control" placeholder="Id"\n\
                                           name="user-id" value="' + id + '"></div>\n\
                         <div class="form-group full-width"><input type="text" class="form-control full-width" placeholder="Full Name"\n\
                                                      name="user-full-name" value="' + fullName + '"></div>\n\
                        <div class="form-group"><a class="btn btn-danger" onclick="deleteUser(this)"><span\n\
                    class="glyphicon glyphicon-remove "></span></a></div>\n\
                    </div>');
        $userList.append($userItem);
    }

    function deleteUser(element) {
        element.parentNode.parentNode.parentNode.removeChild(element.parentNode.parentNode);
        var id = element.parentNode.parentNode.firstChild.firstChild.val();
        for (var i = 0; i < domains[selected].users.length; i++) {
            if (id == domains[selected].users[i]) {
                domains[selected].users.splice(i, 1);
                return;
            }
        }
    }

    function getUserById(id) {
        for (var i = 0; i < allUsers.length; i++) {
            if (id == allUsers[i].id) {
                return allUsers[i];
            }
        }
    }

    function getIndexOf$Domain($domain) {
        var $list = $("#domain-list");
        return list.index($domain);
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
        console.log("Selected " + index);
        if (selected == -1) {
            $("#domain-editor").removeClass("hidden");
        }
        if (index == -1) {
            $("#domain-editor").addClass("hidden");
        }
        var $list = $("#domain-list");
        var $domain = get$DomainByIndex(selected);
        $domain.removeClass("active");

        $domain = get$DomainByIndex(index);
        $domain.addClass("active");

        $(".user-container").remove();

        //save prev active domain
        if (selected != -1) {
            domains[selected].name = $("#domain-name-input").val();
            domains[selected].type = $("#domain-type-selector").val();
            domains[selected].region = $("#domain-region-input").val();
            domains[selected].city = $("#domain-city-input").val();
            domains[selected].street = $("#domain-street-input").val();
            domains[selected].building = $("#domain-building-input").val();
            domains[selected].apartment = $("#domain-apartment-input").val();
            saveUsers(selected);
        }

        //load new active domain
        $("#domain-name-input").val(domains[index].name);
        $("#domain-type-selector").val(domains[index].type);
        $("#domain-region-input").val(domains[index].region);
        $("#domain-city-input").val(domains[index].city);
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
        for (var i = 0; i < domains[index].users.length; i++) {
            $addUser(domains[index].users[i]);
        }
    }

    //ajax
    function saveSelected() {

    }
    //ajax
    function deleteSelected() {

    }

    //ajax
    function loadUserDomains() {
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/api/client/domains/get/all",
            dataType: 'json',
            success: function (data) {
                console.log("loadUserDomains() success");
                alert("succes");
                for (var i = 0; i < data.length; ++i) {
                    domains.push({
                        id: data[i].domainId,
                        name: data[i].domainName,
                        type: data[i].domainType.categoryName,
                        region: data[i].regionId,
                        city: data[i].address.city,
                        street: data[i].address.street,
                        building: data[i].address.building,
                        apartment: data[i].address.apartment
                    });
                    numberOfAdded++;
                    //add to html
                    //var $newDomainName = $("#new-domain-name");
                    //var domainName = $newDomainName.val();
                    var $list = $("#domain-list");
                    var $domain = document.createElement("a");
                    $domain.appendChild(document.createTextNode(domains[i].name));
                    $domain.className = "list-group-item";
                    var index = domains.length;
                    $domain.onclick = function () {
                        selectItem(index);
                    };
                    $list.append($domain);
                }

            },
            error: function (e) {
                console.log("loadUserDomains() error");
                alert("error");
            }
        });
    }


    //ajax
    function loadAllUsers() {
        var $userSelector = $("#user-selector");

        allUsers.push({
            id: 29,
            firstName: "andrey",
            lastName: "melnyk"
        });
        var fullName = allUsers[0].firstName + " " + allUsers[0].lastName;
        var id = allUsers[0].id;
        $userSelector.append($("<option value=" + id + ">" + fullName + "</option>"));
        allUsers.push({
            id: 666,
            firstName: "vasya",
            lastName: "pupkin"
        });
        fullName = allUsers[1].firstName + " " + allUsers[1].lastName;
        id = allUsers[1].id;
        $userSelector.append($("<option value=" + id + ">" + fullName + "</option>"));
    }

    $(document).ready(function () {
        loadAllUsers();
        //loadUserDomains();
    });
</script>
</html>