var domains = [];
var authorizedUser;
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
        $list.append($('<a class="list-group-item active" onclick = selectItem(getIndexOfDomainByName(this.innerText))>'+domainName+'</a>'));
        var id = -(++numberOfAdded);
        var index = domains.length;

        // var $domain = document.createElement("a");
        // //need get id from DB
        // var id = -(++numberOfAdded);
        // $domain.appendChild(document.createTextNode(domainName));
        // $domain.className = "list-group-item";
        // var index = domains.length;
        // $domain.onclick = function () {
        //     selectItem(getIndexOfDomainByName(domainName));
        // };
        // $list.append($domain);

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
        addAuthorizedUser();
        selectItem(index);
    }
    $newDomainName.val("");
}

function addAuthorizedUser() {
    $.ajax({
        type: "GET",
        url: "/api/client/domains/get/user/authorized",
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        dataType: 'json',
        success: function (data) {
            console.log("getAuthorizedUser() success");
            console.log(JSON.stringify(data));
            authorizedUser = {
                userId: data.userId,
                email: data.email,
                firstName: data.firstName,
                lastName: data.lastName
            };
            $addUser(authorizedUser);
            domains[selected].users.push(authorizedUser);
            console.log("authorizedUser in ajax");
            console.log(JSON.stringify(authorizedUser));
        },
        error: function (e) {
            console.log(JSON.stringify(e));
            $('<div id="new-user-alert" class="alert alert-danger" role="alert">' +
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                'Can not find user with this e-mail </div>').insertBefore($('table'));
        }
    });
}

function addUser() {
    var $userEmailInput = $("#user-email-input");
    var email = $userEmailInput.val();
    $userEmailInput.val("");
    $("#new-user-alert").remove();
    $.ajax({
        type: "GET",
        url: "/api/client/domains/get/user?email=" + email,
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        dataType: 'json',
        success: function (data) {
            console.log(JSON.stringify(data));
            user = {
                userId: data.userId,
                email: data.email,
                firstName: data.firstName,
                lastName: data.lastName
            };
            $addUser(user);
            domains[selected].users.push(user);
            console.log("user in ajax");
            console.log(JSON.stringify(user));
        },
        error: function (e) {
            console.log(JSON.stringify(e));
            $('<div id="new-user-alert" class="alert alert-danger" role="alert">' +
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                'Can not find user with this e-mail </div>').insertBefore($('table'));
        }
    });
}

