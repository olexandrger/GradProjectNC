var currentSelected = -1;
var numberOfAdded = 0;

var productTypesData;
var productsData;

var regionsData;
var regionsHtml;

function getProductTypeData(id) {
    var result;

    productTypesData.forEach(function (item) {
       if (item.id == id) {
           result = item;
       }
    });

    return result;
}

function getCharacteristicValue(id) {
    var result;
    productsData[currentSelected].productCharacteristicValues.forEach(function (item) {
        if (item.productCharacteristicId == id) {
            result = item;
        }
    });

    return result;
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
    var data = getProductTypeData($("#product-type-selector").val());

    var list = $("#product-characteristics");
    list.empty();
    if (data != undefined) {
        data.characteristics.forEach(function (item) {
            var measureHtml = "";

            if (item.measure != undefined && item.measure != "") {
                measureHtml = '<span class="input-group-addon characteristic-measure-span text-left">' + item.measure + '</span>';
            }


            var inputType = "text";
            if (item.dataTypeId == 2)
                inputType = "datetime-local";

            var value = "";
            var characteristicValue = getCharacteristicValue(item.id);
            // console.log(characteristicValue);
            if (characteristicValue != undefined) {
                if (item.dataTypeId == 1) {
                    value = characteristicValue.numberValue;
                } else if (item.dataTypeId == 2) {
                    value = javaToJsDate(characteristicValue.dateValue);
                } else if (item.dataTypeId) {
                    value = characteristicValue.stringValue;
                }
            }

            var html =
                '<div class="product-characteristic-value-input col-sm-12">' +
                '<label>' + item.name + '</label>' +
                '<div class="input-group col-sm-12">' +
                '<input type="hidden" name="characteristic-id" value="' + item.id + '"/>' +
                '<input type="' + inputType + '" class="form-control" placeholder="value" value="'+ value +  '" name="characteristic-value">' +
                measureHtml +
                '</div>' +
                '</div>';

            list.append($(html));
        });
    }
}

function selectProduct(x) {

    if (x == undefined) {
        x = productsData.length - 1;
    }

    console.log("Selected " + x);

    if (currentSelected == -1) {
        $("#products-editor").removeClass("hidden");
    }
    if (x == -1) {
        $("#products-editor").addClass("hidden");
    }

    currentSelected = x;

    var list = $("#products-list");
    list.find("a").removeClass("active");
    list.find("a:nth-child(" + (x+1) + ")").addClass("active");


    var typeSelector = $("#product-type-selector");

    $("#product-name-input").val("");
    typeSelector.val([]);
    $("#product-characteristics").empty();
    $(".cities-container").empty();
    $("#product-description-input").val("");
    $('input:radio[name=product-status]').filter('[value=true]').prop('checked', true);
    typeSelector.prop( "disabled", false);


    if (currentSelected != -1) {
        $("#product-name-input").val(productsData[currentSelected].productName);

        if (productsData[currentSelected].productType != undefined) {
            typeSelector.val(productsData[currentSelected].productType.productTypeId);
            typeSelector.prop("disabled", productsData[currentSelected].productId > 0);
            changeCharacteristics();
        }
        if (productsData[currentSelected].productDescription != undefined) {
            $("#product-description-input").val(productsData[currentSelected].productDescription);
        }

        if (productsData[currentSelected].prices != undefined) {
            productsData[currentSelected].prices.forEach(function (item) {
                addRegionalPrice(item.region.regionId, item.price);
            });
        }

        var $radios = $('input:radio[name=product-status]');
        $radios.filter('[value=' + productsData[currentSelected].isActive + ']').prop('checked', true);
    }

}

function deleteRegionalPrice(element) {
    element.parentNode.parentNode.parentNode.removeChild(element.parentNode.parentNode);
}

