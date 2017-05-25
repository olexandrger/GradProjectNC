var domains = [];
var authorizedUser;

var selected = -1;
var numberOfAdded = 0;

var map;
var marker;
var infowindow;
var standartGooglePlaceId = "ChIJjw5wVMHZ0UAREED2iIQGAQA"; //Ukraine

function addDomain() {
    var $newDomainName = $("#new-domain-name");
    var domainName = $newDomainName.val();
    var $list = $("#domain-list");
    if (isDomainNameUnique(domainName)) {
        $("#new-domain-alert").remove();
        if (domainName == "") {
            $('<div id="new-domain-alert" class="alert alert-danger" role="alert">' +
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                'Can not add empty name </div>').insertAfter($list);
        } else {
            $list.append($('<a class="list-group-item active" onclick = selectItem(getIndexOfDomainByName(this.innerText))>' + domainName + '</a>'));
            var id = -(++numberOfAdded);
            var index = domains.length;

            domains.push({
                domainId: id,
                domainName: domainName,
                domainType: "",
                regionName: "",
                googlePlaceId: "",
                apartment: "",
                users: [],
                productInstances: []
            });
            addAuthorizedUser();
            selectItem(index);
        }
        $newDomainName.val("");
    } else {
        $('<div id="new-domain-alert" class="alert alert-danger" role="alert">' +
            '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
            'Not unique name of domain </div>').insertAfter($list);
    }
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
            if (selected != -1) {
                domains[selected].users.push(authorizedUser);
            }
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
    $("#new-user-alert").remove();
    if (isUserEmailUnique(email)) {
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
    } else {
        $('<div id="new-user-alert" class="alert alert-danger" role="alert">' +
            '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
            'This user already in table </div>').insertBefore($('table'));
    }
}

