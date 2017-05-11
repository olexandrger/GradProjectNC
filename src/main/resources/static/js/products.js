var currentSelected = -1;

var productTypesCache;
var productsCache;

var regionsCache;
var regionsHtml;

function getProductType(productTypeId) {
    return productTypesCache.find(function (productType) {
        return productType.productTypeId == productTypeId;
    });
}

function getProductCharacteristicValue(productCharacteristicId) {
    return productsCache[currentSelected].productCharacteristicValues.find(function (value) {
        return value.productCharacteristicId == productCharacteristicId;
    });
}

function javaToJsDate(date) {
    function addNumber(number) {
        var result = "";
        if (number < 10)
            result += '0';
        result += number;
        return result;
    }
    var value = date[0] + '-' + addNumber(date[1]) + '-' + addNumber(date[2]) + 'T' + addNumber(date[3]) + ':' + addNumber(date[4]) + ':' + addNumber(date[5]);
    return value;
}

function changeCharacteristics() {
    var productType = getProductType($("#product-type-selector").val());
    var list = $("#product-characteristics");
    list.empty();

    productType.productCharacteristics.forEach(function (productCharacteristic) {
        console.log(productCharacteristic);

        var measureHtml = "";
        if (productCharacteristic.measure != undefined && productCharacteristic.measure != "") {
            measureHtml = '<span class="input-group-addon characteristic-measure-span text-left">' +
                productCharacteristic.measure + '</span>';
        }

        var inputType = "text";
        if (productCharacteristic.dataTypeId == 23)
            inputType = "datetime-local";

        var value = "";
        var value_id = null;
        var productCharacteristicValue = getProductCharacteristicValue(productCharacteristic.productCharacteristicId);
        if (productCharacteristicValue != undefined) {
            value_id = productCharacteristicValue.valueId;

            switch (productCharacteristic.dataTypeId) {
                case 22:
                    value = productCharacteristicValue.numberValue;
                    break;
                case 23:
                    value = javaToJsDate(productCharacteristicValue.dateValue);
                    break;
                case 24:
                    value = productCharacteristicValue.stringValue;
                    break
            }
        }

        var html =
            '<div class="product-characteristic-value-input col-sm-12">' +
            '<label>' + productCharacteristic.characteristicName + '</label>' +
            '<div class="input-group col-sm-12">' +
            '<input type="hidden" name="value-id" value="' + value_id + '"/>' +
            '<input type="hidden" name="characteristic-id" value="' + productCharacteristic.productCharacteristicId + '"/>' +
            '<input type="hidden" name="data-type-id" value="' + productCharacteristic.dataTypeId + '"/>' +
            '<input type="' + inputType + '" class="form-control" placeholder="value" value="' + value + '" name="characteristic-value">' +
            measureHtml +
            '</div>' +
            '</div>';

        list.append($(html));
    });
}

function selectProduct(index) {
    var list = $("#products-list");

    if (currentSelected == -1) {
        $("#products-editor").removeClass("hidden");
    } else {
        list.find("a").removeClass("active");
        $("#new-product-alert").remove();
    }
    currentSelected = index;
    list.find("a:nth-child(" + (index + 1) + ")").addClass("active");

    $("#product-name-input").val(productsCache[currentSelected].productName);
    $("#product-characteristics").empty();
    $(".cities-container").empty();
    $("#product-description-input").val("");
    $('input:radio[name=product-status]').filter('[value=true]').prop('checked', true);

    var typeSelector = $("#product-type-selector");
    typeSelector.val([]);
    typeSelector.prop("disabled", false);
    if (productsCache[currentSelected].productId != null) {
        typeSelector.val(productsCache[currentSelected].productTypeId);
        typeSelector.prop("disabled", productsCache[currentSelected].productId);
        changeCharacteristics();
        $("#product-description-input").val(productsCache[currentSelected].productDescription);
        productsCache[currentSelected].prices.forEach(function (prp) {
            console.log(JSON.stringify(prp));
            displayRegionalPrice(prp.priceId, prp.regionId, prp.price);
        });

        $('input:radio[name=product-status]').filter('[value=' + productsCache[currentSelected].isActive + ']')
            .prop('checked', true);
    }
}

