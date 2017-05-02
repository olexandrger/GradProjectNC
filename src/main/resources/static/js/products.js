var currentSelected = -1;
var numberOfAdded = 0;

var productTypesData;
var productsData;

var regionsData;
var regionsHtml;

function changeCharacteristics() {
    var value = $("#product-type-selector").val();
    console.log("current product type: " + productTypesData[value].name);

    var list = $("#product-characteristics");

    list.empty();

    productTypesData[value].characteristics.forEach(function (item) {
        var measureHtml = "";

        if (item.measure != undefined && item.measure != "") {
            measureHtml = '<span class="input-group-addon characteristic-measure-span text-left">' + item.measure + '</span>';
        }

        var html =
            '<div class="product-characteristic-value-input col-sm-12">' +
                '<label>' + item.name + '</label>' +
                '<div class="input-group col-sm-12">' +
                    '<input type="hidden" name="characteristic-id" value="' + item.id + '"/>' +
                    '<input type="text" class="form-control" placeholder="value" name="characteristic-measure">' +
                        measureHtml +
                '</div>' +
            '</div>';

        list.append($(html));
    });
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

    //TODO refresh region prices
    // $(".product-characteristic-input").remove();

    if (currentSelected != -1) {
        $("#product-name-input").val(productsData[currentSelected].name);

        $("#product-type-selector").val(productsData[currentSelected].productType.productTypeId);
        changeCharacteristics();

        $("#product-description-input").val(productsData[currentSelected].description);

        var $radios = $('input:radio[name=product-status]');
        $radios.filter('[value=' + productsData[currentSelected].isActive + ']').prop('checked', true);

        //TODO add region prices
        // for (var characteristic in productTypeData[selected].characteristics) {
        //     console.log("adding property " + characteristic + " for " + x);
        //     addProductValue(productTypeData[selected].characteristics[characteristic].name,
        //         productTypeData[selected].characteristics[characteristic].measure,
        //         productTypeData[selected].characteristics[characteristic].dataTypeId);
        // }
    }

}

function deleteRegionalPrice(element) {

    element.parentNode.parentNode.parentNode.removeChild(element.parentNode.parentNode);
}

function addRegionalPrice() {
    //TODO generate regions options
    var html =
        '<div class="form-inline cities-container">' +
            '<div class="form-group">'+
                '<select class="form-control" name="characteristic-dataTypeId">'+
                    regionsHtml +
                '</select>'+
            '</div>'+
            '<div class="form-group full-width">'+
                '<input type="text" class="form-control full-width" placeholder="Price" name="characteristic-measure">'+
            '</div>'+
            '<div class="form-group">'+
                '<a class="btn btn-danger" onclick="deleteRegionalPrice(this)">'+
                    '<span class="glyphicon glyphicon-remove "></span>'+
                '</a>'+
            '</div>'+
        '</div>';


    $("#product-general-editor").append($(html));
}

function addProduct(name, index) {
    var list = $("#products-list");
    if (name == undefined) {
        name = $("#new-product-name").val();
    }

    if (name != "") {
        var id = -(++numberOfAdded);
        var ref = document.createElement("a");
        ref.appendChild(document.createTextNode(name));
        ref.className = "list-group-item";
        ref.href = "#";
        if (index == undefined)
            index = productsData.length - 2;

        ref.onclick = function () {
            selectProduct(index);
        };

        list.append(ref);
        productsData.push({id: id, name: name});

    } else {
        $("#new-product-alert").remove();

        $('<div id="new-product-alert" class="alert alert-danger" role="alert">' +
            '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
            'Can not add empty name </div>').insertAfter(list);
    }
}

function addLoadedProducts() {
    productsData.forEach(function (product, i) {
        addProduct(product.name, i);
    });
}

function loadProducts() {
    console.log("loadProducts");
    $.ajax({
        url: "/api/admin/products/all",
        success: function (data) {

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
        url: "/api/admin/productTypes/all",
        success: function (data) {

            console.log("loaded " + data.length + " product types");

            var sel = $("#product-type-selector");

            data.forEach(function(item, i) {
                // console.log("adding " + item.name);
                var option = document.createElement("option");
                option.text = item.name;
                option.value = i;
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