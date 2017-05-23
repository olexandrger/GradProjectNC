<!DOCTYPE html>
<html lang="en">
<head>
    <title>Products</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.min.css">

    <link rel="stylesheet" href="/css/main.css">

    <link rel="stylesheet" href="/css/products.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.18.1/moment.min.js"></script>
    <script src=" https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twbs-pagination/1.4.1/jquery.twbsPagination.min.js"></script>

    <script src="/js/products.js"></script>
</head>
<body>
<div class="container">
<#include "../resources/navbar.ftl"/>

    <div class="row">
        <div class="col-sm-10 col-sm-offset-1">
            <div id="new-product-alert-place"></div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-3 col-sm-offset-1">
            <div class="list-group" id="products-list"></div>

            <div class="text-center">
                <ul id="pagination" class="pagination-xs"></ul>
            </div>

            <div class="form-group">
                <input type="text" class="form-control" placeholder="New product" id="new-product-name">
                <span class="input-group-btn">
                        <button id="add-product-button" type="button" class="btn btn-default pull-right">
                            <span class="glyphicon glyphicon-plus"></span>
                            Add new product
                        </button>
                </span>
            </div>
        </div>
        <div class="col-sm-7 hidden" id="products-editor">

            <div class="row">
                <div class="col-sm-12">
                    <ul class="nav nav-tabs">
                        <li class="active"><a data-toggle="tab" href="#product-general">Main information</a></li>
                        <li><a data-toggle="tab" href="#product-characteristics">Characteristics</a></li>
                    </ul>

                    <div class="tab-content">
                        <div id="product-general" class="tab-pane fade in active">
                            <div id="product-general-editor">

                                <div class="form-group">
                                    <label for="product-name">Name</label>
                                    <input type="text" class="form-control" name="product-name" placeholder="Name"
                                           id="product-name-input">
                                </div>

                                <div class="form-group">
                                    <label for="product-type-selector" class="radio">Product type
                                        <select class="form-control" id="product-type-selector"
                                                onchange="displayCharacteristics()">
                                        </select>
                                    </label>
                                </div>

                                <div class="form-group">
                                    <label class="radio" for="product-status">Active status</label>
                                    <div class="radio">
                                        <label><input type="radio" value="true" name="product-status">Active</label>
                                    </div>
                                    <div class="radio">
                                        <label><input type="radio" value="false"
                                                      name="product-status">Inactive</label>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="product-description">Description</label>
                                    <textarea class="form-control" rows="5" name="product-description"
                                              placeholder="Description" id="product-description-input"></textarea>
                                </div>

                                <div class="form-group">
                                    <label>Regional prices</label>
                                    <a class="btn btn-default" onclick="addRegionalPrice()">
                                        <span class="glyphicon glyphicon-plus"></span>
                                        Add new regional price
                                    </a>
                                </div>
                            </div>
                        </div>
                        <div id="product-characteristics" class="tab-pane fade"></div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12" style="margin-top: 10px;">
                    <div class="col-xs-12 text-center">
                        <div class="form-group">
                            <a class="btn btn-success" onclick="saveSelectedProduct()">
                                <span class="glyphicon glyphicon-floppy-disk"></span>
                                Save
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>