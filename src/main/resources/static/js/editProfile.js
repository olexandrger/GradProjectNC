var ordersListSize = 10;
var ordersListCurrentPage = 0;
var instancesListSize = 10;
var instancesListCurrentPage = 0;
var ordersData;
const ORDER_STATUS_CREATED = "CREATED";
const ORDER_STATUS_IN_PROGRESS = "IN_PROGRESS";
const ORDER_STATUS_CANCELLED = "CANCELLED";
const ORDER_STATUS_COMPLETED = "COMPLETED";

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


function selectOrder(index) {
    if (index == -1) {
        $("#order-main").addClass("hidden");
    } else {
        $("#order-main").removeClass("hidden");
    }

    selectedOrder = index;

    var list = $("#csr-orders-list");

    list.find("a").removeClass("active");
    list.find("a:nth-child(" + (index + 1) + ")").addClass("active");

    if (selectedOrder != -1) {
        list.find("a:nth-child(" + (index + 1) + ")").find("span").text(ordersData[selectedOrder].status);

        var span = list.find("a:nth-child(" + (index + 1) + ")").find("span").get(0);
        span.className = "label orders-list ";
        span.className += getLabelName(ordersData[selectedOrder].status);

        $("#order-user-name").val(ordersData[selectedOrder].userName);
        $("#order-aim").val(ordersData[selectedOrder].orderAim);
        $("#order-status").val(ordersData[selectedOrder].status);
        $("#order-start-date").val(moment.unix(ordersData[selectedOrder].openDate).format("LLL"));
        if (ordersData[selectedOrder].closeDate != null) {
            $("#order-end-date").val(moment.unix(ordersData[selectedOrder].closeDate).format("LLL"));
        } else {
            $("#order-end-date").val("");
        }

        var domainSelector = $('#order-domain');
        var productSelector = $('#order-product');


        if (ordersData[selectedOrder].orderAim == ORDER_AIM_CREATE && ordersData[selectedOrder].status == ORDER_STATUS_CREATED) {
            domainSelector.prop("disabled", false);
            productSelector.prop("disabled", false);
        } else {
            domainSelector.prop("disabled", true);
            productSelector.prop("disabled", true);
        }

        if (ordersData[selectedOrder].possibleDomains == null) {
            domainSelector.get(0).parentNode.style.display = "none";
        } else {
            domainSelector.get(0).parentNode.style.display = "block";
            domainSelector.empty();
            for (var domainId in ordersData[selectedOrder].possibleDomains) {
                var domainHtml = "<option value='" + domainId + "' " +
                    (domainId == ordersData[selectedOrder].domainId ? "selected" : "") + " >" +
                    ordersData[selectedOrder].possibleDomains[domainId] +
                    "</option>";
                domainSelector.append($(domainHtml));
            }
        }

        if (ordersData[selectedOrder].possibleProducts == null) {
            productSelector.get(0).parentNode.style.display = "none";
        } else {
            productSelector.get(0).parentNode.style.display = "block";
            productSelector.empty();
            for (var productId in ordersData[selectedOrder].possibleProducts) {

                var productHtml = "<option value='" + productId + "' " +
                    (productId == ordersData[selectedOrder].productId ? "selected" : "") + " >" +
                    ordersData[selectedOrder].possibleProducts[productId] +
                    "</option>";
                productSelector.append($(productHtml));
            }
        }


        $(".order-button").addClass("hidden");

        if (!(ordersData[selectedOrder].responsibleId == null || ordersData[selectedOrder].responsibleId == currentUserId)) {
            domainSelector.prop("disabled", true);
            productSelector.prop("disabled", true);
        } else {
            if (ordersData[selectedOrder].status == ORDER_STATUS_CREATED) {
                $("#order-button-start").removeClass("hidden");
                $("#order-button-cancel").removeClass("hidden");

                if (ordersData[selectedOrder].orderAim == ORDER_AIM_CREATE) {
                    $("#order-button-update").removeClass("hidden");
                }
            }
            if (ordersData[selectedOrder].status == ORDER_STATUS_IN_PROGRESS) {
                $("#order-button-complete").removeClass("hidden");
                $("#order-button-cancel").removeClass("hidden");
            }
        }

    }
}

