console.log("catalog js");

var catalogProductsCache;
var currentSelected = -1;

var catalogSelectedCategory = decodeURIComponent(window.location.search.substr(1));
// var catalogCharacteristics = {};

var catalogDomains;

function getCharacteristicStringValue(characteristic, dataType) {
    if (dataType == "NUMBER") {
        return characteristic.numberValue;
    }

    if (dataType == "STRING")
        return characteristic.stringValue;

    if (dataType == "DATE") {
        return moment.unix(characteristic.dateValue).format("LLL")
    }
}

function getRegionalPrice(regionId) {
    return catalogProductsCache[currentSelected].prices.find(function(price) {
        return price.regionId == regionId;
    });
}

function selectCatalogProduct(index, positionInList) {
    $("#catalog-main-info").removeClass("hidden");

    currentSelected = index;

    var list = $("#catalog-products-list");
    list.find("a").removeClass("active");
    list.find("a:nth-child(" + positionInList + ")").addClass("active");

    $(".table-row").remove();

    $("#catalog-product-name").text(catalogProductsCache[index].productName);
    $("#catalog-product-description").text(catalogProductsCache[index].productDescription);

    var table = $("#catalog-table-details");
    catalogProductsCache[index].productCharacteristicValues.forEach(function (item) {
        var html =
            '<tr class="table-row">' +
                '<td>' + item.productCharacteristic.characteristicName + '</td>' +
                '<td>' + getCharacteristicStringValue(item, item.productCharacteristic.dataType.categoryName) + '</td>' +
                '<td>' + item.productCharacteristic.measure + '</td>' +
            '</tr>';

        table.append($(html));
    });

    var price =  getRegionalPrice(localStorage.getItem("regionId"));
    console.log(JSON.stringify(price));

    var priceHTML =
        '<tr class="table-row final-price-table-row"">' +
            '<td>Price</td>' +
            '<td colspan="2">' + price.price + '</td>' +
        '</tr>';

    table.append($(priceHTML));

    if (price.discount != null) {
        table.find('tr').removeClass('final-price-table-row');

        var discountHTML = '<tr class="table-row">' +
            '<td>Discount : ' + price.discount.discountTitle + '</td>' +
            '<td>' + price.discount.discount + '</td>' +
            '<td><span class="info">%</span></td>' +
            '</tr>';

        table.append($(discountHTML));

        var finalPrice = calculateFinalPrice(price);
        var finalPriceHTML =
            '<tr class="table-row final-price-table-row"">' +
            '<td>Final price</td>' +
            '<td colspan="2">' + finalPrice + '</td>' +
            '</tr>';

        table.append($(finalPriceHTML));
    }
}

function calculateFinalPrice(priceObj) {
    var discountObj = priceObj.discount;
    if (discountObj != null) {
        return priceObj.price - priceObj.price * discountObj.discount / 100;
    }
    return priceObj.price;
}

function updateCatalog() {
    console.log("Updating catalog");
    var list = $("#catalog-products-list");
    list.empty();
    $("#catalog-main-info").addClass("hidden");

    var positionInList = 1;
    catalogProductsCache.forEach(function(item, index) {
        if (item.productTypeId == catalogSelectedCategory) {
            var ref = document.createElement("a");
            ref.appendChild(document.createTextNode(item.productName));
            ref.className = "list-group-item";
            ref.href = "#";
            var position = positionInList++;
            ref.onclick = function () {
                selectCatalogProduct(index, position);
            };
            list.append(ref);
        }
    });
}

function loadCatalogData() {
    console.log("Loading catalog data");
    $.ajax({
        url: "/api/user/products/byRegion/" + localStorage.getItem("regionId"),
        success: function(data) {
            catalogProductsCache = data;
            updateCatalog();
        },
        error: function () {
            console.error("Cannot load list of products");
        }
    });
}

function loadDomainsData() {
    console.log("Loading domains data for catalog");
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
    //TODO display address
    //Maybe this is wright
     var price = getRegionalPrice(catalogDomains[domainId].address.location.region.regionId);

    if (price == null) {
        price = "Product unavailable in this region";
        $('#catalog-price-field').val(price);
        $('#new-product-order-modal-submit').addClass("disabled");
    } else {
        $('#new-product-order-modal-submit').removeClass("disabled");
        $('#catalog-price-field').val(price);
    }

    $('#catalog-price-field').val(calculateFinalPrice(price));
}

function catalogSubmitOrder() {

    //TODO Maybe this is wright
    var selectedDomainIndex =document.getElementById("catalog-domain-selector").value;


    $.ajax({
        url: "/api/client/orders/new/create",
        method: "POST",
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        contentType: 'application/json',
        data: JSON.stringify({
            productId: catalogProductsCache[currentSelected].productId,
            domainId: catalogDomains[selectedDomainIndex].domainId
        }),
        success: function(data) {
            if (data.status == "success") {
                // console.log(data.message);
                // var info = window.name == "" ? {} : JSON.parse(window.name);
                // info.selectedInstanceId = data.instanceId;
                // window.name = JSON.stringify(info);
                window.location.href = "/client/instance/" + data.instanceId;
            } else {
                console.error("Cannot create order: " + data.message);
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