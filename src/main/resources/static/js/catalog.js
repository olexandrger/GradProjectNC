var catalogProducts;
var catalogSelectedItem = -1;

var catalogSelectedCategory = decodeURIComponent(window.location.search.substr(1));
console.log(catalogSelectedCategory);
var catalogCharacteristics = {};

function getCharacteristicStringValue(characteristic, dataType) {
    // console.log(characteristic);
    // console.log(dataType);
    //
    // return "FUCK THIS SHIT";

    if (dataType == "NUMBER") {
        return characteristic.numberValue;
    }

    if (dataType == "STRING")
        return characteristic.stringValue;

    if (dataType == "DATE") {
        // return new Date(characteristic[0], characteristic[1], characteristic[2], characteristic[3], characteristic[4], characteristic[5], 0);
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

function getRegionalPrice() {
    var price = "Error while searching for price";

    var selectedRegion = localStorage.getItem("regionId");

    catalogProducts[catalogSelectedItem].prices.forEach(function(item) {
        if (item.region.regionId == selectedRegion)
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
        getRegionalPrice() +
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
    console.log("trigger works");
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

$(document).on("region-changed", loadCatalogData);