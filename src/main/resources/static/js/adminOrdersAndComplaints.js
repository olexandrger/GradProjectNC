/**
 * Created by DeniG on 23.05.2017.
 */

var ordersListCurrentPage = 0;
var ordersListSize = 10;
var selectedOrder = -1;

var complaintListCurrentPage = 0;
var complaintListSize = 10;
var selectedComplain = -1;

var complaintsData;
var ordersData;

const COMPLAINT_STATUS_CREATED = 5;
const COMPLAINT_STATUS_UNDER_CONSIDERATION = 6;
const COMPLAINT_STATUS_CONSIDERATION_COMPLETED = 7;

const ORDER_STATUS_CREATED = 1;
const ORDER_STATUS_IN_PROGRESS = 2;
const ORDER_STATUS_CANCELLED = 3;
const ORDER_STATUS_COMPLETED = 4;

function getPrevOrderPage() {
    if (ordersListCurrentPage > 0) {
        ordersListCurrentPage -= 1;
    }
    selectedOrder = -1;
    loadOrders();
}
function getNextOrderPage() {
    ordersListCurrentPage++;
    selectedOrder = -1;
    loadOrders();
}
function getPrevComplaintPage() {
    if (complaintListCurrentPage > 0) {
        complaintListCurrentPage -= 1;
    }
    selectedComplain = -1;
    loadComplaints();
}
function getNextComplaintPage() {
    complaintListCurrentPage++;
    selectedComplain = -1;
    loadComplaints();
}

function selectComplaintsTab() {
    $("#complaints-tab").addClass("active");
    $("#orders-tab").removeClass("active");

    $("#orders-area").addClass("hidden");
    $("#complaint-area").removeClass("hidden");


}
function selectOrdersTab() {
    $("#orders-tab").addClass("active");
    $("#complaints-tab").removeClass("active");

    $("#complaint-area").addClass("hidden");
    $("#orders-area").removeClass("hidden");


}

