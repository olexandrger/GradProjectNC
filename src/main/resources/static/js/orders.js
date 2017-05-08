var ordersListSize = 10;
var ordersListCurrentPage = 0;

var ordersData;

var selectedOrder;

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
            orderId: ordersData[selectedOrder].productOrderId,
            productId: productId,
            domainId: domainId
        }),
        success: function(data) {
            if (data.status == "success") {
                ordersData[selectedOrder].productId = productId;
                ordersData[selectedOrder].domainId = domainId;

                orderSuccessMessage(data.message);
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

function cancelSelectedOrder() {
    var selectedId = selectedOrder;

    $.ajax({
        url: "/api/csr/orders/" + ordersData[selectedId].productOrderId + "/cancel",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        success: function (data) {
            if (data.status == "success") {
                orderSuccessMessage(data.message);
                ordersData[selectedId].status = "CANCELLED";

                if (selectedOrder == selectedId) {
                    selectOrder(selectedOrder);
                }
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

function startSelectedOrder() {
    var selectedId = selectedOrder;
    $.ajax({
        url: "/api/csr/orders/" + ordersData[selectedOrder].productOrderId + "/start",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        success: function (data) {
            if (data.status == "success") {
                orderSuccessMessage(data.message);
                ordersData[selectedId].status = "IN_PROGRESS";

                if (selectedOrder == selectedId) {
                    selectOrder(selectedOrder);
                }
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
    var selectedId = selectedOrder;
    $.ajax({
        url: "/api/csr/orders/" + ordersData[selectedOrder].productOrderId + "/complete",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        success: function (data) {
            if (data.status == "success") {
                orderSuccessMessage(data.message);
                ordersData[selectedId].status = "COMPLETED";

                if (selectedOrder == selectedId) {
                    selectOrder(selectedOrder);
                }
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

function selectOrder(index) {
    if (index == -1) {
        $("#order-main").addClass("hidden");
    } else {
        $("#order-main").removeClass("hidden");
    }

    selectedOrder = index;

    var list = $("#csr-orders-list");

    list.find("a").removeClass("active");
    list.find("a:nth-child(" + (index+1) + ")").addClass("active");

    if (selectedOrder != -1) {
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

        if (ordersData[selectedOrder].orderAim == "CREATE" && ordersData[selectedOrder].status == "CREATED") {
            domainSelector.prop( "disabled", false);
            productSelector.prop( "disabled", false);
        } else {
            domainSelector.prop("disabled", true);
            productSelector.prop("disabled", true);
        }

        domainSelector.empty();
        for (var domainId in ordersData[selectedOrder].possibleDomains) {
            var domainHtml = "<option value='" + domainId + "' " +
                                (domainId == ordersData[selectedOrder].domainId ? "selected" : "") + " >" +
                                ordersData[selectedOrder].possibleDomains[domainId] +
                            "</option>";
            domainSelector.append($(domainHtml));
        }

        productSelector.empty();
        for (var productId in ordersData[selectedOrder].possibleProducts) {

            var productHtml = "<option value='" + productId + "' " +
                                (productId == ordersData[selectedOrder].productId ? "selected" : "") + " >" +
                                ordersData[selectedOrder].possibleProducts[productId] +
                            "</option>";
            productSelector.append($(productHtml));
        }

        $(".order-button").addClass("hidden");

        if (ordersData[selectedOrder].status == "CREATED") {
            $("#order-button-start").removeClass("hidden");
            $("#order-button-cancel").removeClass("hidden");

            if (ordersData[selectedOrder].orderAim == "CREATE") {
                $("#order-button-update").removeClass("hidden");
            }
        }
        if (ordersData[selectedOrder].status == "IN_PROGRESS") {
            $("#order-button-complete").removeClass("hidden");
            $("#order-button-cancel").removeClass("hidden");
        }

    }
}

function loadOrders() {
    $.ajax({
        url: "/api/csr/orders/get/all/size/" + ordersListSize + "/offset/" + ordersListCurrentPage * ordersListSize,
        success: function (data) {
            var list = $("#csr-orders-list");
            list.empty();

            ordersData = data;

            data.forEach(function (item, i) {
                var ref = document.createElement("a");
                ref.appendChild(document.createTextNode("Order #" + item.productOrderId));
                ref.className = "list-group-item";
                ref.href = "#";
                ref.onclick = function () {
                    selectOrder(i);
                };

                list.append(ref);
            });

            selectOrder(-1);
        },
        error: function () {
            console.error("Cannot load list product types");
        }
    });
}

$(document).ready(function() {
    loadOrders();
});