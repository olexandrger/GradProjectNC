/**
 * Created by Alex on 5/23/2017.
 */
var currentUserId;
var instanceId;
var complaintListSize = 5;
var complaintListCurrentPage = 0;
var  instances = [];
var currentSelected;

const MIN_SUBJECT_LENGTH = 5;
const CATEGORY_TYPE_COMPLAIN_REASON = 5;

const ordersPageSize = 5;
var ordersPageNumber = 0;

function suspendInstance() {

    $.ajax({
        url: "/api/csr/orders/new/suspend",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        processData: false,
        contentType: 'application/json',
        data: JSON.stringify({instanceId: instanceId, userId: currentUserId}),
        success: function (data) {
            //loadInstance();
            selectInstance(currentSelected);

        },
        error: function (data) {
            //loadInstance();
            selectInstance(currentSelected);
        }
    });
}

function continueInstance() {

    $.ajax({
        url: "/api/csr/orders/new/activate",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        processData: false,
        contentType: 'application/json',
        data: JSON.stringify({instanceId: instanceId, userId: currentUserId}),
        success: function (data) {
            //loadInstance();
            selectInstance(currentSelected);
        },
        error: function (data) {
            //loadInstance();
            selectInstance(currentSelected);
        }
    });
}

function deactivateInstance() {

    $.ajax({
        url: "/api/csr/orders/new/deactivate",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        processData: false,
        contentType: 'application/json',
        data: JSON.stringify({instanceId: instanceId, userId: currentUserId}),
        success: function (data) {
            //loadInstance();
            selectInstance(currentSelected);
        },
        error: function (data) {
            //loadInstance();
            selectInstance(currentSelected);
        }
    });
}


function cancelOrder(orderId) {
    $.ajax({
        url: "/api/csr/orders/" + orderId + "/cancel",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        success: function (data) {
            //loadInstance();
            selectInstance(currentSelected);
        },
        error: function (data) {
            //loadInstance();
            selectInstance(currentSelected);
        }
    });
}

function loadInfo(id) {
    instanceId =id;
    loadInstance();
    loadOrders();
    loadComplaints();
}

function loadAllInstances() {

    $.ajax({
        url: "/api/csr/instances/find/all",
        success: function(data) {

            var list = $("#instances-list");
            //list.empty();

            instances = data;
            data.forEach(function(item, i) {

                    console.log(item);
                    var ref = document.createElement("a");
                    var orderName = item.product.productName +" for "+ item.domain.domainName;
                    ref.appendChild(document.createTextNode(orderName));
                    ref.className = "list-group-item";
                    ref.href = "#";
                    ref.onclick = function () {
                        selectInstance(i);
                    };
                    list.append(ref);

            });


        },
        error: function () {
            console.error("Cannot load list of users");
        }
    });

}

function selectInstance(i) {
    instanceId = instances[i].instanceId;

    //clearDataFields();
    console.log(JSON.stringify(instances[i].instanceId));
    currentSelected = i;
    loadInfo(instances[i].instanceId)

    var ordersTable = $("#instance-orders-table");
    ordersTable.find(".order-row").remove();
    ordersPageNumber = 0;


}

const INSTANCE_STATUS_ACTIVATED = "ACTIVATED";
const INSTANCE_STATUS_SUSPENDED = "SUSPENDED";
const INSTANCE_STATUS_DEACTIVATED = "DEACTIVATED";
const INSTANCE_STATUS_CREATED = "CREATED";

function loadInstance() {
    $.ajax({
        url: "/api/csr/instances/get/byId/" + instanceId,
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


        },
        error: function (data) {
            console.error("Failed to load instance");
            console.log(data);
        }
    });
}

function loadMoreOrders() {
    ordersPageNumber++;
    loadOrders();
}

function loadOrders() {

    var ordersTable = $("#instance-orders-table");
    $.ajax({
        url: "/api/csr/orders/get/byInstance/" + instanceId + "/size/" + (ordersPageSize + 1) + "/offset/" + (ordersPageNumber * ordersPageSize) ,
        success: function (data) {

            // ordersTable.find(".order-row").remove();
            if (data.length <= ordersPageSize) {
                $("#more-orders").addClass("hidden");
            } else {
                $("#more-orders").removeClass("hidden");
            }

            data.forEach(function (order) {
                var cancelId = order.productOrderId;
                var cancelButton = '<button class="btn pull-right" onclick="cancelOrder(' + cancelId + ')">Cancel</button>';

                console.log(JSON.stringify(order.status));

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
        url: "/api/csr/instances/complains/get/byInstance/" + instanceId + "/size/" + (complaintListSize + 1) + "/offset/" + complaintListCurrentPage * complaintListSize + "/",
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
                        "resize='none' readonly>" + complaint.content+
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
                        "resize='none' readonly>" +complaint.response+"</textarea>" +
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

    loadAllInstances();
    $.ajax({
        url: "/api/user/account",
        success: function (data) {
            currentUserId = data.userId;
            //loadInfo();
        },
        error: function () {
            alertError("Internal server error!")

        }
    });

});