function loadOrders() {
    $.ajax({
        url: "/api/admin/orders/get/all/size/" + (ordersListSize + 1) + "/offset/" + ordersListCurrentPage * ordersListSize,
        success: function (data) {
            var list = $("#admin-order-list");
            list.empty();
            ordersData = data;
            data.forEach(function (item, i) {
                if (i < ordersListSize) {
                    var ref = document.createElement("a");
                    var orderName = item.orderAim + " " + item.productName + " #" + item.productOrderId;
                    ref.appendChild(document.createTextNode(orderName));
                    var span = document.createElement("span");
                    span.className = "label orders-list ";
                    span.className += getLabelName(item.statusId);
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

            var prevPage = $("#order-btn-prev");
            var nextPage = $("#order-btn-next");
            nextPage.attr("disabled", "disabled");
            prevPage.attr("disabled", "disabled");
            if (ordersListCurrentPage > 0) {
                prevPage.removeAttr("disabled");
            }
            if (data.length > ordersListSize) {
                nextPage.removeAttr("disabled");
            }

            selectOrder(selectedOrder);
        },
        error: function () {
            console.error("Cannot load list product types");
        }
    });
}

function loadComplaints() {
    $.ajax({
        url: "/api/admin/complaint/get/all/size/" + (complaintListSize + 1) + "/offset/" + complaintListSize * complaintListCurrentPage,
        success: function (data) {
            $("#no-complain-selected-alert").removeAttr("hidden");
            list = $("#complain-list");
            list.empty();
            complaintsData = data;
            data.forEach(function (item, i) {
                if (i < complaintListSize) {
                    ref = document.createElement("a");
                    complaintName = item.userName + ": " + item.complainReason;
                    ref.appendChild(document.createTextNode("#" + item.complainId + ": " + item.userName + ": "));
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
            nextPage.attr("disabled", "disabled");
            prevPage.attr("disabled", "disabled");
            if (complaintListCurrentPage > 0) {
                prevPage.removeAttr("disabled");
            }
            if (data.length > complaintListSize) {
                nextPage.removeAttr("disabled");
            }
            selectComplaint(selectedComplain);
        },
        error: function () {
            console.error("Cannot load list complaints");
            alertError("Internal server error!")
        }
    });
}

function selectOrder(index) {
    clearOfderForm();
    if (index == -1) {
        $("#order-content-panel").addClass("hidden");
    } else {
        $("#order-content-panel").removeClass("hidden");
    }

    selectedOrder = index;

    var list = $("#admin-order-list");

    list.find("a").removeClass("active");
    list.find("a:nth-child(" + (index + 1) + ")").addClass("active");

    if (selectedOrder != -1) {

        $("#selected-order-user-name").val(ordersData[selectedOrder].userName);
        $("#selected-order-domain").val(ordersData[selectedOrder].domain);
        $("#selected-order-product").val(ordersData[selectedOrder].productName);
        $("#selected-order-aim").val(ordersData[selectedOrder].orderAim);
        $("#selected-order-status").val(ordersData[selectedOrder].status);
        $("#selected-order-start-date").val(moment.unix(ordersData[selectedOrder].openDate).format("LLL"));
        if (ordersData[selectedOrder].closeDate != null) {
            $("#selected-order-end-date").val(moment.unix(ordersData[selectedOrder].closeDate).format("LLL"));
        } else {
            $("#selected-order-end-date").val("");
        }
        if (ordersData[selectedOrder].responsibleEmail != null) {
            responsibles = $("#selected-order-responsible-name");
            option = document.createElement("option");
            option.setAttribute("selected", "selected");
            option.setAttribute("value", ordersData[selectedOrder].responsiblId);
            option.appendChild(document.createTextNode(ordersData[selectedOrder].responsibleEmail));
            responsibles.append(option);
        }
        addOrderControlButtons();
    }
}

function clearOfderForm() {
    $("#selected-order-user-name").val("");
    $("#selected-order-domain").val("");
    $("#selected-order-product").val("");
    $("#selected-order-aim").val("");
    $("#selected-order-status").val("");
    $("#selected-order-start-date").val("");
    $("#selected-order-end-date").val("");
    $("#selected-order-responsible-name").empty();
}

function selectComplaint(index) {
    list = $("#complain-list");
    $("#responsible-info-row").addClass("hidden");
    $("#instance-info-row").addClass("hidden");
    selectedComplain = index;
    if (selectedComplain == -1) {
        $("#no-complain-selected-alert").removeClass("hidden");
        $("#complaint-content-panel").addClass("hidden");
        list.find("a").removeClass("active");
    } else {
        $("#no-complain-selected-alert").addClass("hidden");
        $("#complaint-content-panel").removeClass("hidden");
        list.find("a").removeClass("active");
        list.find("a:nth-child(" + (selectedComplain + 1) + ")").addClass("active");
    }
    if (selectedComplain != -1) {
        $("#complain-user-email").val(complaintsData[selectedComplain].userEmail);
        userDetails = document.createTextNode(complaintsData[selectedComplain].userName + ", Phpne: " + complaintsData[selectedComplain].userNumber);
        $("#complain-user-details").empty();
        $("#complain-user-details").append(userDetails);
        responsibles = $("#complain-responsible-email");
        responsibles.attr("disabled", "disabled");
        responsibles.empty();
        if (complaintsData[selectedComplain].responsibleEmail != null) {
            option = document.createElement("option");
            option.setAttribute("selected", "selected");
            option.setAttribute("value", complaintsData[selectedComplain].responsiblId);
            option.appendChild(document.createTextNode(complaintsData[selectedComplain].responsibleEmail));
            responsibles.append(option);
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
        }
        $("#selected-complain-title").val(complaintsData[selectedComplain].complainTitle);
        $("#selected-complain-content").val(complaintsData[selectedComplain].content);
        $("#selected-complain-responce").val(complaintsData[selectedComplain].response);
        addComplaintControlButtons();
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

function addOrderControlButtons() {
    editResponsibleBtn = $("#edit-order-responsible-btn");
    assignResponsibleBtn = $("#assign-order-responsible-btn");
    editResponsibleBtn.addClass("hidden");
    assignResponsibleBtn.addClass("hidden");
    if (selectedOrder >= 0) {
        if (ordersData[selectedOrder].statusId == ORDER_STATUS_IN_PROGRESS) {
            editResponsibleBtn.removeClass("hidden");
        }
    }
}

function addComplaintControlButtons() {
    editResponsibleBtn = $("#edit-conplaint-responsible-btn");
    assignResponsibleBtn = $("#assign-complaint-responsible-btn");
    editResponsibleBtn.addClass("hidden");
    assignResponsibleBtn.addClass("hidden");
    if (selectedComplain >= 0) {
        if (complaintsData[selectedComplain].statusId == COMPLAINT_STATUS_UNDER_CONSIDERATION) {
            editResponsibleBtn.removeClass("hidden");
        }
    }
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

function editOrderResponsible() {

    $.ajax({
        url: "/api/admin/users/find/csr/",
        success: function (data) {
            list = $("#selected-order-responsible-name");
            list.empty();
            data.forEach(function (item, i) {
                option = document.createElement("option");
                if (item.userId == ordersData[selectedOrder].responsibleId) {
                    option.setAttribute("selected", "selected");
                }

                option.setAttribute("value", item.userId);
                text = item.firstName + " " + item.lastName + " <" + item.email + ">";
                option.appendChild(document.createTextNode(text));
                list.append(option);
            });
            editResponsibleBtn = $("#edit-order-responsible-btn");
            assignResponsibleBtn = $("#assign-order-responsible-btn");
            editResponsibleBtn.addClass("hidden");
            assignResponsibleBtn.removeClass("hidden");
            list.removeAttr("disabled");
        },
        error: function () {
            alertError("internal servrt error!")
        }
    });
}

function assignOrderResponsible() {

    $.ajax({
        url: "/api/admin/orders/set/responsible/",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        contentType: 'application/json',
        data: JSON.stringify({
            orderId:ordersData[selectedOrder].productOrderId,
            responsibleId:$("#selected-order-responsible-name").val()
        }),
        success: function (data) {
            editResponsibleBtn = $("#edit-order-responsible-btn");
            assignResponsibleBtn = $("#assign-order-responsible-btn");
            responsibles = $("#selected-order-responsible-name");
            responsibles.attr("disabled", "disabled");
            editResponsibleBtn.removeClass("hidden");
            assignResponsibleBtn.addClass("hidden");
            loadOrders();
            if(data.status=="success"){
                alertSuccess(data.message);
            } else {
                alertError(data.message);
            }
        },
        error: function () {
            alertError("Inrenal server error!");
        }
    });


}

function editComplaintResponsible() {
    $.ajax({
        url: "/api/admin/users/find/pmg/",
        success: function (data) {
            list = $("#complain-responsible-email");
            list.empty();
            data.forEach(function (item, i) {
                option = document.createElement("option");
                if (item.userId == complaintsData[selectedComplain].responsiblId) {
                    option.setAttribute("selected", "selected");
                }
                option.setAttribute("value", item.userId);
                text = item.firstName + " " + item.lastName + " <" + item.email + ">";
                option.appendChild(document.createTextNode(text));
                list.append(option);
            });

            editResponsibleBtn = $("#edit-conplaint-responsible-btn");
            assignResponsibleBtn = $("#assign-complaint-responsible-btn");
            editResponsibleBtn.addClass("hidden");
            assignResponsibleBtn.removeClass("hidden");
            list.removeAttr("disabled");
            $("#responsible-info-row").removeClass("hidden");
            $("#complain-responsible-details").empty();
        },
        error: function () {
            alertError("internal servrt error!")
        }
    });
}

function assignComplaintResponsible() {
    $.ajax({
        url: "/api/admin/complaint/set/responsible/",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        contentType: 'application/json',
        data: JSON.stringify({
            complaintId:complaintsData[selectedComplain].complainId,
            responsibleId:$("#complain-responsible-email").val()
        }),
        success: function (data) {
            editResponsibleBtn = $("#edit-conplaint-responsible-btn");
            assignResponsibleBtn = $("#assign-complaint-responsible-btn");
            editResponsibleBtn.removeClass("hidden");
            assignResponsibleBtn.addClass("hidden");
            loadComplaints();
            if(data.status=="success"){
                alertSuccess(data.message);
            } else {
                alertError(data.message);
            }
            },
        error: function () {
            alertError("Inrenal server error!");
        }
    });

}

$(document).ready(function () {
        selectOrdersTab();
        loadOrders();
        loadComplaints();
    }
);


