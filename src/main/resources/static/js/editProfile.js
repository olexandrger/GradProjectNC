var ordersListSize = 10;
var ordersListCurrentPage = 0;
var instancesListSize = 10;
var instancesListCurrentPage = 0;
var ordersData;
const ORDER_STATUS_CREATED = "CREATED";
const ORDER_STATUS_IN_PROGRESS = "IN_PROGRESS";
const ORDER_STATUS_CANCELLED = "CANCELLED";
const ORDER_STATUS_COMPLETED = "COMPLETED";

var selectedComplain = -1;
var complaintListSize = 10;
var complaintListCurrentPage = 0;
var complaintsData;
var modalAlert;
var selectUserId;
var currentUserId;

const CATEGORY_TYPE_COMPLAIN_REASON = 5;
const MIN_SUBJECT_LENGTH = 5;
const MIN_RESPONCE_LENGTH = 5;
const COMPLAINT_STATUS_CREATED = 5;
const COMPLAINT_STATUS_UNDER_CONSIDERATION = 6;
const COMPLAINT_STATUS_CONSIDERATION_COMPLETED = 7;

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

function loadComplaints() {
    $.ajax({
        url: "/api/profile/complaint/get/all/size/" + (complaintListSize + 1) + "/offset/" + complaintListSize * complaintListCurrentPage,
        success: function (data) {
            $("#no-complain-selected-alert").removeAttr("hidden");
            list = $("#complain-list");
            list.empty();
            complaintsData = data;
            console.log(data);
            data.forEach(function (item, i) {
                if (i < complaintListSize) {
                    ref = document.createElement("a");
                    span = document.createElement("span");
                    span.className = getSpanClass(item.statusId);
                    span.appendChild(document.createTextNode(item.status));
                    complaintName = item.userName + ": " + item.complainReason;
                    ref.appendChild(document.createTextNode("#" + item.complainId + ": " + item.userName + ": "));
                    ref.append(span);
                    ref.appendChild(document.createElement("br"));
                    if (item.productInstanceName != null) {
                        ref.appendChild(document.createTextNode(item.productInstanceName + " - " + item.complainReason));
                    } else {
                        ref.appendChild(document.createTextNode(item.complainReason));
                    }

                    ref.className = "list-group-item";
                    ref.href = "#";
                    ref.onclick = function () {
                        selectComplaint(i);
                    };
                    list.append(ref);
                }
            });
            prevPage = $("#complaint-btn-prev");
            nextPage = $("#complaint-btn-next");
            prevPageUp = $("#complaint-btn-prev-up");
            nextPageUp = $("#complaint-btn-next-up");
            nextPage.attr("disabled", "disabled");
            prevPage.attr("disabled", "disabled");
            nextPageUp.attr("disabled", "disabled");
            prevPageUp.attr("disabled", "disabled");
            if (complaintListCurrentPage > 0) {
                prevPage.removeAttr("disabled");
                prevPageUp.removeAttr("disabled");
            }
            if (data.length > complaintListSize) {
                nextPage.removeAttr("disabled");
                nextPageUp.removeAttr("disabled");
            }
            selectComplaint(selectedComplain);
        },
        error: function () {
            console.error("Cannot load list complaints");
            alertError("Internal server error!")
        }
    });
}

function getSpanClass(statusId) {
    switch(statusId){
        case COMPLAINT_STATUS_CREATED:
            return "label label-warning";
            break;
        case COMPLAINT_STATUS_UNDER_CONSIDERATION:
            return "label label-info";
            break;
        case COMPLAINT_STATUS_CONSIDERATION_COMPLETED:
            return"label label-success";
            break;
        default:
            return "label label-danger";
            break;
    }
}

