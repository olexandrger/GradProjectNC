<!DOCTYPE html>
<html lang="en">
<head>
    <title>Product types</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet" href="/css/typeahead.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twbs-pagination/1.4.1/jquery.twbsPagination.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/typeahead.js/0.11.1/typeahead.bundle.min.js"></script>

    <script src="/js/productTypes.js"></script>

</head>
<body>

<div class="container">

<#include "../resources/navbar.ftl"/>

    <div class="row">
        <div class="col-sm-10 col-sm-offset-1">
            <div id="new-product-type-alert-place">
            </div>
        </div>
    </div>
    <div class="row">
        <div class=" col-sm-3 col-sm-offset-1">
            <div id="typeahead-box" class="input-group">
                <span class="input-group-addon info"><span class="glyphicon glyphicon-search"></span></span>
                <input class="typeahead" type="text" placeholder="Search">
                <button type="button" id="search-clear" class="btn btn-link hide"><i class="glyphicon glyphicon-remove"></i></button>
            </div>
            <div class="list-group" id="product-types-list"></div>
            <div id="alert-box"></div>
            <div class="input-group">
                <input type="text" class="form-control" placeholder="New type" id="new-product-type-name">
                <span class="input-group-btn">
                <button id="add-product-type-button" type="button"
                        class="btn btn-default"><span class="glyphicon glyphicon-plus"></span>Add type</button>
            </span>
            </div>
        </div>
        <div class="col-sm-7 hidden" id="product-type-editor">

            <div class="row">
                <div class="col-sm-12" id="product-type-values">
                    <div class="form-group">
                        <label for="product-type-name">Name</label>
                        <input type="text" class="form-control" name="product-type-name" placeholder="Name"
                               id="product-type-name-input">
                    </div>

                    <div class="form-group">
                        <label for="product-type-description">Description</label>
                        <textarea class="form-control" rows="5" name="product-type-description"
                                  placeholder="Description" id="product-type-description-input"></textarea>
                    </div>
                    <div class="form-group">
                        <label class="radio" for="product-type-status">Active status</label>
                        <div class="radio">
                            <label><input type="radio" value="true" name="product-type-status">Active</label>
                        </div>
                        <div class="radio">
                            <label><input type="radio" value="false" name="product-type-status">Inactive</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>Characteristics</label>
                        <a class="btn btn-default" onclick="addProductCharacteristic()">
                            <span class="glyphicon glyphicon-plus"></span>Add
                        </a>
                    </div>
                    <div id="characteristic-box"></div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12" style="margin-top: 10px;">
                    <div class="col-xs-12 text-center">
                        <div class="form-group">
                            <a class="btn btn-success" onclick="saveSelectedProductType()">
                                <span class="glyphicon glyphicon-floppy-disk"></span>Save
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <div class="text-center">
                <ul id="pagination" class="pagination-xs"></ul>
            </div>
        </div>
    </div>
</div>
</body>
