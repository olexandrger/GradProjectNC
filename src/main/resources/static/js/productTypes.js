var productTypesCache;

var dataTypesHTML;

var currentSelected = -1;

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
var $pagination;

//typeahead global variables
var prefetchAmount = 50;

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

function checkProductTypeName(productTypeName) {
    if (productTypeName == "") {
        $('#new-product-type-alert').remove();

        var alertDiv = $('<div id="new-product-type-alert" class="alert alert-danger" role="alert">' +
            '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
            'Please, enter product type name</div>');
        alertDiv.delay(2000)
            .fadeOut(function () {
                alertDiv.remove();
            });

        alertDiv.appendTo($("#alert-box"));

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
            '<input type="text" class="form-control" value="' + name + '" placeholder="Name" name="characteristic-productName">' +
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
        element.find('select[name="characteristic-dataTypeId"]').val(dataTypeId);
    }
}

function extractProductTypeName() {
    var productTypeName = $("#product-type-name-input").val();
    if (productTypeName == '' || productTypeName.length < 3) {
        $("#empty-product-type-name-alert").remove();
        console.log("Error: Product type name cannot be empty");

        $('<div id="empty-product-type-name-alert" class="alert alert-danger" role="alert">' +
            '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
            "Please, specify product type name, it cannot be empty</div>")
            .delay(2000)
            .fadeOut(function () {
                $(this).remove();
            })
            .insertAfter($('#product-type-name-input'));

        return null;
    }
    return productTypeName;
}

function checkCharacteristicsNameInputs() {
    var checkResult = true;
    $("#product-type-values").find(".product-characteristic-input").each(function () {
        if ($(this).find("input[name='characteristic-productName']").val() == '') {
            checkResult = false;
        }
    });

    if (!checkResult) {
        $("#empty-product-type-char-name-measure-alert").remove();
        console.log("Error: Product type characteristic name cannot be empty");

        $('<div id="empty-product-type-char-name-alert" class="alert alert-danger" role="alert">' +
            '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
            "Please, specify product type characteristics names, it cannot be empty</div>")
            .delay(2000)
            .fadeOut(function () {
                $(this).remove();
            })
            .insertAfter($('#characteristic-box:last-child'));
    }

    return checkResult;
}

function saveSelectedProductType() {
    var newProductType = {};

    newProductType.productTypeId = productTypesCache[currentSelected].productTypeId;

    var productTypeName = extractProductTypeName();
    if (productTypeName == null) {
        return;
    }
    newProductType.productTypeName = productTypeName;
    newProductType.productTypeDescription = $("#product-type-description-input").val();
    newProductType.isActive = ($('input[name=product-type-status]:checked').val() == 'true');

    if (!checkCharacteristicsNameInputs()) {
        return;
    }

    newProductType.productCharacteristics = [];
    $("#product-type-values").find(".product-characteristic-input").each(function () {
        newProductType.productCharacteristics.push({
            productCharacteristicId: $(this).find("input[name='characteristic-id']").val(),
            characteristicName: $(this).find("input[name='characteristic-productName']").val(),
            measure: $(this).find("input[name='characteristic-measure']").val(),
            dataType: {
                categoryId: $(this).find("select").val()
            }
        });
    });

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
            var alert;
            $("#new-product-type-alert").remove();
            if (data.status == 'success') {
                console.log("Product type update success! Data from server " + JSON.stringify(data));

                alert = $('<div id="new-product-type-alert" class="alert alert-success" role="alert">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                    data.message + '</div>');

                $.ajax({
                    type: 'GET',
                    url: '/api/user/productTypes/' + data.id,
                    success: function (data) {
                        console.log("result of GET request to server: " + JSON.stringify(data));

                        $("#product-types-list").find("a:nth-child(" + (productTypesCache.length - currentSelected) + ")")
                            .html(data.productTypeName);
                        productTypesCache[currentSelected] = data;
                    },
                    error: function (data) {
                        console.log("Get request failed. Returned data: " + data)
                    }
                });

            } else {
                console.log("Problems occurred during persisting changes. Returned data: " + JSON.stringify(data));
                alert = $('<div id="new-product-type-alert" class="alert alert-danger" role="alert">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                    data.message + '</div>');
            }

            alert.delay(2000)
                .fadeOut(function () {
                    alert.remove();
                });
            alert.insertAfter($("#new-product-type-alert-place"));
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $("#new-product-type-alert").remove();

            $('<div id="new-product-type-alert" class="alert alert-danger" role="alert">' +
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                jqXHR.responseText + '</div>')
                .delay(2000)
                .fadeOut(function () {
                    $(this).remove();
                })
                .insertAfter($("#new-product-type-alert-place"));
        }
    });
}

function selectItem(index) {
    var activeLinkIndex = productTypesCache.length - index;

    console.log("Selecting product type with id: " + productTypesCache[index].productTypeId);

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

function loadProductTypes() {
    $.ajax({
        url: "/api/user/productTypes/all ",
        success: function (data) {
            var list = $("#product-types-list");
            productTypesCache = data;

            data.forEach(function (productType, i) {
                console.log(productType.productTypeName + " loaded");

                var ref = document.createElement("a");
                ref.appendChild(document.createTextNode(productType.productTypeName));
                ref.className = "list-group-item";
                ref.href = "#";
                ref.onclick = function () {
                    selectItem(i);
                };

                list.append(ref);
            });
        },
        error: function () {
            console.error("Cannot load list of product types");
        }
    });
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