//pointer for selected item in products-list
var currentSelected = -1;

//page cache
var productTypesCache;
var productsCache;

//bundle of html options for choosing region
var regionsHtml;

//pagination global variables
var amount = 10; //amount of items per page
var startPage = 1;
var defaultOpts = { //twbs pagination default options
    visiblePages : 7,
    initiateStartPageClick: false,
    hideOnlyOnePage : true,
    onPageClick: function (event, page) {
        console.log('clicked page #' + page);
        loadProductPage(page, amount, function () {
            selectProduct(productsCache.length - 1);
        });
    }
};
//$ selector
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
    var $dateTimePicker = $('.product-characteristic-value-input:last-child .container .date');
    $dateTimePicker.datetimepicker();
    if (dateValue != null) {
        $dateTimePicker.data("DateTimePicker").date(moment.unix(dateValue));
    }
}

function constructDateInputElement() {
    return '<div class="container col-sm-12">' +
            '<div class="row">' +
                '<div class="form-group">' +
                    '<div class="input-group date">' +
                        '<input type="text" class="form-control " name="characteristic-value"/>' +
                        '<span class="input-group-addon">' +
                            '<span class="glyphicon glyphicon-calendar">' +
                            '</span>' +
                        '</span>' +
                    '</div>' +
                '</div>' +
            '</div>' +
        '</div>';
}

function constructNumberOrStringInputElement(measure, value) {
    var measureHtml = '<span class="input-group-addon characteristic-measure-span text-left">' +
        measure + '</span>';

    return '<input type="text" class="form-control" placeholder="value" value="'
        + value + '" name="characteristic-value">' + measureHtml;
}

function selectProduct(index) {
    var activeLinkIndex = productsCache.length - index;

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
    if ((newProduct.productName = extractProductName()) == null) {
        return;
    }
    newProduct.productDescription = $("#product-description-input").val();
    newProduct.isActive = ($('input[name=product-status]:checked').val() == 'true');
    if ((newProduct.productTypeId = extractProductTypeId()) == null) {
        return;
    }
    if ((newProduct.prices = extractPrices()) == null) {
        return;
    }
    if ((newProduct.productCharacteristicValues = extractProductCharacteristicValues()) == null) {
        return;
    }

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
            $('#new-product-alert').remove();
            $constructAlertDiv('new-product-alert', data.message)
                .addClass('alert-success')
                .delay(3000)
                .fadeOut(function () {
                    $(this).remove();
                })
                .appendTo($('#product-alert-place'));

            $.ajax({
                type: 'GET',
                url: '/api/user/products/get/' + data.id,
                success: function (data) {
                    console.log("result of GET request to server: " + JSON.stringify(data));

                    $("#products-list").find("a:nth-child(" + (productsCache.length - currentSelected) + ")")
                        .html(data.productName);
                    $("#product-type-selector").prop('disabled', true);
                    productsCache[currentSelected] = data;
                },
                error: function (data) {
                    console.log("Update after saving errored: " + data)
                }
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $("#new-product-alert").remove();
            $constructAlertDiv('new-product-alert', jqXHR.responseText)
                .addClass('alert-danger')
                .delay(3000)
                .fadeOut(function () {
                    $(this).remove();
                })
                .appendTo($("#product-alert-place"));
        }
    });
}

function $constructAlertDiv(divId, message) {
    return $('<div id="' + divId + '" class="alert" role="alert">' +
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' + message +
            '</div>');
}

function extractProductName() {
    var $productNameInput = $("#product-name-input");
    var productName = $productNameInput.val();
    if (productName.trim().length < 3 || productName.length > 60) {
        $('#product-name-alert').remove();

        $constructAlertDiv('product-name-alert', 'Product name should contain ' +
                'at least 3 characters and cannot be longer than 60 characters')
            .addClass('alert-danger')
            .delay(3000)
            .fadeOut(function () {
                $(this).remove();
            })
            .insertAfter($productNameInput);

        return null;
    }

    return productName;
}

function extractProductTypeId() {
    var $productTypeSelector =  $("#product-type-selector");
    var productTypeId = $productTypeSelector.val();
    if (productTypeId == null) {
        $("#select-no-product-type-alert").remove();

        $constructAlertDiv('select-no-product-type-alert', 'Product should belong to specific product type')
            .addClass('alert-danger')
            .delay(3000)
            .fadeOut(function () {
                $(this).remove();
            })
            .insertAfter($productTypeSelector);

        return null;
    }

    return productTypeId;
}

function showValueDangerAlert(message) {
    $("#value-alert").remove();

    $constructAlertDiv('value-alert', message)
        .addClass('alert-danger')
        .delay(3000)
        .fadeOut(function () {
            $(this).remove();
        })
        .appendTo($('#product-alert-place'));
}