function loadOrders() {
    $.ajax({
        url: "/api/client/orders/get/all/size/" + (ordersListSize + 1) + "/offset/" + ordersListCurrentPage * ordersListSize,
        success: function (data) {
            var list = $("#csr-orders-list");
            list.empty();

            console.log(data)
            ordersData = data;

            data.forEach(function (item, i) {
                if (i < ordersListSize) {
                    var ref = document.createElement("a");
                    var orderName = item.orderAim + " " + item.productName + " #" + item.productOrderId;
                    ref.appendChild(document.createTextNode(orderName));
                    var span = document.createElement("span");
                    span.className = "label orders-list ";
                    span.className += getLabelName(item.status);
                    span.appendChild(document.createTextNode(item.status));
                    ref.appendChild(span);
                    ref.className = "list-group-item";
                    ref.href = "#";
                    ref.onclick = function () {
                        selectOrder(i);
                    };
                    list.append(ref);
                }
            });

            var prevPage = $("#orders-page-previous");
            var nextPage = $("#orders-page-next");
            nextPage.addClass("hidden");
            prevPage.addClass("hidden");
            if (ordersListCurrentPage > 0) {
                prevPage.removeClass("hidden");
            }
            if (data.length > ordersListSize) {
                nextPage.removeClass("hidden");
            }

            selectOrder(-1);
        },
        error: function () {
            console.error("Cannot load list product types");
        }
    });
}

function getLabelName(status) {
    if (status == ORDER_STATUS_CREATED) {
        return "label label-info orders-list";
    } else if (status == ORDER_STATUS_IN_PROGRESS) {
        return "label label-primary orders-list";
    } else if (status == ORDER_STATUS_CANCELLED) {
        return "label label-danger orders-list";
    } else if (status == ORDER_STATUS_COMPLETED) {
        return "label label-success orders-list";
    }
}

function nextPage() {
    ordersListCurrentPage++;
    loadOrders();
}

function previousPage() {
    ordersListCurrentPage--;
    loadOrders();
}



$(document).ready(function () {
    loadInfo();
});

function loadAllInstances() {

    $.ajax({
        url: "/api/client/instance/find/byUser/size/" + (instancesListSize + 1) + "/offset/" + instancesListCurrentPage * instancesListSize,
        success: function(data) {
            var list = $("#instances-list");
            list.empty();
            instances = data;
            data.forEach(function(item, i) {
                if (i < ordersListSize){
                    console.log(item);
                var ref = document.createElement("a");
                var orderName = item.product.productName + " for " + item.instanceId;
                ref.appendChild(document.createTextNode(orderName));
                ref.className = "list-group-item";
                ref.href = "#";
                ref.onclick = function () {
                    selectInstance(i);
                };
                list.append(ref);
                }
            });
            var prevPage = $("#instances-page-previous");
            var nextPage = $("#instances-page-next");
            nextPage.addClass("hidden");
            prevPage.addClass("hidden");
            if (instancesListCurrentPage > 0) {
                prevPage.removeClass("hidden");
            }
            if (data.length > instancesListSize) {
                nextPage.removeClass("hidden");
            }

            selectOrder(-1);
        },
        error: function () {
            console.error("Cannot load list of users");
        }
    });
}

function nextInstancesPage() {
    instancesListCurrentPage++;
    loadAllInstances();
}

function previousInstancesPage() {
    instancesListCurrentPage--;
    loadAllInstances();
}

function selectInstance(i) {
    instanceId = instances[i].instanceId;
    location.href = "/client/instance/" + instanceId;
}