function saveSelected() {
    var data = {};

    data.id = productsData[currentSelected].productId;
    data.name = $("#product-name-input").val();
    data.productTypeId = $("#product-type-selector").val();
    data.description = $("#product-description-input").val();
    data.isActive = ($('input[name=product-status]:checked').val() == 'true');

    data.prices = {};
    $(".cities-container").each(function (item) {
        data.prices[$(this).find('select[name=regionId]').val()] = $(this).find('input[productName=region-price]').val();
    });

    data.characteristicValues = {};
    $("#product-characteristics").find(".product-characteristic-value-input").each(function(item) {
        data.characteristicValues[$(this).find('input[name=characteristic-id]').val()] =
            $(this).find('input[name=characteristic-value]').val();
    });

    var listSaveId = currentSelected;

    $.ajax({
        type: 'POST',
        url: '/api/admin/products/' + (data.id < 0 ? "add" : "update"),
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        processData: false,
        contentType: 'application/json',
        data: JSON.stringify(data),
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
                        $("#products-list").find("a:nth-child(" + (listSaveId+1) + ")").html(data.name);
                        productsData[listSaveId] = data;
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

function addRegionalPrice(regionId, price) {

    // var selectedRegion = "";
    // console.error(regionId);
    // if (regionId != undefined)
    //     selectedRegion=' selected="' + regionId + '" ';

    var selectedPrice = "";
    if (price != undefined)
        selectedPrice =' value="' + price + '" ';

    var html =
        '<div class="form-inline cities-container">' +
            '<div class="form-group">'+
                '<select class="form-control" name="regionId">'+
                    regionsHtml +
                '</select>'+
            '</div>'+
            '<div class="form-group full-width">'+
                '<input type="text" class="form-control full-width" placeholder="Price" name="region-price"' + selectedPrice + '>'+
            '</div>'+
            '<div class="form-group">'+
                '<a class="btn btn-danger" onclick="deleteRegionalPrice(this)">'+
                    '<span class="glyphicon glyphicon-remove "></span>'+
                '</a>'+
            '</div>'+
        '</div>';


    var element = $(html);
    $("#product-general-editor").append(element);

    if (regionId != undefined) {
        element.find('select').val(regionId);
    }

}

function addProduct(name, index) {
    var list = $("#products-list");
    var id = -(++numberOfAdded);
    if (name == undefined) {
        var nameField = $("#new-product-name");

        name = nameField.val();
        nameField.val("");
        productsData.push({productId: id, name: name, productCharacteristicValues: []});
    }

    if (name != "") {
        var ref = document.createElement("a");
        ref.appendChild(document.createTextNode(name));
        ref.className = "list-group-item";
        ref.href = "#";
        if (index == undefined)
            index = productsData.length - 1;

        ref.onclick = function () {
            selectProduct(index);
        };

        list.append(ref);

    } else {
        $("#new-product-alert").remove();

        $('<div id="new-product-alert" class="alert alert-danger" role="alert">' +
            '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
            'Can not add empty name </div>').insertAfter(list);
    }
}

function addLoadedProducts() {
    productsData.forEach(function (product, i) {
        console.log(product);
        addProduct(product.productName, i);
    });
}

function loadProducts() {
    console.log("loadProducts");
    $.ajax({
        url: "/api/user/products/all",
        success: function (data) {

            console.log(data);

            data.forEach(function(prod) {
                prod.characteristics = {};
                prod.productCharacteristics.forEach(function (char) {
                    prod.characteristics[char.productCharacteristicId] = char;
                });
            });

            productsData = data;

            addLoadedProducts();
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
        success: function (data) {

            console.log("loaded " + data.length + " product types");

            var sel = $("#product-type-selector");

            data.forEach(function(item, i) {
                // console.log("adding " + item.name);
                var option = document.createElement("option");
                option.text = item.name;
                option.value = item.id;
                sel.append(option);
            });

            productTypesData = data;

            loadProducts();
        },
        error: function () {
            console.error("Cannot load product types");
        }
    });
}

function loadRegions() {
    console.log('loadRegions');
    $.ajax({
        url: "/api/user/regions/all",
        success: function(data) {

            regionsData = data;

            regionsHtml = "";
            data.forEach(function(item) {
                regionsHtml += '<option value="' + item.regionId + '">' + item.regionName + '</option>'
            });

            loadProductTypes();
        },
        error: function () {
            console.error("Cannot load list of regions");
        }
    });
}

function loadData() {
    console.log("loadData");
    loadRegions();
}

$(document).ready(function () {
    loadData();
});