function $addInstance(instance) {
    console.log('$addInstance');
    console.log(JSON.stringify(instance));
    var $instanceTableBody = $("#instance-table>table>tbody");
    $instanceTableBody.append($('<tr class="instance-info">\n\
                                    <td>' + instance.product.productName + '</td>\n\
                                    <td>' + instance.status.categoryName + '</td>\n\
                                    <td>' + instance.price.price + '</td>\n\
                                    <td><a href="/client/instance/' + instance.instanceId + '">more information</a></td>\n\
                                </tr>'));
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
        if (name == domain.domainName) {
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
    var $domainTypeSelector = $("#domain-type-selector");
    domains[selected].domainType = $domainTypeSelector.val();
}

function selectItem(index) {
    console.log("Selected " + selected);
    console.log("index" + index);

    $(".user-info").remove();
    $(".instance-info").remove();
    $("#new-user-alert").remove();
    if (selected == -1) {
        $("#domain-editor").removeClass("hidden");
        $domain = get$DomainByIndex(index);
        $domain.addClass("active");
    } else {
        var $domain = get$DomainByIndex(selected);
        $domain.removeClass("active");
        $domain[0].textContent = $("#domain-name-input").val();
        $domain = get$DomainByIndex(index);
        $domain.addClass("active");
        domains[selected].domainName = $("#domain-name-input").val();
        domains[selected].domainType = $("#domain-type-selector").val();
        domains[selected].apartment = $("#domain-apartment-input").val();
        //saveUsers(selected);
    }
    if (index != -1) {
        if (domains[index].googlePlaceId == "") {
            geocodePlaceId(map, infowindow, standartGooglePlaceId);
        } else {
            geocodePlaceId(map, infowindow, domains[index].googlePlaceId);
        }
        $("#domain-name-input").val(domains[index].domainName);
        $("#domain-type-selector").val(domains[index].domainType);
        $("#domain-apartment-input").val(domains[index].apartment);
        loadUsers(index);
        loadInstances(index);
    } else {
        geocodePlaceId(map, infowindow, standartGooglePlaceId);
        $("#domain-name-input").val("");
        $("#domain-type-selector").val("");
        $("#domain-apartment-input").val("");
    }

    selected = index;
    if (selected == -1) {
        $("#user-editor").addClass("hidden");
        $("#instances").addClass("hidden");
        $("#domain-name-input").attr("readonly", "readonly");
        $("#domain-type-selector").attr("disabled", "disabled");
        $("#domain-apartment-input").attr("readonly", "readonly");
        $("#pac-input").addClass("hidden");
        $("#save-delete-buttons").addClass("hidden");
    } else {
        $("#user-editor").removeClass("hidden");
        $("#instances").removeClass("hidden");
        $("#domain-name-input").removeAttr("readonly");
        $("#domain-type-selector").removeAttr("disabled");
        $("#domain-apartment-input").removeAttr("readonly");
        $("#pac-input").removeClass("hidden");
        $("#save-delete-buttons").removeClass("hidden");
    }
}

function isDomainNameUnique(domainName) {
    var unique = true;
    domains.forEach(function (domain, i) {
        if (domain.domainName == domainName) {
            unique = false;
        }
    })
    console.log(unique);
    return unique;
}

function isUserEmailUnique(email) {
    var unique = true;
    domains[selected].users.forEach(function (user, i) {
        if (user.email == email) {
            unique = false;
        }
    })
    console.log(unique);
    return unique;
}

function changeDomainName() {
    var $domainNameInput = $("#domain-name-input");
    domainName = $domainNameInput.val();
    $list = $("#domain-list")
    if (domainName == "") {
        $('<div id="domain-name-alert" class="alert alert-danger" role="alert">' +
            '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
            'Domain must have not empty name </div>').insertAfter($list);
    } else if (isDomainNameUnique(domainName)) {
        domains[selected].domainName = domainName;
        selectItem(selected);
        $("#domain-name-alert").remove();
    } else {
        $('<div id="new-domain-alert" class="alert alert-danger" role="alert">' +
            '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
            'Domain with this name already exists </div>').insertAfter($list);
        $domainNameInput.val(domains[selected].domainName);
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

function loadInstances(index) {
    if (domains[index].productInstances != undefined) {
        console.log('adding instances');
        for (var i = 0; i < domains[index].productInstances.length; i++) {
            console.log(i);
            $addInstance(domains[index].productInstances[i]);
        }
    }
}

function saveDomain() {
    selectItem(selected);
    var domain = domains[selected];
    $("#save-domain-alert").remove();
    if (domain.domainName == "" || domain.domainType == "" || domain.googlePlaceId == "") {
        $('<div id="save-domain-alert" class="alert alert-danger" role="alert">' +
            '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
            'Domain name, domain type and domain location are required </div>').insertAfter($("#domain-list"));
    } else {
        var frontendDomain = {
            domainId: domain.domainId,
            domainName: domain.domainName,
            domainType: {
                categoryId: null,
                categoryName: domain.domainType
            },
            address: {
                addressId: null,
                apartment: domain.apartment,
                location: {
                    locationId: null,
                    googlePlaceId: domain.googlePlaceId,
                    region: {
                        regionId: null,
                        regionName: domain.regionName
                    }
                }
            },
            users: domain.users,
            productInstances: domain.productInstances
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
                if (domains[selected].domainId < 0) {
                    domains[selected].domainId = parseInt(data.domainId);
                }
                console.log(domains[selected].id);
                $('<div id="save-domain-alert" class="alert alert-success" role="alert">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                    'Domain successfully saved </div>').insertAfter($("#domain-list"));
            },
            error: function (e) {
                console.log(JSON.stringify(e));
            }
        });
    }
}

function deleteDomain() {
    $("#delete-domain-alert").remove();
    $.ajax({
        type: "POST",
        url: "/api/client/domains/delete",
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        processData: false,
        contentType: "application/json",
        data: JSON.stringify({id: domains[selected].domainId}),
        //dataType: 'json',
        success: function (data) {
            delete$Domain();
            //selectItem(selected);
            console.log(JSON.stringify(data));
        },
        error: function (e) {
            console.log(JSON.stringify(e));
            $('<div id="delete-domain-alert" class="alert alert-danger" role="alert">' +
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                'Can not delete domain </div>').insertAfter($("#domain-list"));
        }
    });
}

function delete$Domain() {
    domains.splice(selected, 1);
    $("#domain-list>.active").remove();
    selected = -1;
    selectItem(domains.length - 1);

    // domains.splice(selected, 1);
    // $("#domain-list>.active").remove();
    // selected = -1;
    // selectItem(selected);
}

function initMap() {
    console.log('iniMap()');

    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 48.379433, lng: 31.16558},
        zoom: 5
    });

    var input = document.getElementById('pac-input');

    var autocomplete = new google.maps.places.Autocomplete(input);
    autocomplete.bindTo('bounds', map);

    map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

    infowindow = new google.maps.InfoWindow();
    marker = new google.maps.Marker({
        map: map
    });
    marker.addListener('click', function () {
        infowindow.open(map, marker);
    });

    autocomplete.addListener('place_changed', function () {
        infowindow.close();
        var place = autocomplete.getPlace();
        if (!place.geometry) {
            return;
        }

        if (place.geometry.viewport) {
            map.fitBounds(place.geometry.viewport);
        } else {
            map.setCenter(place.geometry.location);
            map.setZoom(17);
        }

        // Set the position of the marker using the place ID and location.
        marker.setPlace({
            placeId: place.place_id,
            location: place.geometry.location
        });
        marker.setVisible(true);


        infowindow.open(map, marker);
        infowindow.setContent(document.getElementById('infowindow-content'));

        document.getElementById('place-name').textContent = place.name;
        document.getElementById('place-id').textContent = place.place_id;
        document.getElementById('place-address').textContent =
            place.formatted_address;

        console.log('content is setted');
        console.log('place_id' + place.place_id);
        console.log('standart place:' + place.place_id == standartGooglePlaceId);

        //writting info to domains
        if (place.place_id != standartGooglePlaceId) {
            domains[selected].googlePlaceId = place.place_id;
            place.address_components.forEach(function (component, i) {
                if (component.types.indexOf("administrative_area_level_1") != -1) {
                    switch (component.long_name) {
                        case 'Kyiv city':
                            domains[selected].regionName = 'Kyivs\'ka oblast';
                            break;
                        case 'Sevastopol\' city':
                            domains[selected].regionName = 'Crimea';
                            break;
                        default:
                            console.log(component.long_name);
                            domains[selected].regionName = component.long_name;
                    }
                }
            })
        }
    });
}

