var currentSelected = -1;

var productTypesCache;
var productsCache;

var regionsHtml;

//pagination global variables
var amount = 10; //amount of items per page
var startPage = 1;
var defaultOpts = { //twbs pagination default options
    visiblePages : 3,
    initiateStartPageClick: false,
    hideOnlyOnePage : true,
    first: '<<',
    last: '>>',
    next: '>',
    prev: '<',
    onPageClick: function (event, page) {
        console.log('clicked page #' + page);
        loadProductPage(page, amount);
    }
};
var $pagination;

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

function displayCharacteristics() {
    var productType = getProductType($("#product-type-selector").val());
    var ProductCharacteristicValueList = $("#product-characteristics");
    ProductCharacteristicValueList.empty();

    productType.productCharacteristics.forEach(function (productCharacteristic) {
        var value = null;
        var value_id = null;
        var productCharacteristicValue = getProductCharacteristicValue(productCharacteristic.productCharacteristicId);
        if (productCharacteristicValue != undefined) {
            value_id = productCharacteristicValue.valueId;
            switch (productCharacteristic.dataType.categoryName) {
                case 'NUMBER':
                    value = productCharacteristicValue.numberValue;
                    break;
                case 'STRING':
                    value = productCharacteristicValue.stringValue;
                    break;
                case 'DATE':
                    value = productCharacteristicValue.dateValue;
            }
        }

        var valueInput = (productCharacteristic.dataType.categoryName =='DATE') ?
            constructDateInputElement() :
            constructNumberOrStringInputElement(productCharacteristic.measure, value != null ? value : '');

        var html =
            '<div class="product-characteristic-value-input col-sm-12">' +
                '<label>' + productCharacteristic.characteristicName + '</label>' +
                '<div class="input-group col-sm-12">' +
                    '<input type="hidden" name="value-id" value="' + value_id + '"/>' +
                    '<input type="hidden" name="characteristic-id" value="' + productCharacteristic.productCharacteristicId + '"/>' +
                    '<input type="hidden" name="data-type" value="' + productCharacteristic.dataType.categoryName + '"/>' +
                    valueInput +
                '</div>' +
            '</div>';

        ProductCharacteristicValueList.append($(html));

        if (productCharacteristic.dataType.categoryName == 'DATE') {
            setDateTimePicker(value);
        }
    });
}

function setDateTimePicker(dateValue) {
    $('.product-characteristic-value-input .container:last-child .date').datetimepicker();
    if (dateValue != null) {
        $('.product-characteristic-value-input .container:last-child .date').data("DateTimePicker").date(moment.unix(dateValue));
    }
}

function constructDateInputElement() {
    return '<div class="container col-sm-12">' +
            '<div class="row">' +
                // '<div class="col-sm-6">' +
                    '<div class="form-group">' +
                        '<div class="input-group date">' +
                            '<input type="text" class="form-control " name="characteristic-value"/>' +
                            '<span class="input-group-addon">' +
                                '<span class="glyphicon glyphicon-calendar">' +
                                '</span>' +
                            '</span>' +
                        '</div>' +
                    '</div>' +
                // '</div>' +
            '</div>' +
        '</div>';
}

function constructNumberOrStringInputElement(measure, value) {
    var measureHtml = '';
    if (measure != undefined && measure != "") {
        measureHtml = '<span class="input-group-addon characteristic-measure-span text-left">' +
            measure + '</span>';
    }

    return '<input type="text" class="form-control" placeholder="value" value="'
        + value + '" name="characteristic-value">' + measureHtml;
}

function selectProduct(index) {
    var activeLinkIndex = productsCache.length - index;

    console.log("selecting product with id" + productsCache[index].productId);

    var productList = $("#products-list");
    if (currentSelected == -1) {
        $("#products-editor").removeClass("hidden");
    } else {
        productList.find("a").removeClass("active");
    }
    productList.find("a:nth-child(" + (activeLinkIndex) + ")").addClass("active");
    currentSelected = index;

    $("#product-characteristics").empty();
    $(".cities-container").remove();

    var typeSelector = $("#product-type-selector");
    typeSelector.val([]);
    typeSelector.prop("disabled", false);

    $("#product-name-input").val(productsCache[currentSelected].productName);
    $("#product-description-input").val(productsCache[currentSelected].productDescription);
    $('input:radio[name=product-status]').filter('[value=' + productsCache[currentSelected].isActive + ']')
        .prop('checked', true);

    if (productsCache[currentSelected].productId != null) {
        typeSelector.val(productsCache[currentSelected].productTypeId);
        typeSelector.prop("disabled", true);

        displayCharacteristics();

        productsCache[currentSelected].prices.forEach(function (prp) {
            console.log(JSON.stringify(prp));
            addRegionalPrice(prp.priceId, prp.regionId, prp.price);
        });
    }
}