function $addUser(user) {
    var $userTableBody = $("#user-table>table>tbody");
    if (user.email == authorizedUser.email) {
        $userTableBody.append($('<tr class="user-info">\n\
                                    <td class="email" value="' + user.email + '">' + user.email + '</td>\n\
                                    <td>' + user.firstName + '</td>\n\
                                    <td>' + user.lastName + '</td>\n\
                                    <td></td>\n\
                                </tr>'));
    } else {
        $userTableBody.append($('<tr class="user-info">\n\
                                    <td class="email" value="' + user.email + '">' + user.email + '</td>\n\
                                    <td>' + user.firstName + '</td>\n\
                                    <td>' + user.lastName + '</td>\n\
                                    <td><div class="from-group"><a class="btn btn-danger" onclick="removeUser(this)">\n\
                                        <span class="glyphicon glyphicon-remove "></span>Remove user\n\
                                    </a></div></td>\n\
                                </tr>'));
    }
    // $userTableBody.append($('<tr class="user-info">\n\
    //                                 <td class="email" value="' + user.email + '">' + user.email + '</td>\n\
    //                                 <td>' + user.firstName + '</td>\n\
    //                                 <td>' + user.lastName + '</td>\n\
    //                                 <td>' + (user.email != authorizedUser.email) ? '' : '<div class="from-group"><a class="btn btn-danger" onclick="removeUser(this)">\n\
    //                                     <span class="glyphicon glyphicon-remove "></span>Remove user\n\
    //                                 </a></div>' + '</td>\n\
    //                             </tr>'));
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

function getAuthorizedUser() {

}

function getUserByEmail(email) {
    //for test
//        allUsers.forEach(function (item, i) {
//            if (email == item.email) {
//                user = item;
//            }
//        });
    var user = null;
    $.ajax({
        type: "GET",
        url: "/api/client/domains/get/user?email=" + email,
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        dataType: 'json',
        success: function (data) {
            console.log(JSON.stringify(data));
            console.log(JSON.stringify(data));
            user = {
                userId: data.userId,
                email: data.email,
                firstName: data.firstName,
                lastName: data.lastName
            };
            console.log("user in ajax");
            console.log(JSON.stringify(authorizedUser));
        },
        error: function (e) {
            console.log(JSON.stringify(e));
            user = null;
        }
    });
    alert(JSON.stringify(user));
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

    $(".user-info").remove();
    if(selected == -1){
        $("#domain-editor").removeClass("hidden");
        $domain = get$DomainByIndex(index);
        $domain.addClass("active");
    } else {
        var $domain = get$DomainByIndex(selected);
        $domain.removeClass("active");
        $domain[0].textContent = $("#domain-name-input").val();
        $domain = get$DomainByIndex(index);
        $domain.addClass("active");
        domains[selected].name = $("#domain-name-input").val();
        domains[selected].type = $("#domain-type-selector").val();
        domains[selected].city = $("#domain-city-input").val();
        domains[selected].street = $("#domain-street-input").val();
        domains[selected].building = $("#domain-building-input").val();
        domains[selected].apartment = $("#domain-apartment-input").val();
        //saveUsers(selected);
    }
    if(index != -1){
        $("#domain-name-input").val(domains[index].name);
        $("#domain-type-selector").val(domains[index].type);
        $("#domain-city-input").val(domains[index].city);
        $("#domain-street-input").val(domains[index].street);
        $("#domain-building-input").val(domains[index].building);
        $("#domain-apartment-input").val(domains[index].apartment);
        loadUsers(index);
    } else {
        $("#domain-name-input").val("");
        $("#domain-type-selector").val("");
        $("#domain-city-input").val("");
        $("#domain-street-input").val("");
        $("#domain-building-input").val("");
        $("#domain-apartment-input").val("");
    }

    selected = index;
    if (selected == -1) {
        $("#user-editor").addClass("hidden");
    } else {
        $("#user-editor").removeClass("hidden");
    }


    // console.log("Selected " + selected);
    // console.log("index" + index);
    // if (selected == -1) {
    //     $("#domain-editor").removeClass("hidden");
    // }
    // if (index == -1) {
    //     $("#domain-editor").addClass("hidden");
    // }
    // $(".user-info").remove();
    //
    // var $domain = get$DomainByIndex(selected);
    // //console.log($domain != null);
    // if ($domain != null) {
    //     $domain.removeClass("active");
    //     $domain = get$DomainByIndex(index);
    //     $domain.addClass("active");
    //     //save prev active domain
    //     if (selected != -1) {
    //         domains[selected].name = $("#domain-name-input").val();
    //         domains[selected].type = $("#domain-type-selector").val();
    //         domains[selected].region = $("#domain-region-input").val();
    //         domains[selected].city = $("#domain-city-input").val();
    //         domains[selected].street = $("#domain-street-input").val();
    //         domains[selected].building = $("#domain-building-input").val();
    //         domains[selected].apartment = $("#domain-apartment-input").val();
    //         saveUsers(selected);
    //     }
    // }
    //
    // //load new active domain
    // $("#domain-name-input").val(domains[index].name);
    // $("#domain-type-selector").val(domains[index].type);
    // $("#domain-region-input").val(domains[index].region);
    // $("#domain-city-input").val(domains[index].city);
    // $("#domain-street-input").val(domains[index].street);
    // $("#domain-building-input").val(domains[index].building);
    // $("#domain-apartment-input").val(domains[index].apartment);
    // loadUsers(index);
    //
    // selected = index;
    // if (selected == -1) {
    //     $("#user-editor").addClass("hidden");
    // } else {
    //     $("#user-editor").removeClass("hidden");
    // }
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
    selectItem(selected);
    var domain = domains[selected];
    var frontendDomain = {
        domainId: domain.id,
        domainName: domain.name,
        regionId: domain.region,
        address: {
            city: domain.city,
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
    console.log(JSON.stringify(frontendDomain));
    //console.log($('meta[name=_csrf]').attr("content"));
    $.ajax({
        type: "POST",
        url: "/api/client/domains/update",
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content"),
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        processData: false,
        contentType: "application/json",
        data: JSON.stringify(frontendDomain),
        //dataType: 'json',
        success: function (data) {
            console.log(JSON.stringify(data));
            console.log(parseInt(data.domainId));
            if (domains[selected].id < 0) {
                domains[selected].id = parseInt(data.domainId);
            }
            console.log(domains[selected].id);
        },
        error: function (e) {
            console.log(JSON.stringify(e));
        }
    });
}

function deleteDomain() {
    $.ajax({
        type: "POST",
        url: "/api/client/domains/delete",
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        processData: false,
        contentType: "application/json",
        data: JSON.stringify({id: domains[selected].id}),
        //dataType: 'json',
        success: function (data) {
            delete$Domain();
            //selectItem(selected);
            console.log(JSON.stringify(data));
        },
        error: function (e) {
            console.log(JSON.stringify(e));
        }
    });
}

function delete$Domain() {
    domains.splice(selected, 1);
    $("#domain-list>.active").remove();
    selected = -1;
    selectItem(domains.length-1);

    // domains.splice(selected, 1);
    // $("#domain-list>.active").remove();
    // selected = -1;
    // selectItem(selected);
}

function loadUserDomains() {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/client/domains/get/all",
        dataType: 'json',
        success: function (data) {
            console.log(JSON.stringify(data));
            data.forEach(function (domain, i) {
                domains.push({
                    id: domain.domainId,
                    name: domain.domainName,
                    type: domain.domainType.categoryName,
                    region: domain.regionId,
                    city: domain.address.city,
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
            console.log(JSON.stringify(e));
        }
    });
}


//don't need only for tests
function loadAllUsers() {
    allUsers.push({
        userId: 29,
        email: "melnyk@gmail.com",
        firstName: "andrey",
        lastName: "melnyk"
    });

    allUsers.push({
        userId: 666,
        email: "pupkin@gmail.com",
        firstName: "vasya",
        lastName: "pupkin"
    });
}

$(document).ready(function () {
    loadAllUsers();
    loadUserDomains();
});