function geocodePlaceId(map, infowindow, placeId) {
    console.log('placeId = ' + placeId);
    var geocoder = new google.maps.Geocoder;
    infowindow.close();
    geocoder.geocode({'placeId': placeId}, function (results, status) {
        if (status === 'OK') {
            if (results[0]) {
                if (placeId != standartGooglePlaceId) {
                    map.setZoom(11);
                    map.setCenter(results[0].geometry.location);
                    marker.setPlace({
                        placeId: placeId,
                        location: results[0].geometry.location
                    })
                    marker.setVisible(true);

                    infowindow.open(map, marker);
                    //old content
                    //infowindow.setContent(results[0].formatted_address);
                    //new content
                    infowindow.setContent(document.getElementById('infowindow-content'));

                    document.getElementById('place-name').textContent = results[0].address_components[0].long_name;
                    document.getElementById('place-id').textContent = placeId;
                    document.getElementById('place-address').textContent = results[0].formatted_address;
                } else {
                    map.setCenter({lat: 48.379433, lng: 31.16558});
                    map.setZoom(5);
                }

            } else {
                console.log('No results found');
            }
        } else {
            console.log('Geocoder failed due to: ' + status);
        }
    });
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
                    domainId: domain.domainId,
                    domainName: domain.domainName,
                    domainType: domain.domainType.categoryName,
                    regionName: domain.address.location.region.regionName,
                    googlePlaceId: domain.address.location.googlePlaceId,
                    apartment: domain.address.apartment,
                    users: domain.users != null ? domain.users : [],
                    productInstances: domain.productInstances != null ? domain.productInstances : []
                });
                numberOfAdded++;
                var $list = $("#domain-list");
                var $domain = document.createElement("a");
                var domainName = domains[i].domainName;
                $domain.appendChild(document.createTextNode(domainName));
                $domain.className = "list-group-item";
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

$(document).ready(function () {
    addAuthorizedUser();
    loadUserDomains();
});