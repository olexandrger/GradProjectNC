var currentUserId;
var instanceId;
var complaintListSize = 5;
var complaintListCurrentPage = 0;

const MIN_SUBJECT_LENGTH = 5;
const CATEGORY_TYPE_COMPLAIN_REASON = 5;

function suspendInstance() {
    var path = window.location.pathname.split("/");
    var instanceId = path[path.length - 1];

    $.ajax({
        url: "/api/client/orders/new/suspend",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        processData: false,
        contentType: 'application/json',
        data: JSON.stringify({instanceId: instanceId}),
        success: function (data) {
            loadInstance();
        },
        error: function (data) {
            loadInstance();
        }
    });
}

function continueInstance() {
    var path = window.location.pathname.split("/");
    var instanceId = path[path.length - 1];

    $.ajax({
        url: "/api/client/orders/new/continue",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        processData: false,
        contentType: 'application/json',
        data: JSON.stringify({instanceId: instanceId}),
        success: function (data) {
            loadInstance();
        },
        error: function (data) {
            loadInstance();
        }
    });
}

function deactivateInstance() {
    var path = window.location.pathname.split("/");
    var instanceId = path[path.length - 1];

    $.ajax({
        url: "/api/client/orders/new/deactivate",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        processData: false,
        contentType: 'application/json',
        data: JSON.stringify({instanceId: instanceId}),
        success: function (data) {
            loadInstance();
        },
        error: function (data) {
            loadInstance();
        }
    });
}


function cancelOrder(orderId) {
    $.ajax({
        url: "/api/client/orders/" + orderId + "/cancel",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        success: function (data) {
            loadInstance();
        },
        error: function (data) {
            loadInstance();
        }
    });
}

function loadInfo() {
    path = window.location.pathname.split("/");
    instanceId = path[path.length - 1];
    loadInstance();
    loadComplaints();
}

const INSTANCE_STATUS_ACTIVATED = "ACTIVATED";
const INSTANCE_STATUS_SUSPENDED = "SUSPENDED";
const INSTANCE_STATUS_DEACTIVATED = "DEACTIVATED";
const INSTANCE_STATUS_CREATED = "CREATED";

function calculateDiscountPrice(priceObj) {
    var tmpPrice = priceObj.price;
    var discountObj = priceObj.discount;
    if (discountObj != null) {
        return tmpPrice - tmpPrice * discountObj.discount / 100;
    }
    return tmpPrice;
}
function loadInstance() {
    $.ajax({
        url: "/api/client/instance/get/byId/" + instanceId,
        success: function (data) {
            instanceId = data.instanceId;
            $("#instance-product-name").html(data.product.productName);
            $("#instance-status").html(data.status.categoryName);
            $("#instance-product-type").html(data.product.productType.productTypeName);
            var address = data.address;
            $("#instance-address").html(
                address.street + ", " + address.building +
                (address.apartment == undefined ? "" : " " + address.apartment)
                + "<br/>" + address.city + ", " + data.price.region.regionName);
            $("#instance-product-price").html(data.price.price);
            $("#instance-product-discount-price").html(calculateDiscountPrice(data.price));

            $("#instance-complain-button").removeClass("hidden");
            $("#instance-suspend-button").addClass("hidden");
            $("#instance-continue-button").addClass("hidden");
            $("#instance-deactivate-button").addClass("hidden");
            // if (data.status.categoryName == INSTANCE_STATUS_CREATED) {
            if (data.status.categoryName == INSTANCE_STATUS_ACTIVATED) {
                $("#instance-suspend-button").removeClass("hidden");
                $("#instance-continue-button").addClass("hidden");
                $("#instance-deactivate-button").removeClass("hidden");
            } else if (data.status.categoryName == INSTANCE_STATUS_SUSPENDED) {
                $("#instance-suspend-button").addClass("hidden");
                $("#instance-continue-button").removeClass("hidden");
                $("#instance-deactivate-button").addClass("hidden");
                // }  else if (data.status.categoryName == INSTANCE_STATUS_DEACTIVATED) {
                //     $("#instance-suspend-button").addClass("hidden");
                //     $("#instance-continue-button").addClass("hidden");
                //     $("#instance-deactivate-button").addClass("hidden");
            } else {
                // console.error("Unknown instasnce status: " + data.status.categoryName);
            }


            reloadOrders();

            /*
            var ordersTable = $("#instance-orders-table");
            ordersTable.find(".order-row").remove();
            data.productOrders.forEach(function (order) {
                var cancelId = order.productOrderId;
                var cancelButton = '<button class="btn pull-right" onclick="cancelOrder(' + cancelId + ')">Cancel</button>';

                var html = "<tr class='order-row' id='table-order-" + order.productOrderId + "'>" +
                    "<td class='col-sm-2'>" + order.orderAim + "</td>" +
                    "<td class='col-sm-2 order-status'>" + order.status + "</td>" +
                    "<td class='col-sm-2'>" + moment.unix(order.openDate).format("LLL") + "</td>" +
                    "<td class='col-sm-2 close-date'>" + (order.closeDate == null ? "" : moment.unix(order.closeDate).format("LLL")) + "</td>" +
                    "<td class='col-sm-2 cancel-button'>" + (order.status == "CREATED" ? cancelButton : "") + "</td>" +
                    "</tr>";

                ordersTable.append($(html));
            });
            */
        },
        error: function (data) {
            console.error("Failed to load instance");
            console.log(data);
        }
    });
}

