var catalogProducts;
var catalogSelectedItem = -1;

var catalogSelectedCategory = decodeURIComponent(window.location.search.substr(1));
var catalogCharacteristics = {};

var catalogDomains;

function getCharacteristicStringValue(characteristic, dataType) {
    if (dataType == "NUMBER") {
        return characteristic.numberValue;
    }

    if (dataType == "STRING")
        return characteristic.stringValue;

    if (dataType == "DATE") {
        var date = characteristic.dateValue;
        function addNumber(number) {
            var result = "";
            if (number < 10)
                result += '0';
            result += number;
            return result;
        }
        return addNumber(date[3]) + ':' + addNumber(date[4]) + " " + addNumber(date[2]) + '.' + addNumber(date[1]) + "." + date[0];
    }
}

function getRegionalPrice(regionId) {
    var price = "";

    // var selectedRegion = localStorage.getItem("regionId");

    catalogProducts[catalogSelectedItem].prices.forEach(function(item) {
        if (item.region.regionId == regionId)
            price = item.price;
    });

    return price;
}

function selectCatalogProduct(index) {

    $("#catalog-main-info").removeClass("hidden");

    catalogSelectedItem = index;

    var list = $("#catalog-products-list");
    list.find("a").removeClass("active");
    list.find("a:nth-child(" + (index+1) + ")").addClass("active");

    $(".table-row").remove();

    $("#catalog-product-name").text(catalogProducts[index].name);
    $("#catalog-product-description").text(catalogProducts[index].description);

    catalogProducts[index].productCharacteristics.forEach(function (item) {
        catalogCharacteristics[item.productCharacteristicId] = item;
    });

    var table = $("#catalog-table-details");
    catalogProducts[index].productCharacteristicValues.forEach(function (item) {
           var html = '<tr class="table-row"><td>' +
            catalogCharacteristics[item.productCharacteristicId].characteristicName +
            '</td>' +
            '<td>' +
            getCharacteristicStringValue(item, catalogCharacteristics[item.productCharacteristicId].dataType.dataType) + ' ' +
            catalogCharacteristics[item.productCharacteristicId].measure +
            '</td></tr>';

        table.append($(html));
    });

    var html = '<tr class="table-row"><td>Price</td><td>' +
        getRegionalPrice(localStorage.getItem("regionId")) +
        '</td></tr>';
    table.append($(html));
}

function updateCatalog() {
    console.log("Updating catalog");
    var list = $("#catalog-products-list");
    list.empty();
    $("#catalog-main-info").addClass("hidden");

    catalogProducts.forEach(function(item, index) {
        if (item.productType.productTypeName == catalogSelectedCategory) {
            var ref = document.createElement("a");
            ref.appendChild(document.createTextNode(item.name));
            ref.className = "list-group-item";
            ref.href = "#";
            ref.onclick = function () {
                selectCatalogProduct(index);
            };
            list.append(ref);
        }
    });
}

function loadCatalogData() {
    // console.log("trigger works");
    $.ajax({
        url: "/api/user/products/byRegion/" + localStorage.getItem("regionId"),
        success: function(data) {
            catalogProducts = data;
            updateCatalog();
        },
        error: function () {
            console.error("Cannot load list of regions");
        }
    });
}

function loadDomainsData() {
    $.ajax({
        url: "/api/client/domains/get/all",
        success: function(data) {
            var select = $("#catalog-domain-selector");
            select.empty();

            catalogDomains = {};
            data.forEach(function (domain) {
                catalogDomains[domain.domainId] = domain;

                var option = document.createElement("option");
                option.text = domain.domainName;
                option.value = domain.domainId;
                select.append(option);
            });

            //TODO maybe add message
            if (data.length > 0) {
                $("#catalog-new-order-button").removeClass("hidden");
            }
        },
        error: function () {
            console.error("Cannot load list of domains");
        }
    });
}

function catalogChangeDomain(domainId) {
    // console.log(catalogDomains[domainId]);
    // $('#new-product-order-modal-submit').removeClass("disabled");
    //TODO display address

    var price = getRegionalPrice(catalogDomains[domainId].regionId);
    if (price == "") {
        price = "Product unavailable in this region";
        $('#new-product-order-modal-submit').addClass("disabled");
    } else {
        $('#new-product-order-modal-submit').removeClass("disabled");
    }

    // $('#new-product-order-modal-submit').addClass("disabled");
    $('#catalog-price-field').val(price);
}

function catalogSubmitOrder() {

    var selectedDomainIndex = $("#catalog-domain-selector").val()[0];

    console.log("Creating order for product " +
        catalogProducts[catalogSelectedItem].name +
        " at domain " +
        catalogDomains[selectedDomainIndex].domainName);

    $.ajax({
        url: "/api/client/orders/new/create",
        method: "POST",
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        contentType: 'application/json',
        data: JSON.stringify({
            productId: catalogProducts[catalogSelectedItem].productId,
            domainId: catalogDomains[selectedDomainIndex].domainId
        }),
        success: function(data) {
            if (data.status == "success") {
                console.log(data.message);
            } else {
                console.error("Cannot create order: " + data.message);
                // console.error(data);
            }
        },
        error: function (data) {
            console.error("Cannot create order: ");
            console.error(data);
        }
    });
}

function catalogCreateOrder() {
    $('#new-product-order-modal').modal('toggle');
    $('#new-product-order-modal-submit').addClass("disabled");
    $("#catalog-domain-selector").val([]);
    $('#catalog-price-field').val("")
}

$(document).on("account-loaded", loadDomainsData);
$(document).on("region-changed", loadCatalogData);