var currentUserId;
var instanceId;
var selectedComplain = -1;
var complaintListSize = 5;
var complaintListCurrentPage = 0;

function buttonsToDown() {
    $('.pull-down').each(function () {
        var $this = $(this);
        $this.css('margin-top', $this.parent().height() - $this.height())
    });
}

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

function loadInstance() {
    path = window.location.pathname.split("/");
    instanceId = path[path.length - 1];
    loadOrders();
    loadComplaints();
}

function loadOrders() {
    $.ajax({
        url: "/api/client/instance/get/byId/" + instanceId,
        success: function (data) {
            instanceId = data.instanceId;
            $("#instance-product-name").html(data.product.productName);
            $("#instance-status").html(data.status.categoryName);
            $("#instance-product-type").html(data.product.productType.productTypeName);
            var address = data.domain.address;
            $("#instance-address").html(
                address.street + ", " + address.building +
                (address.apartment == undefined ? "" : " " + address.apartment)
                + "<br/>" + address.city + ", " + data.price.region.regionName);
            $("#instance-product-price").html(data.price.price);

            $("#instance-complain-button").removeClass("hidden");
            $("#instance-suspend-button").addClass("hidden");
            $("#instance-continue-button").addClass("hidden");
            $("#instance-deactivate-button").addClass("hidden");
            // if (data.status.categoryName == "CREATED") {
            if (data.status.categoryName == "ACTIVATED") {
                $("#instance-suspend-button").removeClass("hidden");
                $("#instance-continue-button").addClass("hidden");
                $("#instance-deactivate-button").removeClass("hidden");
            } else if (data.status.categoryName == "SUSPENDED") {
                $("#instance-suspend-button").addClass("hidden");
                $("#instance-continue-button").removeClass("hidden");
                $("#instance-deactivate-button").addClass("hidden");
                // }  else if (data.status.categoryName == "DEACTIVATED") {
                //     $("#instance-suspend-button").addClass("hidden");
                //     $("#instance-continue-button").addClass("hidden");
                //     $("#instance-deactivate-button").addClass("hidden");
            } else {
                // console.error("Unknown instasnce status: " + data.status.categoryName);
            }

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
        },
        error: function (data) {
            console.error("Failed to load instance");
            console.log(data);
        }
    });
}

function loadNewComplaintModal() {
    reasons = $("#new-complaint-reason");
    reasons.attr("disabled", "disabled");
    reasons.empty();
}

function createComplaint() {
//TODO
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
    $.ajax({
        url: "/api/client/complaints/get/byInstance/" + instanceId + "/size/" + (complaintListSize + 1) + "/offset/" + complaintListCurrentPage + "/",
        success: function (data) {
            data.forEach(function (complaint, i) {
                if (i < complaintListSize) {
                    html = "<tr class='complaint-row' id='table-order-" + complaint.complainId + "'>" +
                        "<td class='col-sm-2'>" + complaint.complainReason + "</td>" +
                        "<td class='col-sm-2 order-status'>" + complaint.status + "</td>" +
                        "<td class='col-sm-2'>" + moment.unix(complaint.openDate).format("LLL") + "</td>" +
                        "<td class='col-sm-2 close-date'>" +
                        (complaint.closeDate == null ? "" : moment.unix(complaint.closeDate).format("LLL")) + "</td>";
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

$(document).ready(function () {

    $.ajax({
        url: "/api/user/account",
        success: function (data) {
            currentUserId = data.userId;
            loadInstance();
        },
        error: function () {
            alertError("Internal server error!")

        }
    });

});