function extractProductCharacteristicValues() {
    var productCharacteristicValues = [];
    var $valueInputs = $("#product-characteristics").find(".product-characteristic-value-input");

    for (var i = 0; i < $valueInputs.length; i++) {
        var value = {
            valueId: $($valueInputs[i]).find('input[name=value-id]').val(),
            productCharacteristicId: $($valueInputs[i]).find('input[name=characteristic-id]').val()
        };

        var data;
        var characteristicName = $($valueInputs[i]).find('label').text();
        switch ($($valueInputs[i]).find('input[name=data-type]').val()) {
            case 'NUMBER':
                data = $($valueInputs[i]).find('input[name=characteristic-value]').val();
                if (!$.isNumeric(data)) {
                    showValueDangerAlert("'" + characteristicName + "' must be a number value");
                    return null;
                }
                value.numberValue = +data;
                break;
            case 'DATE':
                data = $($valueInputs[i]).find('.date').data("DateTimePicker").date();
                if (data == null) {
                    showValueDangerAlert("'" + characteristicName + "' cannot be empty");
                    return null;
                }
                value.dateValue = data;
                break;
            case 'STRING':
                data = $($valueInputs[i]).find('input[name=characteristic-value]').val();
                if (data.trim().length == 0) {
                    showValueDangerAlert("'" + characteristicName + "' cannot be empty");
                    return null;
                }
                value.stringValue = data;
                break;
        }
        productCharacteristicValues.push(value);
    }

    return productCharacteristicValues;
}

function extractPrices() {
    var prices = [];
    var $citiesContainer = $(".cities-container");
    var $insertAfterDiv = $("#product-general-editor .cities-container:last-child");

    for (var i = 0; i < $citiesContainer.length; i++) {
        if (prices.some(function (p) {
                return +p.regionId == +$($citiesContainer[i]).find('select[name="regionId"]').val();
            })) {
            $('regional-price-alert').remove();

            $constructAlertDiv('regional-price-alert', 'Ambiguous regional price. ' +
                'Please, specify only one price per region for product')
                .addClass('alert-danger')
                .delay(3000)
                .fadeOut(function () {
                    $(this).remove();
                })
                .insertAfter($insertAfterDiv);

            return null;
        }
        var price = $($citiesContainer[i]).find('input[name="region-price"]').val();

        if (!($.isNumeric(price) && +price > 0)) {
            $('regional-price-alert').remove();

            $constructAlertDiv('regional-price-alert',
                'Product regional price must be a number greater than zero')
                .addClass('alert-danger')
                .delay(3000)
                .fadeOut(function () {
                    $(this).remove();
                })
                .insertAfter($insertAfterDiv);

            return null;
        }

        prices.push({
            priceId: $($citiesContainer[i]).find('input[name="priceId"]').val(),
            regionId: $($citiesContainer[i]).find('select[name="regionId"]').val(),
            price: +$($citiesContainer[i]).find('input[name="region-price"]').val()
        });
    }

    return prices;
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
    if (productName.trim().length < 3 || productName.length > 60) {
        $("#incorrect-product-name-alert").remove();

        $constructAlertDiv('incorrect-product-name-alert', 'Product name should contain ' +
            'at least 3 characters and cannot be longer than 60 characters')
            .addClass('alert-danger')
            .delay(3000)
            .fadeOut(function () {
                $(this).remove();
            }).appendTo($('#alert-box'));

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
            loadProductPage(startPage, amount, function () {
                selectProduct(productsCache.length - 1);
            });
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

function getProductById(id) {
    $.ajax({
        type: 'GET',
        url: '/api/user/products/get/' + id,
        success: function (data) {
            console.log("result of GET request to server: " + JSON.stringify(data));
            productsCache = [data];

            displayLoadedProducts();
            selectProduct(0);
        },
        error: function (data) {
            console.log("Error occurred. Cannot GET specified resource");
        }
    });
}

function setupTypeahead() {
    // Instantiate the Bloodhound suggestion engine
    var products = new Bloodhound({
        datumTokenizer:  Bloodhound.tokenizers.obj.whitespace('name'),
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        identify: function(obj) { return obj.name; },
        remote: {
            wildcard: '%QUERY',
            url: '/api/user/products/search?query=%QUERY'
        }
    });

    var $typeahead = $('.typeahead');
    var $searchClear = $('#search-clear');

// Instantiate the Typeahead UI
    $typeahead.typeahead({
        hint: true,
        highlight: true,
        minLength: 1
    }, {
        name: 'products',
        display: 'name',
        source: products
    }).on('typeahead:selected', function (obj, datum) {
        $searchClear.removeClass('hide');
        getProductById(datum.id);
    });

    //setup search clear button
    $searchClear.click(function () {
        $typeahead.typeahead('val', '');
        $(this).addClass('hide');
        currentSelected = -1;
        $("#products-editor").addClass("hidden");

        var currentPage = +$pagination.find('li.active > a').text();
        currentPage = currentPage != 0 ? currentPage : startPage;
        loadProductPage(currentPage, amount, function () {
            selectProduct(productsCache.length - 1);
        });
    });
}

$(document).ready(function () {
    loadRegions();
    setupAddProductButtonClickEvent();
    setupTypeahead();
});