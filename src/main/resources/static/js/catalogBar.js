function loadCategories() {
    $.ajax({
        url: "/api/user/productTypes/all",
        success: function (data) {
            console.log("Product types loaded");
            var list = $("#catalog-product-types-list");

            data.forEach(function (productType, i) {

                var li = document.createElement("li");
                li.setAttribute("id", "catalog-link-" + productType.productTypeId);

                if (productType.name == decodeURIComponent(window.location.search.substr(1))) {
                    li.className += "active";
                }

                var ref = document.createElement("a");
                ref.appendChild(document.createTextNode(productType.productTypeName));
                ref.href = "/catalog?" + productType.productTypeName;

                li.appendChild(ref);

                list.append(li);
            });

        },
        error: function () {
            console.error("Cannot load product types");
        }
    });
}


$(document).ready(function () {
    $("#navbar-main-page").addClass("active");

    loadCategories();
});

//
// $(document).on("create-order", function(event, product) {
//     console.log("Selected product");
//     console.log(product);
// });