function deleteRegionalPrice(element) {
    element.parentNode.parentNode.parentNode.removeChild(element.parentNode.parentNode);
}

function saveSelectedProduct() {
    var newProduct = {};

    newProduct.productId = productsCache[currentSelected].productId;
    newProduct.productName = $("#product-name-input").val();
    newProduct.productTypeId = $("#product-type-selector").val();
    newProduct.productDescription = $("#product-description-input").val();
    newProduct.isActive = ($('input[name=product-status]:checked').val() == 'true');


    newProduct.prices = [];
    $(".cities-container").each(function () {
        if ($(this).find('select[name=regionId]').val() != undefined) {
            console.log("pushing price of region" + $(this).find('select[name=regionId]').val());
            newProduct.prices.push({
                priceId: $(this).find('input[name=priceId]').val(),
                regionId: $(this).find('select[name=regionId]').val(),
                price: $(this).find('input[name=region-price]').val()
            });
        }
    });

    newProduct.productCharacteristicValues = [];
    $("#product-characteristics").find(".product-characteristic-value-input").each(function () {
        var value = {
            valueId: $(this).find('input[name=value-id]').val(),
            productCharacteristicId: $(this).find('input[name=characteristic-id]').val()
        };
        switch (+$(this).find('input[name=data-type-id]').val()) {
            case 22:
                value.numberValue = +$(this).find('input[name=characteristic-value]').val();
                value.stringValue = null;
                value.dateValue = null;
                break;
            case 23:
                value.dateValue = $(this).find('input[name=characteristic-value]').val();
                value.stringValue = null;
                value.numberValue = null;
                break;
            case 24:
                value.stringValue = $(this).find('input[name=characteristic-value]').val();
                value.numberValue = null;
                value.dateValue = null;
                break;
        }
        newProduct.productCharacteristicValues.push(value);

        console.log(JSON.stringify(newProduct));
    });

    $.ajax({
        type: 'POST',
        url: '/api/admin/products/' + (newProduct.productId == null ? "add" : "update"),
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        processData: false,
        contentType: 'application/json',
        data: JSON.stringify(newProduct),
        success: function (data) {
            var alert;
            $("#new-product-alert").remove();
            if (data.status == 'success') {
                console.log("Product update success! " + JSON.stringify(data));

                alert = $('<div class="alert alert-success" role="alert" id="new-product-alert">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                    data.message + "</div>");

                $.ajax({
                    type: 'GET',
                    url: '/api/user/products/get/' + data.id,
                    success: function (data) {
                        // console.log("Update after saving successful");
                        // console.log("Set name " + data.name);
                        $("#products-list").find("a:nth-child(" + (currentSelected + 1) + ")")
                            .html(data.productName);
                        productsCache[currentSelected] = data;
                    },
                    error: function (data) {
                        console.log("Update after saving errored: " + data)
                    }
                });

            } else {
                console.log("Product update error! " + JSON.stringify(data));
                alert = $('<div class="alert alert-danger" role="alert" id="new-product-alert">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                    data.message + '</div>');
            }

            alert.insertAfter($("#new-product-alert-place"));
        },
        error: function (data) {
            $("#new-product-alert").remove();
            console.error("Product update error! " + JSON.stringify(data));
            $('<div id="new-product-alert" class="alert alert-danger" role="alert">' +
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                "Product changes failed</div>").insertAfter($("#new-product-alert-place"));
        }
    });

}

function displayRegionalPrice(priceId, regionId, price) {
    if (regionId == undefined) {
        return;
    }

    var selectedPrice = ' value="' + price + '" ';

    var html =
        '<div class="form-inline cities-container">' +
        '<input type="hidden" value="' + priceId + '" name="priceId">' +
        '<div class="form-group">' +
        '<select class="form-control" name="regionId">' +
        regionsHtml +
        '</select>' +
        '</div>' +
        '<div class="form-group full-width">' +
        '<input type="text" class="form-control full-width" placeholder="Price" name="region-price"' + selectedPrice + '>' +
        '</div>' +
        '<div class="form-group">' +
        '<a class="btn btn-danger" onclick="deleteRegionalPrice(this)">' +
        '<span class="glyphicon glyphicon-remove "></span>' +
        '</a>' +
        '</div>' +
        '</div>';

    var element = $(html);
    $("#product-general-editor").append(element);

    element.find('select').val(regionId);
}

function addRegionalPrice() {
    var html =
        '<div class="form-inline cities-container">' +
        '<input type="hidden" value="' + null + '" name="priceId">' +
        '<div class="form-group">' +
        '<select class="form-control" name="regionId">' +
        regionsHtml +
        '</select>' +
        '</div>' +
        '<div class="form-group full-width">' +
        '<input type="text" class="form-control full-width" placeholder="Price" name="region-price">' +
        '</div>' +
        '<div class="form-group">' +
        '<a class="btn btn-danger" onclick="deleteRegionalPrice(this)">' +
        '<span class="glyphicon glyphicon-remove "></span>' +
        '</a>' +
        '</div>' +
        '</div>';

    var element = $(html);
    $("#product-general-editor").append(element);
}

function addProduct() {
    var list = $("#products-list");
    var productNameField = $("#new-product-name");
    var productName = productNameField.val();
    productNameField.val("");

    if (productName != "") {
        var ref = document.createElement("a");
        ref.appendChild(document.createTextNode(productName));
        ref.className = "list-group-item";
        ref.href = "#";
        var index = productsCache.length;
        ref.onclick = function () {
            selectProduct(index);
        };
        list.append(ref);

        productsCache.push({
            productId: null,
            productName: productName,
            productCharacteristicValues: [],
            prices: []
        });

        selectProduct(index);
    }
    else {
        $("#new-product-alert").remove();

        $('<div id="new-product-alert" class="alert alert-danger" role="alert">' +
            '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
            'Cannot add product with empty name</div>').insertAfter(list);
    }
}

function displayLoadedProducts() {
    productsCache.forEach(function (product, index) {
        var list = $("#products-list");

        var ref = document.createElement("a");
        ref.appendChild(document.createTextNode(product.productName));
        ref.className = "list-group-item";
        ref.href = "#";
        ref.onclick = function () {
            selectProduct(index);
        };

        list.append(ref);
    });
}

function loadProducts() {
    console.log("loadProducts");
    $.ajax({
        url: "/api/user/products/all",
        success: function (products) {
            productsCache = products;

            displayLoadedProducts();
        },
        error: function () {
            console.error("Cannot load products");
        }
    });
}

function loadProductTypes() {
    console.log("loadProductTypes");
    $.ajax({
        url: "/api/user/productTypes/all",
        success: function (productTypes) {
            var sel = $("#product-type-selector");

            productTypes.forEach(function (productType) {
                var option = document.createElement("option");
                option.text = productType.productTypeName;
                option.value = productType.productTypeId;
                sel.append(option);
            });

            productTypesCache = productTypes;

            loadProducts();
        },
        error: function () {
            console.error("Cannot load product types");
        }
    });
}

function loadRegions() {
    $.ajax({
        url: "/api/user/regions/all",
        success: function (regions) {

            regionsCache = regions;

            regionsHtml = "";
            regions.forEach(function (region) {
                regionsHtml += '<option value="' + region.regionId + '">' + region.regionName + '</option>'
            });
            loadProductTypes();
        },
        error: function () {
            console.error("Cannot load list of regions");
        }
    });
}

$(document).ready(function () {
    loadRegions();
});