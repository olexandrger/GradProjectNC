
function buttonsToDown() {
    $('.pull-down').each(function() {
        var $this = $(this);
        $this.css('margin-top', $this.parent().height() - $this.height())
    });
}

function suspendInstance() {
    $.ajax({
        url: "/api/client/orders/new/suspend",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        processData: false,
        contentType: 'application/json',
        data: JSON.stringify({instanceId: JSON.parse(window.name).selectedInstanceId}),
        success: function (data) {
            loadInstance();
        },
        error: function (data) {
            loadInstance();
        }
    });
}

function continueInstance() {
    $.ajax({
        url: "/api/client/orders/new/continue",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        processData: false,
        contentType: 'application/json',
        data: JSON.stringify({instanceId: JSON.parse(window.name).selectedInstanceId}),
        success: function (data) {
            loadInstance();
        },
        error: function (data) {
            loadInstance();
        }
    });
}

function deactivateInstance() {
    $.ajax({
        url: "/api/client/orders/new/deactivate",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        processData: false,
        contentType: 'application/json',
        data: JSON.stringify({instanceId: JSON.parse(window.name).selectedInstanceId}),
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
    var instanceId = JSON.parse(window.name).selectedInstanceId;

    $.ajax({
        url: "/api/client/instance/get/byId/" + instanceId,
        success: function (data) {
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
                console.error("Unknown instasnce status: " + data.status.categoryName);
            }

            var ordersTable = $("#instance-orders-table");
            ordersTable.find(".order-row").remove();
            data.productOrders.forEach(function(order) {
                var cancelButton = '<button class="btn pull-right" onclick="cancelOrder(' + order.productOrderId + ')">Cancel</button>';

                var html = "<tr class='order-row' id='table-order-" + order.productOrderId + "'>" +
                    "<td class='col-sm-2'>" + order.orderAim + "</td>" +
                    "<td class='col-sm-2 order-status'>" + order.status + "</td>" +
                    "<td class='col-sm-2'>" + moment.unix(order.openDate).format("LLL")+ "</td>" +
                    "<td class='col-sm-2 close-date'>" + (order.closeDate == null ? "" : moment.unix(order.closeDate).format("LLL"))+ "</td>" +
                    "<td class='col-sm-2 cancel-button'>" + (order.status == "CREATED" ? cancelButton : "")+ "</td>" +
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

$(document).ready(function () {
    loadInstance();
});