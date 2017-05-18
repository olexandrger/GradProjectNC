var regionsData;
var discounts=[];
var discountsCache=[];
var productPricesForRegion;
var productPricesForDiscount = [];
var numberOfAdded = 0;
var currentSelected = -1;
function addDiscount() {
    clearDataFields();


    var list = $("#discounts-list");
    var nameInput = $("#new-discount-name");
    var name = nameInput.val();
    nameInput.val("");


    if (name != "") {
        var id = -(++numberOfAdded);
        var ref = document.createElement("a");
        ref.appendChild(document.createTextNode(name));
        ref.className = "list-group-item";
        var index = discounts.length;
        ref.onclick = function () {
            //currentSelected = index;
            selectDiscount(index);

        };

        list.append(ref);
        //productTypeData.push({id: id, name: name, description: "", characteristics: []});
        discounts.push({discountId: id,discountTitle: name,discount: 0.0, productRegionPrices: [] });

        selectDiscount(index);
    } else {
        $("#new-discount-alert-place").remove();

        $('<div id="new-discount-alert-place" class="alert alert-danger" role="alert">' +
            '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
            'Can not add empty name </div>').insertAfter( list);
    }


    
}
function saveSelected() {
    var discount = discounts[currentSelected];

    var title = $("#discount-name-input").val();
    var startDate = $('#datetimepicker1').data("DateTimePicker").date().unix();
    var endDate = $('#datetimepicker2').data("DateTimePicker").date().unix();
    var discountAmm = $("#ammInput").val();

    discount.discountTitle = title;
    discount.startDate = startDate;
    discount.endDate = endDate;
    discount.discount = discountAmm;

    console.log(JSON.stringify(discounts[currentSelected]));

    var operationType;
    if(discount.discountId >= 0){
        operationType = "update";
    }else {
        operationType = "add";
    }



    $.ajax({
        type: 'POST',
        url: '/api/admin/discounts/' + operationType,
        headers: {

            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        processData: false,
        contentType: 'application/json',
        data: JSON.stringify(discount),
        success: function (data) {
            //var alert;
            if (data.status == 'success') {
                console.log("Success on " + operationType + JSON.stringify(data));
                //alert = $('<div id="search-alert" class="alert alert-success" role="alert">' +
                    //"User " + userData1.email+" updated!" + "</div>");
            } else {
                console.log("Error on  " + operationType + JSON.stringify(data));
               // alert = $('<div id="search-alert" class="alert alert-danger" role="alert">' +
                  //  data.message + '</div>');
            }


           // $("#search-alert").replaceWith(alert);
        },
        error: function (data) {
            console.error("Error on  " + operationType + JSON.stringify(data));
            //var alert = $('<div id="search-alert" class="alert alert-danger" role="alert">' +
              //  "Update failed</div>");
           // $("#search-alert").replaceWith(alert);
        }
    });


}
function loadAllDiscounts() {
    $.ajax({
        url: "/api/admin/discounts/allDiscounts ",
        success: function(data) {
            discounts = data;
            var list = $("#discounts-list");
            console.log(JSON.stringify(discounts));

            data.forEach(function(item, i) {
                console.log(item.discountTitle + "discount title loaded");

                var ref = document.createElement("a");
                ref.appendChild(document.createTextNode(item.discountTitle));
                ref.className = "list-group-item";
                ref.href = "#";
                ref.onclick = function () {
                    selectDiscount(i);
                };

                list.append(ref);
            });



            for (var i in discounts){
                console.log(JSON.stringify(moment.unix(discounts[i].startDate).format("LLL")));
            }
        },
        error: function () {
            console.error("Cannot load list product types");
        }
    });
    
}

function selectDiscount(i) {
    clearDataFields();
    currentSelected = i;

    console.log(JSON.stringify(discounts[i].discountTitle));

    var discount = discounts[i];
    var prices = discount.productRegionPrices;

    $("#discount-name-input").val(discount.discountTitle);
    $('#datetimepicker1').data("DateTimePicker").date(moment.unix(discount.startDate));
    $('#datetimepicker2').data("DateTimePicker").date(moment.unix(discount.endDate));
    $("#ammInput").val(discount.discount);

    var node =$("#products");
    node.empty();

    for (var prp in prices ){

        addProduct(prices[prp].priceId, prices[prp].product.productName +" for "+ prices[prp].region.regionName + " region");
    }

}

function addToProducts() {

    var id = document.getElementById("discount-product-selector").value;
    var name = $("#discount-product-selector option:selected").text();


    for (var i in productPricesForRegion){
        if(productPricesForRegion[i].priceId == id ){
            //productPricesForDiscount.push(productPricesForRegion[i]);
            discounts[currentSelected].productRegionPrices.push(productPricesForRegion[i]);
        }
    }


    var fullProductName = name +" for " + $("#discount-region-selector option:selected").text() + " region";

    addProduct(id, fullProductName);
}

function addProduct(id,name) {
    var node =$("#products");

    var products = document.getElementById("products");

    var html =
        '<a href="#" class="list-group-item"'+' name="'+id+'"'+  ' >'+name+ '<input type="checkbox" class="pull-right"></a>';


    node.append($(html));

    
}



function changeRegion() {

    var regionId= document.getElementById("discount-region-selector").value;
    $("#discount-product-selector").empty();


    $.ajax({
            type: 'GET',
            url: '/api/admin/discounts/productPricesForRegion',
            headers: {

                'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
            },

            data: ({regionId: regionId}),
            dataType: 'json',

            success: function (data) {

                productPricesForRegion = data;

                var options = document.getElementById("discount-product-selector");

                data.forEach(function(item, i) {
                    console.log(item.region.regionName + "region name change region loaded");

                    var option = document.createElement("option");
                    option.text = item.product.productName;
                    option.value = item.priceId;

                    options.appendChild(option);
                });


            },
            error: function (data) {
                console.error("Error! ");

            }

        }
    );



}
function changeProduct(){

}
function pickStartDate() {

}
function pickEndDate() {
    console.log(JSON.stringify($('#datetimepicker1').data("DateTimePicker").date()));
    console.log(")))&");





}
function loadAllRegions() {

    $.ajax({
        url: "/api/admin/discounts/allRegions ",
        success: function(data) {
            regionsData = data;

            var options = document.getElementById("discount-region-selector");
            var option = document.createElement("option");


            data.forEach(function(item, i) {
                console.log(item.regionName + " region name");

                var option = document.createElement("option");
                option.text = item.regionName;
                option.value = item.regionId;

                options.appendChild(option);
            });

            console.log(JSON.stringify(regionsData));
        },
        error: function () {
            console.error("Cannot load list product types");
        }
    });

}

function deleteSelectedProducts() {
    console.log(JSON.stringify(discounts[currentSelected].productRegionPrices));
    var tmpArr = [];

    // $('.all').prop("checked",false);
    var items = $("#products input:checked:not('.all')");
    items.each(function(idx,item){
        var choice = $(item);
        var deleteDomId = choice.parent().attr("name");


        for (var characteristic in discounts[currentSelected].productRegionPrices) {

            if(discounts[currentSelected].productRegionPrices[characteristic].priceId == deleteDomId){
                // tmpArr.push(userData.domains[characteristic]);
                discounts[currentSelected].productRegionPrices.splice(characteristic,1);
                choice.parent().remove();

                console.log(JSON.stringify(characteristic));

            }
        }

    });

    //userData.domains = tmpArr;
    console.log(JSON.stringify(discounts[currentSelected].productRegionPrices));

}

function clearDataFields() {

    $("#discount-name-input").val("");
    $('#datetimepicker1').data("DateTimePicker").date(moment());
    $('#datetimepicker2').data("DateTimePicker").date(moment());
    $("#ammInput").val("");

    var node =$("#products");
    node.empty();


}



$(document).ready(function () {

    $('#datetimepicker1').datetimepicker();
    $('#datetimepicker2').datetimepicker();
    loadAllRegions();
    loadAllDiscounts();


});