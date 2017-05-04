<div class="row">

    <div class="col-sm-12">
        <ul class="nav nav-pills" id="catalog-product-types-list">
        </ul>
    </div>

</div>

<script>
    function loadCategories() {
        $.ajax({
            url: "/api/user/productTypes/all",
            success: function (data) {
                console.log("Product types loaded");
                var list = $("#catalog-product-types-list");

                data.forEach(function (item, i) {

                    var li = document.createElement("li");
                    li.setAttribute("id", "catalog-link-" + item.id);

                    if (item.name == decodeURIComponent(window.location.search.substr(1))) {
                        li.className += "active";
                    }

                    var ref = document.createElement("a");
                    ref.appendChild(document.createTextNode(item.name));
                    ref.href = "/catalog?" + item.name;

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
</script>