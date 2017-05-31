var productTypesCache;

function loadCategories() {
    $.ajax({
        url: "/api/user/productTypes?active&regionId=" + localStorage.getItem("regionId"),
        success: function (data) {
            productTypesCache = data;
            var $productTypeList = $("#catalog-product-types-list");

            if (productTypesCache.length == 0) {
                $productTypeList.append($('<h3>There are no available services for specified region</h3>'));
            }

            data.forEach(function (productType) {
                var li = document.createElement("li");

                var ref = document.createElement("a");
                ref.appendChild(document.createTextNode(productType.productTypeName));
                ref.href = "#";
                ref.onclick = function () {
                    var $contentAndPaginationRow =  $('#content-row, #pagination-row');
                    $contentAndPaginationRow.addClass('hidden');
                    $productTypeList.find('li').removeClass('active');
                    $(this).parent().addClass('active');

                    loadCatalogPageOfType({productTypeId: productType.productTypeId}, function () {
                        if (catalogProductsCache.length > 0) {
                            selectCatalogProduct(0);
                            $contentAndPaginationRow.removeClass('hidden');
                        }
                    });
                };

                li.appendChild(ref);
                $productTypeList.append(li);
            });
        },
        error: function () {
            console.error("Cannot load product types");
        }
    });
}

$(document).ready(function () {
    $pagination = $('#pagination');
    loadCategories();
});