var productTypeData;
var dataTypeData;

var numberOfAdded = 0;
var selected = -1;
//TODO correctIdAfterSave

function addProductType() {
    var list = $("#product-types-list");
    var name = $("#new-product-type-name").val();

    if (name != "") {
        var id = -(++numberOfAdded);
        var ref = document.createElement("a");
        ref.appendChild(document.createTextNode(name));
        ref.className = "list-group-item";
        var index = productTypeData.length;
        ref.onclick = function () {
            selectItem(index);
        };

        list.append(ref);
        productTypeData.push({id: id, name: name, description: "", characteristics: []});

        selectItem(index);
    } else {
            $("#new-product-type-alert").remove();

            $('<div id="new-product-type-alert" class="alert alert-danger" role="alert">' +
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
            'Can not add empty name </div>').insertAfter( list);
    }

}

function removeProductValue(element) {
    console.log(element);
    element.parentNode.parentNode.removeChild(element.parentNode)
}

function addProductValue(name, measure, dataType) {

    var options = "";

    for (var dataTypeId in dataTypeData) {
        if (dataTypeData.hasOwnProperty(dataTypeId)) {
            options += '<option value="' + dataTypeId + '"';

            if (dataTypeId == dataType) {
                options += " selected ";
            }
            options += '>' + dataTypeData[dataTypeId] + '</option>';
        }
    }

    if (name == undefined) name="";
    if (measure == undefined) measure="";
    if (dataType == undefined) dataType="";

    var html=
        '<div class="input-group product-characteristic-input">'+
            '<span class="input-group-addon">Name</span>'+
            '<input type="text" class="form-control" value="' + name + '" placeholder="Name" name="characteristic-name">'+
            '<span class="input-group-addon">Measure</span>'+
            '<input type="text" class="form-control" value="' + measure + '" placeholder="Measure" name="characteristic-measure">'+
            '<span class="input-group-addon">Data type</span>'+
            '<select class="form-control" name="characteristic-dataTypeId">'+
                options +
            '</select>'+
            '<span class="input-group-addon" style="background-color: #d9534f; cursor: pointer" onclick="removeProductValue(this)">'+
                '<span class="glyphicon glyphicon-remove bg-danger" style="color: white; background-color: #d9534f; cursor: pointer"></span>'+
            '</span>'+
        '</div>';

    $("#product-type-values").append($(html));
}

function saveSelected() {
    var _csrf = $('meta[name=_csrf]').attr("content");

    var savedId = selected;

    productTypeData[savedId].name = $("#product-type-name-input").val();
    productTypeData[savedId].description = $("#product-type-description-input").val();
    productTypeData[savedId].characteristics = [];

    $("#product-type-values").find(".product-characteristic-input").each(function (element) {
       productTypeData[savedId].characteristics.push({
           name: $(this).find("input[name='characteristic-name']").val(),
           measure: $(this).find("input[name='characteristic-measure']").val(),
           dataTypeId: $(this).find("select").val()
       });
    });

    console.log("Sending\n" + JSON.stringify(productTypeData[savedId]));

    $.ajax({
        type: 'POST',
        url: '/api/admin/productTypes/update',
        headers: {
            'X-CSRF-TOKEN': _csrf
        },
        processData: false,
        contentType: 'application/json',
        data: JSON.stringify(productTypeData[selected]),
        success: function (data) {
            var alert;
            if (data.status == 'success') {
                console.log("Saving success! " + JSON.stringify(data));

                $("#product-types-list").find("a:nth-child(" + (savedId+1) + ")").html(productTypeData[savedId].name);

                alert = $('<div id="new-product-type-alert" class="alert alert-success" role="alert">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                     data.message + '</div>');
            } else {
                console.log("Saving error! " + JSON.stringify(data));
                alert = $('<div id="new-product-type-alert" class="alert alert-danger" role="alert">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                    data.message + '</div>');
            }

            $("#new-product-type-alert").remove();

            alert.insertAfter($("#new-product-type-alert-place"));

        },
        error: function (data) {
            console.error("Saving error! " + JSON.stringify(data));

            $("#new-product-type-alert").remove();

            $('<div id="new-product-type-alert" class="alert alert-danger" role="alert">' +
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                'Product changes failed </div>').insertAfter($("#new-product-type-alert-place"));
        }
    });
}

function deleteSelected() {

}

function selectItem(x) {
    console.log("Selected " + x);

    if (selected == -1) {
        $("#product-type-editor").removeClass("hidden");
    }
    if (x == -1) {
        $("#product-type-editor").addClass("hidden");
    }

    selected = x;

    var list = $("#product-types-list");
    list.find("a").removeClass("active");
    list.find("a:nth-child(" + (x+1) + ")").addClass("active");

    $(".product-characteristic-input").remove();

    if (selected != -1) {
        $("#product-type-name-input").val(productTypeData[selected].name);
        $("#product-type-description-input").val(productTypeData[selected].description);

        for (var characteristic in productTypeData[selected].characteristics) {
            console.log("adding property " + characteristic + " for " + x);
            addProductValue(productTypeData[selected].characteristics[characteristic].name,
                productTypeData[selected].characteristics[characteristic].measure,
                productTypeData[selected].characteristics[characteristic].dataTypeId);
        }
    }
}

function loadInfo() {
    $.ajax({
        url: "/api/admin/dataTypes",
        success: function (data) {
            dataTypeData = data;
            loadProductTypes();
        },
        error: function () {
            console.error("Cannot load dataTypes");
        }
    });
}

function loadProductTypes() {
    $.ajax({
        url: "/api/admin/productTypes/all ",
        success: function(data) {
        var list = $("#product-types-list");

        productTypeData = data;

        data.forEach(function(item, i) {
            console.log(item.name + " loaded");

            var ref = document.createElement("a");
            ref.appendChild(document.createTextNode(item.name));
            ref.className = "list-group-item";
            ref.onclick = function () {
                selectItem(i);
            };

            list.append(ref);
        });
    },
    error: function () {
        console.error("Cannot load list product types");
    }
});
}

$(document).ready(function () {
    loadInfo();
});