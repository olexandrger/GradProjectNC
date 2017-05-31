//pointer for selected item in product-types-list
var currentSelected = -1;

//page cache
var productTypesCache;

//bundle of html options for choosing data type
var dataTypesHTML;

//pagination global variables
var amount = 10; //amount of items per page
var startPage = 1;
var defaultOpts = { //twbs pagination default options
    visiblePages : 7,
    initiateStartPageClick: false,
    hideOnlyOnePage : true,
    onPageClick: function (event, page) {
        console.log('clicked page #' + page);
        loadProductTypePage(page, amount, function () {
            selectItem(productTypesCache.length - 1);
        });
    }
};
//$ selector
var $pagination;

function addProductType() {
    var productTypeList = $("#product-types-list");
    var productTypeNameInputText = $("#new-product-type-name");
    var productTypeName = productTypeNameInputText.val();
    productTypeNameInputText.val("");

    var ref = document.createElement("a");
    ref.appendChild(document.createTextNode(productTypeName));
    ref.className = "list-group-item";
    var index = productTypesCache.length;
    ref.onclick = function () {
        selectItem(index);
    };
    productTypeList.prepend(ref);

    productTypesCache.push({
        productTypeId: null,
        productTypeName: productTypeName,
        productTypeDescription: null,
        isActive: false,
        productCharacteristics: []
    });

    selectItem(index);
}

function $constructAlertDiv(divId, message) {
    return $('<div id="' + divId + '" class="alert" role="alert">' +
        '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' + message +
        '</div>');
}