function deleteRegionalPrice(element) {
    $(element).remove();
}

function saveSelectedProduct() {
    var newProduct = {};

    newProduct.productId = productsCache[currentSelected].productId;
    newProduct.productName = $("#product-name-input").val();
    newProduct.productDescription = $("#product-description-input").val();
    newProduct.isActive = ($('input[name=product-status]:checked').val() == 'true');

    var productTypeId = extractProductTypeId();
    if (productTypeId == null) {
        return;
    }
    newProduct.productTypeId = productTypeId;

    newProduct.prices = [];
    var resultOfExtracting = extractPrices(newProduct.prices);
    if (!resultOfExtracting) {
        return;
    }

    newProduct.productCharacteristicValues = extractProductCharacteristicValues();

    console.log("Object to be sent from client: " + JSON.stringify(newProduct));

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
                console.log("Product update success! Data from server " + JSON.stringify(data));

                alert = $('<div class="alert alert-success" role="alert" id="new-product-alert">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                    data.message + "</div>");

                $.ajax({
                    type: 'GET',
                    url: '/api/user/products/get/' + data.id,
                    success: function (data) {
                        console.log("result of GET request to server: " + JSON.stringify(data));

                        $("#products-list").find("a:nth-child(" + (productsCache.length - currentSelected) + ")")
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

            alert.delay(2000)
                .fadeOut(function () {
                    alert.remove();
                });
            alert.insertAfter($("#new-product-alert-place"));
        },
        error: function (data) {
            $("#new-product-alert").remove();
            console.error("Product update error! " + JSON.stringify(data));
            $('<div id="new-product-alert" class="alert alert-danger" role="alert">' +
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                "Product changes failed</div>")
                .delay(2000)
                .fadeOut(function () {
                    $(this).remove();
                })
                .insertAfter($("#new-product-alert-place"));
        }
    });
}

function extractProductTypeId() {
    var productTypeSelector =  $("#product-type-selector");
    var productTypeId = productTypeSelector.val();
    if (productTypeId == null) {
        $("#select-no-product-type-alert").remove();
        console.error("Error: Product type of product isn`t selected");

        $('<div id="select-no-product-type-alert" class="alert alert-danger" role="alert">' +
            '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
            "Please, specify product type for new product before persisting data</div>")
            .delay(4000)
            .fadeOut(function () {
                $(this).remove();
            })
            .insertAfter(productTypeSelector);

        return null;
    }

    return productTypeId;
}

function extractProductCharacteristicValues() {
    var productCharacteristicValues = [];

    $("#product-characteristics").find(".product-characteristic-value-input").each(function () {
        var value = {
            valueId: $(this).find('input[name=value-id]').val(),
            productCharacteristicId: $(this).find('input[name=characteristic-id]').val()
        };

        var data;
        switch ($(this).find('input[name=data-type]').val()) {
            case 'NUMBER':
                data = $(this).find('input[name=characteristic-value]').val();
                if (data === '') {
                    return;
                }
                value.numberValue = +data;
                break;
            case 'DATE':
                data = $(this).find('.date').data("DateTimePicker").date();
                if (data == null) {
                    return;
                }
                value.dateValue = data;
                break;
            case 'STRING':
                data = $(this).find('input[name=characteristic-value]').val();
                if (data === '') {
                    return;
                }
                value.stringValue = data;
                break;
        }
        productCharacteristicValues.push(value);
    });

    return productCharacteristicValues;
}

function extractPrices(prices) {
    var citiesContainer = $(".cities-container");

    for (var i = 0; i < citiesContainer.length; i++) {

        if (prices.some(function (p) {
                return +p.regionId == +$(citiesContainer[i]).find('select[name="regionId"]').val();
            })) {
            $("#ambiguous-regional-price-alert").remove();
            console.error("Error: Ambiguous regional price");

            $('<div id="ambiguous-regional-price-alert" class="alert alert-danger" role="alert">' +
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                "Ambiguous regional price. Please, specify only one price per region for product</div>")
                .delay(4000)
                .fadeOut(function () {
                    $(this).remove();
                })
                .insertAfter($("#product-general-editor .cities-container:last-child"));

            return false;
        }

        prices.push({
            priceId: $(citiesContainer[i]).find('input[name="priceId"]').val(),
            regionId: $(citiesContainer[i]).find('select[name="regionId"]').val(),
            price: +$(citiesContainer[i]).find('input[name="region-price"]').val()
        });
    }

    return true;
}

function addRegionalPrice(priceId, regionId, price) {
    priceId = priceId != undefined ? priceId : null;
    price = price != undefined ? price : '';

    var regionalPriceHtml =
        '<div class="form-inline cities-container">' +
            '<input type="hidden" value="' + priceId + '" name="priceId">' +
            '<div class="form-group">' +
                '<select class="form-control" name="regionId">' +
                regionsHtml +
                '</select>' +
            '</div>' +
            '<div class="form-group full-width">' +
                '<input type="text" class="form-control full-width" placeholder="Price" name="region-price" ' +
                'value="' + price + '"' + '>' +
            '</div>' +
            '<div class="form-group">' +
                '<a class="btn btn-danger" onclick="deleteRegionalPrice($(this).parent().parent())">' +
                    '<span class="glyphicon glyphicon-remove "></span>' +
                '</a>' +
            '</div>' +
        '</div>';

    var element = $(regionalPriceHtml);
    $("#product-general-editor").append(element);

    if (regionId != undefined) {
        element.find('select[name="regionId"]').val(regionId);
    }
}

function addProduct() {
    var productList = $("#products-list");
    var productNameInput = $("#new-product-name");
    var productName = productNameInput.val();
    productNameInput.val("");

    var ref = document.createElement("a");
    ref.appendChild(document.createTextNode(productName));
    ref.className = "list-group-item";
    ref.href = "#";
    var index = productsCache.length;
    ref.onclick = function () {
        selectProduct(index);
    };
    productList.prepend(ref);

    productsCache.push({
        productId: null,
        productName: productName,
        productDescription: null,
        isActive: false,
        productTypeId: null,
        productCharacteristicValues: [],
        prices: []
    });

    selectProduct(index);
}

function checkProductName(productName) {
    if (productName == "") {
        $("#new-product-alert").remove();

        var alertDiv = $('<div id="new-product-alert" class="alert alert-danger" role="alert">' +
            '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
            'Please, enter product name</div>');
        alertDiv.delay(2000)
            .fadeOut(function () {
                alertDiv.remove();
            });

        alertDiv.insertAfter($("#products-list"));

        return false;
    }
    return true;
}

function setupAddProductButtonClickEvent() {
    $('#add-product-button').click(function () {
        if (!checkProductName($("#new-product-name").val())) {
            return;
        }

        if ($pagination.data('twbsPagination').options.totalPages > 1 || productsCache.length >= amount) {
            loadProductPage(startPage, amount - 1, addProduct);
        } else {
            addProduct();
        }
    })
}

function displayLoadedProducts() {
    var productList = $("#products-list");
    productList.empty();

    productsCache.reverse();
    productsCache.forEach(function (product, index) {
        var ref = document.createElement("a");
        ref.appendChild(document.createTextNode(product.productName));
        ref.className = "list-group-item";
        ref.href = "#";
        ref.onclick = function () {
            selectProduct(index);
        };

        productList.prepend(ref);
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

function loadProductPage(page, amount, callback) {
    console.log("loading products of page #" + page);
    var jqxhr = $.ajax({
        url: '/api/user/products?page=' + page + '&amount=' + amount,
        success: function (data) {
            productsCache = data.content;
            displayLoadedProducts();

            updatePaginationWidget(page, data.totalPages);
        },
        error: function () {
            console.error("Cannot load products");
        }
    });

    if (callback != undefined) {
        jqxhr.done(callback);
    }
}

function updatePaginationWidget(currentPage, totalPages) {
    console.log('updating pagination widget');
    $pagination.twbsPagination('destroy');
    $pagination.twbsPagination($.extend({}, defaultOpts, {
        startPage: currentPage,
        totalPages: totalPages
    }));
}

function loadProductTypes() {
    $.ajax({
        url: "/api/user/productTypes/all",
        success: function (productTypes) {
            var productTypeSelector = $("#product-type-selector");

            productTypes.forEach(function (productType) {
                var option = document.createElement("option");
                option.text = productType.productTypeName;
                option.value = productType.productTypeId;
                productTypeSelector.append(option);
            });
            productTypesCache = productTypes;

            $pagination = $('#pagination');
            loadProductPage(startPage, amount);
        },
        error: function () {
            console.error("Cannot load product types");
        }
    });
}

function createRegionsHtml(regions) {
    var regionsHtml = '';
    regions.forEach(function (region) {
        regionsHtml += '<option value="' + region.regionId + '">' + region.regionName + '</option>';
    });
    return regionsHtml;
}

function loadRegions() {
    $.ajax({
        url: "/api/user/regions/all",
        success: function (regions) {
            regionsHtml = createRegionsHtml(regions);
            loadProductTypes();
        },
        error: function () {
            console.error("Cannot load list of regions");
        }
    });
}

$(document).ready(function () {
    loadRegions();
    setupAddProductButtonClickEvent();
});