function selectComplaint(index) {
    list = $("#complain-list");
    $("#responsible-info-row").addClass("hidden");
    $("#instance-info-row").addClass("hidden");
    selectedComplain = index;
    if (index == -1) {
        $("#no-complain-selected-alert").removeClass("hidden");
        $("#complain-info-panel").addClass("hidden");
        list.find("a").removeClass("active");
    } else {
        $("#no-complain-selected-alert").addClass("hidden");
        $("#complain-info-panel").removeClass("hidden");
        list.find("a").removeClass("active");
        list.find("a:nth-child(" + (selectedComplain + 1) + ")").addClass("active");
    }
    if (selectedComplain != -1) {
        $("#complain-user-email").val(complaintsData[selectedComplain].userEmail);
        userDetails = document.createTextNode(complaintsData[selectedComplain].userName + ", Phpne: " + complaintsData[selectedComplain].userNumber);
        $("#complain-user-details").empty();
        $("#complain-user-details").append(userDetails);
        if (complaintsData[selectedComplain].responsibleEmail != null) {
            $("#complain-responsible-email").val(complaintsData[selectedComplain].responsibleEmail);
            $("#responsible-info-row").removeClass("hidden");
            responsibleDetails = document.createTextNode(complaintsData[selectedComplain].responsibleName + ", Phone: " + (complaintsData[selectedComplain].responsibleNumber));
            $("#complain-responsible-details").empty();
            $("#complain-responsible-details").append(responsibleDetails);
        }
        if (complaintsData[selectedComplain].productInstanceName != null) {
            $("#complain-instance-name").val(complaintsData[selectedComplain].productInstanceName);
            $("#instance-info-row").removeClass("hidden");
        }

        $("#selected-complain-stats").val(complaintsData[selectedComplain].status);
        $("#selected-complain-reason").val(complaintsData[selectedComplain].complainReason);

        $("#selected-complain-start-date").val(moment.unix(complaintsData[selectedComplain].openDate).format("LLL"));
        if (complaintsData[selectedComplain].closeDate != null) {
            $("#selected-complain-end-date").val(moment.unix(complaintsData[selectedComplain].closeDate).format("LLL"));
        } else {
            $("#selected-complain-end-date").val("");
        }
        $("#selected-complain-title").val(complaintsData[selectedComplain].complainTitle);
        $("#selected-complain-content").val(complaintsData[selectedComplain].content);
        $("#selected-complain-responce").val(complaintsData[selectedComplain].response);
        loadControlButtons();
    }
}

function loadDomainsInModal(keyCode) {
    if (keyCode != 13) {
        return;
    }
    modalAlert = $("#new-complaint-modal-error-msg");
    $("#new-complaint-domain").empty();
    if ($("#new-complaint-user-email").val().length < 1) {
        modalAlert.html("<strong>Warning! </strong> E-mail field is empty!");
        modalAlert.removeClass("hidden");
        clearNewComplaintModalFormByIncorrectEmail();
    } else {
        $.ajax({
            url: "/api/pmg/domains/find/bymail/" + $("#new-complaint-user-email").val() + "/",
            success: function (data) {
                if (data.status == "not found") {
                    modalAlert.html("<strong> Warning! </strong> Email not found");
                    modalAlert.removeClass("hidden");
                    clearNewComplaintModalFormByIncorrectEmail();
                } else {
                    selectUserId = data.userId;
                    if (data.domains.length > 0) {
                        options = $("#new-complaint-domain");
                        data.domains.forEach(function (item, i) {
                            modalAlert.empty();
                            modalAlert.addClass("hidden");
                            option = document.createElement("option");
                            if (i == 0) {
                                option.setAttribute("selected", "selected");
                            }
                            option.setAttribute("value", item.domainId);
                            option.appendChild(document.createTextNode(item.domainName));
                            options.append(option);
                        });
                        options.removeAttr("disabled");
                        loadProductInstancesInModal();
                    } else {
                        modalAlert.html("<strong> Warning! </strong> This user does not have any domains!");
                        modalAlert.removeClass("hidden");
                        clearNewComplaintModalFormByIncorrectEmail();
                    }
                }
            },
            error: function () {
                modalAlert.html("<strong>Error! </strong> Internal server error!");
                modalAlert.removeClass("hidden");
                clearNewComplaintModalFormByIncorrectEmail();
            }
        });
    }
}

function loadProductInstancesInModal() {
    modalAlert = $("#new-complaint-modal-error-msg");
    $.ajax({
        url: "/api/pmg/instances/find/bydomain/" + $("#new-complaint-domain").val() + "/",
        success: function (data) {
            clearNewComplaintModalFormByIncorrectDomain();
            modalAlert.empty();
            modalAlert.addClass("hidden");
            options = $("#new-complaint-instanse");
            option = document.createElement("option");
            option.setAttribute("value", "-1");
            option.appendChild(document.createTextNode("It does not apply to the product"));
            option.setAttribute("selected", "selected");
            options.append(option);
            data.forEach(function (item, i) {
                option = document.createElement("option");
                option.setAttribute("value", item.instanceId);
                option.appendChild(document.createTextNode(item.product.productName));
                options.append(option);
            });
            options.removeAttr("disabled");
            $("#new-complaint-subject").removeAttr("disabled");
            $("#new-complaint-reason").removeAttr("disabled");
            $("#new-complaint-content").removeAttr("readonly");
        },
        error: function () {
            clearNewComplaintModalFormByIncorrectDomain();
            modalAlert.html("<strong>Error! </strong> Internal server error!");
            modalAlert.removeClass("hidden");
        }

    });
}