const ordersPageSize = 10;
var ordersPageNumber = 0;

function loadMoreOrders() {
    addOrdersPage(ordersPageNumber++);
}

function addOrdersPage(pageNumber) {
    $.ajax({
        url: "/api/client/orders/get/byInstance/" + instanceId + "/size/" + (ordersPageSize + 1) + "/offset/" + (ordersPageNumber * ordersPageSize) ,
        success: function (data) {
            var ordersTable = $("#instance-orders-table");
            // ordersTable.find(".order-row").remove();
            if (data.length <= ordersPageSize) {
                $("#more-orders").addClass("hidden");
            } else {
                $("#more-orders").removeClass("hidden");
            }
            data.forEach(function (order) {
                var cancelId = order.productOrderId;
                var cancelButton = '<button class="btn pull-right" onclick="cancelOrder(' + cancelId + ')">Cancel</button>';

                var html = "<tr class='order-row' id='table-order-" + order.productOrderId + "'>" +
                "<td class='col-sm-2'>" + order.orderAim + "</td>" +
                "<td class='col-sm-2 order-status'>" + order.status + "</td>" +
                "<td class='col-sm-2'>" + moment.unix(order.openDate).format("LLL") + "</td>" +
                "<td class='col-sm-2 close-date'>" + (order.closeDate == null ? "" : moment.unix(order.closeDate).format("LLL")) + "</td>" +
                "<td class='col-sm-2 cancel-button'>" + (order.status == "CREATED" ? cancelButton : "") + "</td>" +
                "</tr>";

                ordersTable.append($(html));
            });

        },
        error: function (data) {
            console.error("Failed to load orders");
            console.log(data);
        },
        async: false
    });
}

function reloadOrders() {

    $("#instance-orders-table").find(".order-row").remove();

    //execute async
    setTimeout(function() {
        for (var i = 0; i <= ordersPageNumber; i++) {
            console.log(i);
            addOrdersPage(i);
        }
    }, 0);
}

function loadNewComplaintModal() {
    reasons = $("#new-complaint-reason");
    reasons.attr("disabled", "disabled");
    reasons.empty();
    $("#new-complaint-subject").val("");
    $("#new-complaint-content").val("");
    $("#instanse-depend-check").prop("checked", "checked");

    $.ajax({
        url: "/api/category/get/bytype/" + CATEGORY_TYPE_COMPLAIN_REASON + "/",
        success: function (data) {
            if (data.status = "found") {
                data.categories.forEach(function (item, i) {
                    option = document.createElement("option");
                    if (i == 0) {
                        option.setAttribute("selected", "selected");
                    }
                    option.setAttribute("value", item.categoryId);
                    option.appendChild(document.createTextNode(item.categoryName));
                    reasons.append(option);
                });
                reasons.removeAttr("disabled");
            }
            else {
                alertError("Error in the database");
                reasons.attr("disabled", "disabled");
                reasons.empty();
            }
        },
        error: function () {
            alertError("Internal server error!");
            reasons.attr("disabled", "disabled");
            reasons.empty();
        }
    })
}

function createComplaint() {
    reason = $("#new-complaint-reason").val();
    userId = currentUserId;
    complaintInstanceId = ($("#instanse-depend-check").prop("checked")) ? instanceId : "-1";
    complaintSubject = $("#new-complaint-subject").val();
    complaintContent = $("#new-complaint-content").val();
    $.ajax({
        url: "/api/client/complaints/new/",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        contentType: 'application/json',
        data: JSON.stringify({
            instanceId: complaintInstanceId,
            reasonId: reason,
            title: complaintSubject,
            content: complaintContent
        }),
        success: function (data) {
            loadComplaints();
            if (data.status == "success") {
                alertSuccess(data.message);
            } else {
                alertError(data.message);
            }

        },
        error: function () {
            alertError("Internal server error!")
        }
    });
}

function getPrevComplaintPage() {
    if (complaintListCurrentPage > 0) {
        complaintListCurrentPage -= 1;
    }
    loadComplaints();
}
function getNextComplaintPage() {
    complaintListCurrentPage++;
    loadComplaints();

}

