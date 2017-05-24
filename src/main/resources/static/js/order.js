var orderData;

const ORDER_STATUS_CREATED = "CREATED";
const ORDER_STATUS_IN_PROGRESS = "IN_PROGRESS";
const ORDER_STATUS_CANCELLED = "CANCELLED";
const ORDER_STATUS_COMPLETED = "COMPLETED";

const ORDER_AIM_CREATE = "CREATE";

var currentUserId;

function orderErrorMessage(message) {
    var alert = $('<div id="order-alert" class="alert alert-danger" role="alert">' +
        '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
        message + '</div>');


    $("#order-alert").remove();
    alert.insertAfter($("#csr-order-alert-place"));
}

function orderSuccessMessage(message) {
    var alert = $('<div id="order-alert" class="alert alert-success" role="alert">' +
        '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
        message + '</div>');


    $("#order-alert").remove();
    alert.insertAfter($("#csr-order-alert-place"));
}

function updateSelectedOrder() {
    var domainId = $("#order-domain").val();
    var productId = $("#order-product").val();

    $.ajax({
        url: "/api/csr/orders/update",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        processData: false,
        contentType: 'application/json',
        data: JSON.stringify({
            orderId: orderData.productOrderId,
            productId: productId,
            domainId: domainId
        }),
        success: function (data) {
            if (data.status == "success") {
                orderData.productId = productId;
                orderData.domainId = domainId;

                orderSuccessMessage(data.message);
            } else {
                orderErrorMessage(data.message);
            }
        },
        error: function (data) {
            orderErrorMessage("Request failed");
        }
    });
}

function cancelSelectedOrder() {
    $.ajax({
        url: "/api/csr/orders/" + orderData.productOrderId + "/cancel",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        success: function (data) {
            if (data.status == "success") {
                orderSuccessMessage(data.message);
                orderData.status = ORDER_STATUS_CANCELLED;
                orderData.closeDate = Date.now() / 1000;

                loadOrder();
            } else {
                orderErrorMessage(data.message);
            }
        },
        error: function (data) {
            orderErrorMessage("Request failed");
        }
    });
}

function startSelectedOrder() {
    $.ajax({
        url: "/api/csr/orders/" + orderData.productOrderId + "/start",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        success: function (data) {
            if (data.status == "success") {
                orderSuccessMessage(data.message);
                orderData.status = ORDER_STATUS_IN_PROGRESS;

                loadOrder();
            } else {
                orderErrorMessage(data.message);
                console.log(data);
            }
        },
        error: function (data) {
            orderErrorMessage("Request failed");
            console.error(data);
        }
    });
}

function completeSelectedOrder() {
    $.ajax({
        url: "/api/csr/orders/" + orderData.productOrderId + "/complete",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        success: function (data) {
            if (data.status == "success") {
                orderSuccessMessage(data.message);
                orderData.status = ORDER_STATUS_CREATED;
                orderData.closeDate = Date.now() / 1000;

                loadOrder();
            } else {
                orderErrorMessage(data.message);
            }
        },
        error: function (data) {
            orderErrorMessage("Request failed");
            console.error(data);
        }
    });
}

function loadOrder() {

    var path = window.location.pathname.split("/");
    var orderId = path[path.length - 1];

    $.ajax({
        url: "/api/csr/orders/get/" + orderId,
        success: function (data) {
            if (data == "") {
                $("#order-header").text("Order not found");
                $("#order-header").removeClass("hidden");
            } else {

                orderData = data;

                $("#order-header").text("Order #" + orderData.productOrderId);
                $("#order-user-name").val(orderData.userName);
                $("#order-aim").val(orderData.orderAim);
                $("#order-status").val(orderData.status);
                $("#order-start-date").val(moment.unix(orderData.openDate).format("LLL"));
                if (orderData.closeDate != null) {
                    $("#order-end-date").val(moment.unix(orderData.closeDate).format("LLL"));
                } else {
                    $("#order-end-date").val("");
                }

                var domainSelector = $('#order-domain');
                var productSelector = $('#order-product');

                if (orderData.orderAim == ORDER_AIM_CREATE && orderData.status == ORDER_STATUS_CREATED) {
                    domainSelector.prop("disabled", false);
                    productSelector.prop("disabled", false);
                } else {
                    domainSelector.prop("disabled", true);
                    productSelector.prop("disabled", true);
                }

                if (orderData.possibleDomains == null) {
                    domainSelector.get(0).parentNode.style.display = "none";
                } else {
                    domainSelector.get(0).parentNode.style.display = "block";
                    domainSelector.empty();
                    for (var domainId in orderData.possibleDomains) {
                        var domainHtml = "<option value='" + domainId + "' " +
                            (domainId == orderData.domainId ? "selected" : "") + " >" +
                            orderData.possibleDomains[domainId] +
                            "</option>";
                        domainSelector.append($(domainHtml));
                    }
                }

                if (orderData.possibleProducts == null) {
                    productSelector.get(0).parentNode.style.display = "none";
                } else {
                    productSelector.get(0).parentNode.style.display = "block";
                    productSelector.empty();
                    for (var productId in orderData.possibleProducts) {

                        var productHtml = "<option value='" + productId + "' " +
                            (productId == orderData.productId ? "selected" : "") + " >" +
                            orderData.possibleProducts[productId] +
                            "</option>";
                        productSelector.append($(productHtml));
                    }
                }

                $(".order-button").addClass("hidden");

                if (!(orderData.responsibleId == null || orderData.responsibleId == currentUserId)) {
                    domainSelector.prop("disabled", true);
                    productSelector.prop("disabled", true);
                } else {
                    if (orderData.status == ORDER_STATUS_CREATED) {
                        $("#order-button-start").removeClass("hidden");
                        $("#order-button-cancel").removeClass("hidden");

                        if (orderData.orderAim == ORDER_AIM_CREATE) {
                            $("#order-button-update").removeClass("hidden");
                        }
                    }
                    if (orderData.status == ORDER_STATUS_IN_PROGRESS) {
                        $("#order-button-complete").removeClass("hidden");
                        $("#order-button-cancel").removeClass("hidden");
                    }
                }

                $("#order-main").removeClass("hidden");
                $("#order-header").removeClass("hidden");
            }
        },
        error: function () {
            console.error("Cannot load list product types");
        }
    });
}

function loadAccountInfo() {
    var _csrf = $('meta[name=_csrf]').attr("content");

    $.ajax({
        type: 'GET',
        url: "/api/user/account",
        headers: {
            'X-CSRF-TOKEN': _csrf
        },
        success: function (data) {

            currentUserId = data.userId;
            loadOrder();
        }
    });
}

$(document).ready(function () {
    loadAccountInfo();
});