function clearNewComplaintModalForm() {
    $("#new-complaint-user-email").val("");
    clearNewComplaintModalFormByIncorrectEmail();
}
function clearNewComplaintModalFormByIncorrectEmail() {
    $("#new-complaint-domain").attr("disabled", "disabled");
    $("#new-complaint-domain").empty();
    clearNewComplaintModalFormByIncorrectDomain();
}
function clearNewComplaintModalFormByIncorrectDomain() {
    $("#new-complaint-instanse").attr("disabled", "disabled");
    $("#new-complaint-instanse").empty();
    $("#new-complaint-subject").attr("disabled", "disabled");
    $("#new-complaint-subject").val("");
    $("#new-complaint-content").attr("readonly", "readonly");
    $("#new-complaint-content").val("");
    $("#new-complaint-reason").attr("disabled", "disabled");
    $("#create-complaint-ftom-modal-btn").attr("disabled", "disabled");
}

function getPrevPage() {
    if (complaintListCurrentPage > 0) {
        complaintListCurrentPage -= 1;
    }
    selectedComplain = -1;
    loadComplaints();
}

function getNextPage() {
    complaintListCurrentPage++;
    selectedComplain = -1;
    loadComplaints();
}

function takeComplaintForConsideration() {
    $.ajax({
        url: "/api/pmg/complaint/take/byId/",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        contentType: 'application/json',
        data: JSON.stringify({
            complaintId: complaintsData[selectedComplain].complainId
        }),
        success: function (data) {
            loadComplaints();
            printResult(data);
        },
        error: function () {
            alertError("Internal server error!")
        }
    });
}

function unlockCompletConsiderationButton() {
    if ($("#selected-complain-responce").val().length > MIN_RESPONCE_LENGTH) {
        $("#complet-consideration-complaint-btn").removeAttr("disabled");
    } else {
        $("#complet-consideration-complaint-btn").attr("disabled", "disabled");
    }
}

function completComplaintConsideration() {
    $.ajax({
        url: "/api/pmg/complaint/complete/byid/",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        contentType: 'application/json',
        data: JSON.stringify({
            complaintId: complaintsData[selectedComplain].complainId,
            response: $("#selected-complain-responce").val()
        }),
        success: function (data) {
            selectedComplain = 0;
            loadComplaints();
            printResult(data);
        },
        error: function () {
            alertError("Internal server error!")
        }
    });
}

function loadControlButtons() {
    takeComplaintBtn = $("#take-complaint-btn");
    completComplaintBtn = $("#complet-consideration-complaint-btn");
    takeComplaintBtn.attr("disabled", "disabled");
    takeComplaintBtn.addClass("hidden");
    completComplaintBtn.attr("disabled", "disabled");
    completComplaintBtn.addClass("hidden");
    addButtons();


}

function addButtons() {

    switch (complaintsData[selectedComplain].statusId) {
        case COMPLAINT_STATUS_CREATED:
            takeComplaintBtn.removeAttr("disabled");
            takeComplaintBtn.removeClass("hidden");
            break;

        case COMPLAINT_STATUS_UNDER_CONSIDERATION:
            if (currentUserId == complaintsData[selectedComplain].responsiblId) {
                completComplaintBtn.removeClass("hidden");
            }
            break;

        case COMPLAINT_STATUS_CONSIDERATION_COMPLETED:

            break;

        default:
            alertError("Unknown complaint status");
    }
}

function alertError(msg) {
    container = $("#alerts-bar");
    alert = document.createElement("div");
    alert.className = "alert alert-danger alert-dismissable";
    alert.appendChild(document.createTextNode(msg));
    ref = document.createElement("a");
    ref.appendChild(document.createTextNode("X"));
    ref.href = "#";
    ref.className = "close"
    ref.setAttribute("data-dismiss", "alert");
    ref.setAttribute("aria-label", "close");
    alert.appendChild(ref);
    container.append(alert);
}

function alertSuccess(msg) {
    container = $("#alerts-bar");
    alert = document.createElement("div");
    alert.className = "alert alert-success alert-dismissable";
    alert.appendChild(document.createTextNode(msg));
    ref = document.createElement("a");
    ref.appendChild(document.createTextNode("X"));
    ref.href = "#";
    ref.className = "close"
    ref.setAttribute("data-dismiss", "alert");
    ref.setAttribute("aria-label", "close");
    alert.appendChild(ref);
    container.append(alert);
}

function printResult(data) {
    if (data.status == "success") {
        alertSuccess(data.message)
    } else {
        alertError(data.message)
    }
}
function unlockCreateButton() {
    if ($("#new-complaint-subject").val().length < MIN_SUBJECT_LENGTH) {
        $("#create-complaint-ftom-modal-btn").attr("disabled", "disabled");
    } else {
        $("#create-complaint-ftom-modal-btn").removeAttr("disabled");
    }
}


function firstLoadComplaints() {
    $.ajax({
        url: "/api/user/account",
        success: function (data) {
            currentUserId = data.userId;
            loadComplaints();
        },
        error: function () {
            alertError("Internal server error!")

        }
    });
}

$(document).ready(function () {
    loadInfo();
});

