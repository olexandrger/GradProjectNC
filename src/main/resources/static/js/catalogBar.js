var productTypesCache;

function loadCategories() {
    $.ajax({
        url: "/api/user/productTypes/active",
        success: function (data) {
            console.log("Product types loaded");
            productTypesCache = data;
            $pagination = $('#pagination');

            var productTypeList = $("#catalog-product-types-list");

            data.forEach(function (productType, i) {
                var li = document.createElement("li");

                var ref = document.createElement("a");
                ref.appendChild(document.createTextNode(productType.productTypeName));
                ref.href = "#";
                ref.onclick = function () {
                   $('#catalog-product-types-list li').removeClass('active');
                    $(this).parent().addClass('active');

                    loadCatalogPageOfType({productTypeId: productType.productTypeId});
                };

                li.appendChild(ref);
                productTypeList.append(li);
            });

        },
        error: function () {
            console.error("Cannot load product types");
        }
    });
}

$(document).ready(function () {
    loadCategories();
});