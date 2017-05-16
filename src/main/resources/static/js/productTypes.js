var productTypesCache;

var dataTypesHTML;

var currentSelected = -1;

function addProductType() {
    var productTypeList = $("#product-types-list");
    var productTypeNameInputText = $("#new-product-type-name");
    var productTypeName = productTypeNameInputText.val();
    productTypeNameInputText.val("");

    if (productTypeName != "") {
        var ref = document.createElement("a");
        ref.appendChild(document.createTextNode(productTypeName));
        ref.className = "list-group-item";
        var index = productTypesCache.length;
        ref.onclick = function () {
            selectItem(index);
        };
        productTypeList.append(ref);

        productTypesCache.push({
            productTypeId: null,
            productTypeName: productTypeName,
            productTypeDescription: null,
            isActive: false,
            productCharacteristics: []
        });

        selectItem(index);

    } else {
        $('#new-product-type-alert').remove();

        var alertDiv = $('<div id="new-product-type-alert" class="alert alert-danger" role="alert">' +
            '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
            'Please, enter product type name</div>');
        alertDiv.delay(2000)
            .fadeOut(function () {
                alertDiv.remove();
            });

        alertDiv.insertAfter(productTypeList);
    }
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
    $("#product-type-values").append(element);

    if (dataTypeId != undefined) {
        element.find('select[name="characteristic-dataTypeId"]').val(dataTypeId);
    }
}

function saveSelectedProductType() {
    var newProductType = {};

    newProductType.productTypeId = productTypesCache[currentSelected].productTypeId;
    newProductType.productTypeName = $("#product-type-name-input").val();
    newProductType.productTypeDescription = $("#product-type-description-input").val();
    newProductType.isActive = ($('input[name=product-type-status]:checked').val() == 'true');

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
                    url: '/api/user/productTypes/get/' + data.id,
                    success: function (data) {
                        console.log("result of GET request to server: " + JSON.stringify(data));

                        $("#product-types-list").find("a:nth-child(" + (currentSelected + 1) + ")")
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
        error: function (data) {
            $("#new-product-type-alert").remove();
            console.error("Error while trying to add/update product type" + JSON.stringify(data));

            $('<div id="new-product-type-alert" class="alert alert-danger" role="alert">' +
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                'Error while trying to add/update product type</div>')
                .delay(2000)
                .fadeOut(function () {
                    $(this).remove();
                })
                .insertAfter($("#new-product-type-alert-place"));
        }
    });
}

function selectItem(index) {
    console.log("Selecting product type with id: " + productTypesCache[index].productTypeId);

    var productTypeList = $("#product-types-list");
    if (currentSelected == -1) {
        $("#product-type-editor").removeClass("hidden");
    } else {
        productTypeList.find("a").removeClass("active");
    }
    productTypeList.find("a:nth-child(" + (index + 1) + ")").addClass("active");
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

            loadProductTypes();
        },
        error: function () {
            console.error("Cannot load dataTypes");
        }
    });
}

$(document).ready(function () {
    loadDataTypes();
});