function checkProductTypeName(productTypeName) {
    if (productTypeName.trim().length < 3 || productTypeName.length > 60) {
        $('#incorrect-product-type-name-alert').remove();

        $constructAlertDiv('incorrect-product-type-name-alert', 'Product type name should contain ' +
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

function setupAddProductTypeButtonClickEvent() {
    $('#add-product-type-button').click(function () {
        if (!checkProductTypeName($("#new-product-type-name").val())) {
            return;
        }

        if ($pagination.data('twbsPagination').options.totalPages > 1 || productTypesCache.length >= amount) {
            loadProductTypePage(startPage, amount - 1, addProductType);
        } else {
            addProductType();
        }
    })
}

function removeProductCharacteristic(element) {
    $(element).remove();
}

function addProductCharacteristic(id, name, measure, dataTypeId) {
    id = (id != undefined) ? id : null;
    name = (name != undefined) ? name : '';
    measure = (measure != undefined) ? measure : '';

    var productCharacteristicHTML =
        '<div class="input-group product-characteristic-input">' +
            '<input type="hidden" name="characteristic-id" value="' + id + '"/>' +
            '<span class="input-group-addon">Name</span>' +
            '<input type="text" class="form-control" value="' + name + '" placeholder="Name" name="characteristic-name">' +
            '<span class="input-group-addon">Measure</span>' +
            '<input type="text" class="form-control" value="' + measure + '" placeholder="Measure" name="characteristic-measure">' +
            '<span class="input-group-addon">Data type</span>' +
            '<select class="form-control" name="characteristic-dataTypeId">' +
            dataTypesHTML +
            '</select>' +
            '<span class="input-group-addon" style="background-color: #d9534f; cursor: pointer" ' +
                    'onclick="removeProductCharacteristic($(this).parent())">' +
            '<span class="glyphicon glyphicon-remove bg-danger" style="color: white; background-color: #d9534f; cursor: pointer"></span>' +
            '</span>' +
        '</div>';

    var element = $(productCharacteristicHTML);
    $("#characteristic-box").append(element);

    if (dataTypeId != undefined) {
        $dataTypeSelector = element.find('select[name="characteristic-dataTypeId"]');
        $dataTypeSelector.val(dataTypeId);
        $dataTypeSelector.prop("disabled", true);
    }
}

function extractProductTypeName() {
    var $productTypeNameInput = $("#product-type-name-input");
    var productTypeName = $productTypeNameInput.val();
    if (productTypeName.trim().length < 3 || productTypeName.length > 60) {
        $("#product-type-name-alert").remove();

        $constructAlertDiv('product-type-name-alert', 'Product type name should contain ' +
            'at least 3 characters and cannot be longer than 60 characters')
            .addClass('alert-danger')
            .delay(3000)
            .fadeOut(function () {
                $(this).remove();
            })
            .insertAfter($productTypeNameInput);

        return null;
    }
    return productTypeName;
}

function extractProductCharacteristics() {
    var characteristics = [];
    var $characteristicInputs = $('#characteristic-box').find('.product-characteristic-input');

    for (var i = 0; i <$characteristicInputs.length; i++) {
        var characteristic = {
            productCharacteristicId: $($characteristicInputs[i]).find('input[name="characteristic-id"]').val(),
            dataType: {
                categoryId: $($characteristicInputs[i]).find('select[name ="characteristic-dataTypeId"]').val()
            }
        };
        var characteristicName = $($characteristicInputs[i]).find('input[name="characteristic-name"]').val();
        var measure = $($characteristicInputs[i]).find('input[name="characteristic-measure"]').val();
        if (characteristicName.trim().length == 0 || measure.trim().length == 0) {
            $("#characteristic-alert").remove();

            $constructAlertDiv('characteristic-alert', 'Product characteristic ' +
            'name and measure cannot be empty')
                .addClass('alert-danger')
                .delay(3000)
                .fadeOut(function () {
                    $(this).remove();
                })
                .insertAfter($('#characteristic-box'));

            return null;
        }
        characteristic.characteristicName = characteristicName;
        characteristic.measure = measure;

        characteristics.push(characteristic);
    }

    return characteristics;
}

function saveSelectedProductType() {
    var newProductType = {};

    newProductType.productTypeId = productTypesCache[currentSelected].productTypeId;
    if ((newProductType.productTypeName = extractProductTypeName()) == null) {
        return;
    }
    newProductType.productTypeDescription = $("#product-type-description-input").val();
    newProductType.isActive = ($('input[name=product-type-status]:checked').val() == 'true');
    if ((newProductType.productCharacteristics = extractProductCharacteristics()) == null) {
        return;
    }

    console.log("Object to be sent from client: " + JSON.stringify(newProductType));

    $.ajax({
        type: 'POST',
        url: '/api/admin/productTypes/' + (newProductType.productTypeId == null ? 'add' : 'update'),
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        processData: false,
        contentType: 'application/json',
        data: JSON.stringify(newProductType),
        success: function (data) {
            $("#new-product-type-alert").remove();
            $constructAlertDiv('new-product-type-alert', data.message)
                .addClass('alert-success')
                .delay(3000)
                .fadeOut(function () {
                    $(this).remove();
                })
                .appendTo($('#product-type-alert-place'));

            $.ajax({
                type: 'GET',
                url: '/api/user/productTypes/' + data.id,
                success: function (data) {
                    console.log("result of GET request to server: " + JSON.stringify(data));

                    $("#product-types-list").find("a:nth-child(" + (productTypesCache.length - currentSelected) + ")")
                        .html(data.productTypeName);
                    $('.product-characteristic-input select[name="characteristic-dataTypeId"]')
                        .prop("disabled", true);

                    productTypesCache[currentSelected] = data;
                },
                error: function (data) {
                    console.log("Get request failed. Returned data: " + data)
                }
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $("#new-product-type-alert").remove();
            $constructAlertDiv('#new-product-type-alert', jqXHR.responseText)
                .addClass('alert-danger')
                .delay(3000)
                .fadeOut(function () {
                    $(this).remove();
                })
                .appendTo($("#product-type-alert-place"));
        }
    });
}

function selectItem(index) {
    var activeLinkIndex = productTypesCache.length - index;

    var productTypeList = $("#product-types-list");
    if (currentSelected == -1) {
        $("#product-type-editor").removeClass("hidden");
    } else {
        productTypeList.find("a").removeClass("active");
    }
    productTypeList.find("a:nth-child(" + (activeLinkIndex) + ")").addClass("active");
    currentSelected = index;

    $(".product-characteristic-input").remove();

    $("#product-type-name-input").val(productTypesCache[currentSelected].productTypeName);
    $("#product-type-description-input").val(productTypesCache[currentSelected].productTypeDescription);
    $('input:radio[name=product-type-status]').filter('[value=' + productTypesCache[currentSelected].isActive + ']')
        .prop('checked', true);

    productTypesCache[currentSelected].productCharacteristics.forEach(function (characteristic) {
        addProductCharacteristic(
            characteristic.productCharacteristicId,
            characteristic.characteristicName,
            characteristic.measure,
            characteristic.dataType.categoryId
        );
    });
}

function displayLoadedProductTypes() {
    var productTypesList = $("#product-types-list");
    productTypesList.empty();

    productTypesCache.reverse();
    productTypesCache.forEach(function (productType, index) {
        var ref = document.createElement("a");
        ref.appendChild(document.createTextNode(productType.productTypeName));
        ref.className = "list-group-item";
        ref.href = "#";
        ref.onclick = function () {
            selectItem(index);
        };

        productTypesList.prepend(ref);
    });
}

function loadProductTypePage(page, amount, callback) {
    console.log("loading product types page #" + page);
    var jqxhr = $.ajax({
        url: '/api/user/productTypes?page=' + page + '&amount=' + amount,
        success: function (data) {
            productTypesCache = data.content;
            displayLoadedProductTypes();

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

function createDataTypesHTML(dataTypes) {
    var dataTypesHTML = "";
    dataTypes.forEach(function (dataType) {
        dataTypesHTML += '<option value="' + dataType.categoryId + '">' + dataType.categoryName + '</option>';
    });

    return dataTypesHTML;
}

function loadDataTypes() {
    $.ajax({
        url: "/api/user/dataTypes",
        success: function (data) {
            console.log('data types are loaded');
            dataTypesHTML = createDataTypesHTML(data);

            $pagination = $('#pagination');
            loadProductTypePage(startPage, amount, function () {
                selectItem(productTypesCache.length - 1);
            });
        },
        error: function () {
            console.error("Cannot load dataTypes");
        }
    });
}

function getProductTypeById(id) {
    $.ajax({
        type: 'GET',
        url: '/api/user/productTypes/' + id,
        success: function (data) {
            console.log("result of GET request to server: " + JSON.stringify(data));
            productTypesCache = [data];

            displayLoadedProductTypes();
            selectItem(0);
        },
        error: function (data) {
            console.log("Error occurred. Cannot GET specified resource");
        }
    });
}

function setupTypeahead() {
    // Instantiate the Bloodhound suggestion engine
    var productTypes = new Bloodhound({
        datumTokenizer:  Bloodhound.tokenizers.obj.whitespace('name'),
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        identify: function(obj) { return obj.name; },
        remote: {
            wildcard: '%QUERY',
            url: '/api/user/productTypes/search?query=%QUERY'
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
        name: 'productTypes',
        display: 'name',
        source: productTypes
    }).on('typeahead:selected', function (obj, datum) {
        $searchClear.removeClass('hide');
        getProductTypeById(datum.id);
    });

    //setup search clear button
    $searchClear.click(function () {
        $typeahead.typeahead('val', '');
        $(this).addClass('hide');
        currentSelected = -1;
        $("#products-editor").addClass("hidden");

        var currentPage = +$pagination.find('li.active > a').text();
        currentPage = currentPage != 0 ? currentPage : startPage;
        loadProductTypePage(currentPage, amount, function () {
            selectItem(productTypesCache.length - 1);
        });
    });
}

$(document).ready(function () {
    loadDataTypes();
    setupAddProductTypeButtonClickEvent();
    setupTypeahead();
});