function loadComplaints() {

    complaints = $("#instance-complaints-table");
    complaints.find(".complaint-row").remove();
    complaints.find(".complaint-content-row").remove();
    $.ajax({
        url: "/api/client/complaints/get/byInstance/" + instanceId + "/size/" + (complaintListSize + 1) + "/offset/" + complaintListCurrentPage * complaintListSize + "/",
        success: function (data) {
            data.forEach(function (complaint, i) {
                if (i < complaintListSize) {

                    html = "<tr class='complaint-row' onclick='selectComplaintRow(" + complaint.complainId + ")' id='table-order-" + complaint.complainId + "'>" +
                        "<td class='col-sm-2'>" + complaint.complainReason + "</td>" +
                        "<td class='col-sm-2'>" + complaint.status + "</td>" +
                        "<td class='col-sm-2'>" + moment.unix(complaint.openDate).format("LLL") + "</td>" +
                        "<td class='col-sm-2'>" +
                        (complaint.closeDate == null ? "" : moment.unix(complaint.closeDate).format("LLL")) + "</td>" + "</tr>"
                        + "<tr class='complaint-content-row hidden' align='center' id='table-order-content-" + complaint.complainId + "'>" +
                        "<td class='col-sm-2' colspan='4'> " +
                        "<form class='complain-content-form'>" +
                        "<div class='row'>" +
                        "<div class='form-group'>" +
                        "<label for='selected-complain-title'>Complaint subject:</label>" +
                        "<input type='text' class='form-control' id='selected-complain-title' value='"+complaint.complainTitle+"' disabled>" +
                        "</div>" +
                        "</div>" +
                        "<div class='row'>" +
                        "<div class='form-group'>" +
                        "<label for='selected-complain-content '>Complaint content:</label>" +
                        "<textarea class='form-control ' name='complain-content' rows='4'" +
                        "placeholder='Content'" +
                        "id='selected-complain-content'" +
                        "maxlength='240'" +
                        "resize='none' readonly>" + ((complaint.content !=null)?(complaint.content):(""))+
                        "</textarea>" +
                        "</div>" +
                        "</div>" +
                        "<div class='row'>" +
                        "<div class='form-group'>" +
                        "<label for='omplain-response'>Complaint Responce:</label>" +
                        "<textarea class='form-control' name='complain-response' rows='4'" +
                        "placeholder='Responce'" +
                        "id='selected-complain-responce'" +
                        "maxlength='240'" +
                        "resize='none' readonly>" +((complaint.response != null)?(complaint.response):(""))+"</textarea>" +
                        "</div>" +
                        "</div>" +
                        "</form>" +
                        " </td>" +
                        "</tr>";
                    complaints.append($(html));


                }
                prevComplaintPage = $("#complaint-btn-prev");
                nextComplaintPage = $("#complaint-btn-next");
                prevComplaintPage.attr("disabled", "disabled");
                nextComplaintPage.attr("disabled", "disabled");
                if (complaintListCurrentPage > 0) {
                    prevComplaintPage.removeAttr("disabled");
                }
                if (data.length > complaintListSize) {
                    nextComplaintPage.removeAttr("disabled");
                }

            })
        },
        error: function (data) {
            console.error("Failed to load instance");
            alertError(data)
        }
    });

}

function selectComplaintRow(complainId) {
    complaints = $("#instance-complaints-table");
    rowId = 'table-order-content-' + complainId;
    row = $("#" + rowId);
    if(row.hasClass("hidden")){
        complaints.find(".complaint-content-row").addClass("hidden");
        row.removeClass("hidden");
    } else{
        complaints.find(".complaint-content-row").addClass("hidden");
    }


}

function selectComplaintsTab() {
    $("#orders-tab").removeClass("active");
    $("#conplaints-tab").removeClass("active");
    $("#conplaints-tab").addClass("active");
    $("#instanse-orders").addClass("hidden");
    $("#instance-complaints").removeClass("hidden");
    loadComplaints();
}
function selectOrdersTab() {
    $("#orders-tab").removeClass("active");
    $("#conplaints-tab").removeClass("active");
    $("#orders-tab").addClass("active");
    $("#instance-complaints").addClass("hidden");
    $("#instanse-orders").removeClass("hidden");

}

function alertError(msg) {
    container = $("#alerts-bar");
    alert = document.createElement("div");
    alert.className = "alert alert-danger alert-dismissable";
    alert.appendChild(document.createTextNode(msg));
    ref = document.createElement("a");
    ref.appendChild(document.createTextNode("X"));
    ref.href = "#";
    ref.className = "close";
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
    ref.className = "close";
    ref.setAttribute("data-dismiss", "alert");
    ref.setAttribute("aria-label", "close");
    alert.appendChild(ref);
    container.append(alert);
}

function unlockCreateButton() {
    if ($("#new-complaint-subject").val().length < MIN_SUBJECT_LENGTH) {
        $("#create-new-complaint-from-modal-btn").attr("disabled", "disabled");
    } else {
        $("#create-new-complaint-from-modal-btn").removeAttr("disabled");
    }
}

$(document).ready(function () {

    $.ajax({
        url: "/api/user/account",
        success: function (data) {
            currentUserId = data.userId;
            loadInfo();
        },
        error: function () {
            alertError("Internal server error!")

        }
    });

});