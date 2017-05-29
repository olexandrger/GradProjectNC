var catalogProductsCache;

var currentProductTypeId;
var currentSelected = -1;

//pagination global variables
var startPage = 1;
var defaultAmount = 10;
var defaultOpts = { //twbs pagination default options
    visiblePages : 7,
    initiateStartPageClick: false,
    hideOnlyOnePage : true,
    onPageClick: function (event, page) {
        console.log('clicked page #' + page);
        loadCatalogPageOfType({page: page});
    }
};
var $pagination;

var catalogDomains;

function getValue(valueObj, dataType) {
    switch (dataType) {
        case 'NUMBER':
            return valueObj.numberValue;
        case 'STRING':
            return valueObj.stringValue;
        case 'DATE':
            return moment.unix(valueObj.dateValue).format("LLL");
    }
}

function getRegionalPrice(regionId) {
    return catalogProductsCache[currentSelected].prices.find(function(price) {
        return price.regionId == regionId;
    });
}

function selectCatalogProduct(index) {
    var activeLinkIndex = index + 1;

    var catalogProductList = $("#catalog-products-list");
    $("#catalog-main-info").removeClass("hidden");
    catalogProductList.find("a").removeClass("active");
    catalogProductList.find("a:nth-child(" + (activeLinkIndex) + ")").addClass("active");
    currentSelected = index;

    $(".table-row").remove();

    $("#catalog-product-name").text(catalogProductsCache[index].productName);
    $("#catalog-product-description").text(catalogProductsCache[index].productDescription);

    var table = $("#catalog-table-details");
    catalogProductsCache[index].productCharacteristicValues.forEach(function (item) {
        var html =
            '<tr class="table-row">' +
                '<td>' + item.productCharacteristic.characteristicName + '</td>' +
                '<td>' + getValue(item, item.productCharacteristic.dataType.categoryName) + '</td>' +
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
    var selectedDomainIndex = document.getElementById("catalog-domain-selector").value;
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

function displayCatalogProducts() {
    console.log("Refreshing catalog");
    var catalogProductList = $("#catalog-products-list");
    catalogProductList.empty();
    $("#catalog-main-info").addClass("hidden");

    catalogProductsCache.forEach(function (product, index) {
        var ref = document.createElement("a");
        ref.appendChild(document.createTextNode(product.productName));
        ref.className = "list-group-item";
        ref.href = "#";
        ref.onclick = function () {
            selectCatalogProduct(index);
        };

        catalogProductList.append(ref);
    });
}

function loadCatalogPageOfType(options) {
    var page = options.page != undefined ? options.page : startPage;
    var amount = options.amount != undefined ? options.amount : defaultAmount;
    var productTypeId;
    if (options.productTypeId != undefined) {
        currentProductTypeId = options.productTypeId;
    }
    productTypeId = currentProductTypeId;
    var regionId = options.regionId != undefined ? options.regionId : localStorage.getItem("regionId");

    console.log("loading products of page #" + page);
    $.ajax({
        url: '/api/user/products?productTypeId=' + productTypeId + '&regionId=' + regionId +
        '&page=' + page + '&amount=' + amount,
        success: function (data) {
            catalogProductsCache = data.content;
            displayCatalogProducts();

            updatePaginationWidget(page, data.totalPages);
        },
        error: function () {
            console.error("Cannot load products");
        }
    });
}

function updatePaginationWidget(currentPage, totalPages) {
    console.log('updating pagination widget');
    $pagination.twbsPagination('destroy');
    $pagination.twbsPagination($.extend({}, defaultOpts, {
        startPage: currentPage,
        totalPages: totalPages
    }));
}

function catalogCreateOrder() {
    $('#new-product-order-modal').modal('toggle');
    $('#new-product-order-modal-submit').addClass("disabled");
    $("#catalog-domain-selector").val([]);
    $('#catalog-price-field').val("")
}

$(document).on("account-loaded", loadDomainsData);