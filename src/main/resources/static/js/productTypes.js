var productTypes;
var dataTypes;

var currentSelectedItem = -1;

function addProductType() {
    var list = $("#product-types-list");
    var productTypeNameInputText = $("#new-product-type-name");
    var productTypeName = productTypeNameInputText.val();
    productTypeNameInputText.val("");

    if (productTypeName != "") {
        var ref = document.createElement("a");
        ref.appendChild(document.createTextNode(productTypeName));
        ref.className = "list-group-item";
        var index = productTypes.length;
        ref.onclick = function () {
            selectItem(index);
        };

        list.append(ref);
        productTypes.push({
            productTypeId: null, productTypeName: productTypeName,
            productTypeDescription: "", isActive: false, productCharacteristics: []
        });

        selectItem(index);
    } else {
        $("#new-product-type-alert").remove();

        $('<div id="new-product-type-alert" class="alert alert-danger" role="alert">' +
            '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
            'Please, enter product type name</div>').insertAfter(list);
    }
}

function removeProductCharacteristic(element) {
    console.log(element);
    element.parentNode.parentNode.removeChild(element.parentNode)
}

function addProductCharacteristic(id, name, measure, dataTypeId) {
    var dataTypeOptions = "";
    dataTypes.forEach(function (dataType) {
        dataTypeOptions += '<option value="' + dataType.categoryId + '"';
        if (dataType.categoryId == dataTypeId) {
            dataTypeOptions += " selected ";
        }
        dataTypeOptions += '>' + dataType.categoryName + '</option>';
    });

    if (name == undefined) name = "";
    if (measure == undefined) measure = "";
    if (dataTypeId == undefined) dataTypeId = "";
    if (id == undefined) id = null;

    var html =
        '<div class="input-group product-characteristic-input">' +
        '<input type="hidden" name="characteristic-id" value="' + id + '"/>' +
        '<span class="input-group-addon">Name</span>' +
        '<input type="text" class="form-control" value="' + name + '" placeholder="Name" name="characteristic-productName">' +
        '<span class="input-group-addon">Measure</span>' +
        '<input type="text" class="form-control" value="' + measure + '" placeholder="Measure" name="characteristic-measure">' +
        '<span class="input-group-addon">Data type</span>' +
        '<select class="form-control" name="characteristic-dataTypeId">' +
        dataTypeOptions +
        '</select>' +
        '<span class="input-group-addon" style="background-color: #d9534f; cursor: pointer" onclick="removeProductCharacteristic(this)">' +
        '<span class="glyphicon glyphicon-remove bg-danger" style="color: white; background-color: #d9534f; cursor: pointer"></span>' +
        '</span>' +
        '</div>';

    $("#product-type-values").append($(html));
}

function saveSelectedProductType() {
    var index = currentSelectedItem;

    productTypes[index].productTypeName = $("#product-type-name-input").val();
    productTypes[index].productTypeDescription = $("#product-type-description-input").val();
    productTypes[index].isActive = $('input[name=product-type-status]:checked').val() == 'true';
    productTypes[index].productCharacteristics = [];

    $("#product-type-values").find(".product-characteristic-input").each(function (element) {
        productTypes[index].productCharacteristics.push({
            productCharacteristicId: $(this).find("input[name='characteristic-id']").val(),
            characteristicName: $(this).find("input[name='characteristic-productName']").val(),
            measure: $(this).find("input[name='characteristic-measure']").val(),
            dataTypeId: $(this).find("select").val()
        });
    });

    console.log("Sending\n" + JSON.stringify(productTypes[index]));

    $.ajax({
        type: 'POST',
        url: '/api/admin/productTypes/' + (productTypes[index].productTypeId == null ? 'add' : 'update'),
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        processData: false,
        contentType: 'application/json',
        data: JSON.stringify(productTypes[index]),
        success: function (data) {
            var alert;
            if (data.status == 'success') {
                console.log("Saving success! " + JSON.stringify(data));

                alert = $('<div id="new-product-type-alert" class="alert alert-success" role="alert">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                    data.message + '</div>');

                $.ajax({
                    type: 'GET',
                    url: '/api/user/productTypes/get/' + data.id,
                    success: function (data) {
                        $("#product-types-list").find("a:nth-child(" + (index + 1) + ")").html(data.productTypeName);
                        productTypes[index] = data;
                    },
                    error: function (data) {
                        console.log("Get request failed. Returned data: " + data)
                    }
                });

            } else {
                console.log("Problems occured during persisting changes. Returned data: " + JSON.stringify(data));
                alert = $('<div id="new-product-type-alert" class="alert alert-danger" role="alert">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                    data.message + '</div>');
            }

            $("#new-product-type-alert").remove();

            alert.insertAfter($("#new-product-type-alert-place"));
        },
        error: function (data) {
            console.error("Error while trying to add/update product type" + JSON.stringify(data));

            $("#new-product-type-alert").remove();

            $('<div id="new-product-type-alert" class="alert alert-danger" role="alert">' +
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                'Error while trying to add/update product type</div>').insertAfter($("#new-product-type-alert-place"));
        }
    });
}

function selectItem(itemIndex) {
    console.log("Select product type with id: " + itemIndex);
    var list = $("#product-types-list");

    if (currentSelectedItem == -1) {
        $("#product-type-editor").removeClass("hidden");
    }
    else {
        list.find("a").removeClass("active");
        $("#new-product-type-alert").remove();
    }
    currentSelectedItem = itemIndex;
    list.find("a:nth-child(" + (itemIndex + 1) + ")").addClass("active");


    $(".product-characteristic-input").remove();

    if (currentSelectedItem != -1) {
        $("#product-type-name-input").val(productTypes[currentSelectedItem].productTypeName);
        $("#product-type-description-input").val(productTypes[currentSelectedItem].productTypeDescription);
        $('input:radio[name=product-type-status]').filter('[value=' + productTypes[currentSelectedItem].isActive + ']')
            .prop('checked', true);

        for (var characteristic in productTypes[currentSelectedItem].productCharacteristics) {
            console.log("displaying characteristic " + characteristic + " for product type with index " + itemIndex);
            addProductCharacteristic(
                productTypes[currentSelectedItem].productCharacteristics[characteristic].productCharacteristicId,
                productTypes[currentSelectedItem].productCharacteristics[characteristic].characteristicName,
                productTypes[currentSelectedItem].productCharacteristics[characteristic].measure,
                productTypes[currentSelectedItem].productCharacteristics[characteristic].dataTypeId);
        }
    }
}

function loadProductTypes() {
    $.ajax({
        url: "/api/user/productTypes/all ",
        success: function (data) {
            var list = $("#product-types-list");

            productTypes = data;

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

function loadDataTypes() {
    $.ajax({
        url: "/api/user/dataTypes",
        success: function (data) {
            dataTypes = data;
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