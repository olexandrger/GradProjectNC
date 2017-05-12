<!DOCTYPE html>
<html lang="en">
<head>
    <title>Products</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>


    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


    <link rel="stylesheet" href="/css/main.css">
    <script src="/js/products.js"></script>

</head>
<body>
<div class="container">

    <#include "../resources/navbar.ftl"/>

    <div class="row">

        <div class="row">
            <div class="col-sm-10 col-sm-offset-1">
                <div id="new-product-alert-place">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-3 col-sm-offset-1">
                <div class="list-group" id="products-list"></div>
                <span class="input-group-btn">
                <button type="button" onclick="this.style.visibility='hidden'; loadAllProducts();" class="btn btn-info btn-block"> show all</button>
                    </span>
                <div class="input-group">
                    <input type="text" class="form-control" placeholder="New product" id="new-product-name">
                    <span class="input-group-btn">
                        <button type="button" onclick="addProduct(); selectProduct();" class="btn btn-default"><span class="glyphicon glyphicon-plus"></span></button>
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
                                        <input type="text" class="form-control" name="product-name" placeholder="Name" id="product-name-input">
                                    </div>

                                    <div class="form-group">
                                        <label for="product-type-selector" class="radio">Product type
                                            <select class="form-control" id="product-type-selector" onchange="changeCharacteristics()">
                                            </select>
                                        </label>
                                    </div>

                                    <div class="form-group">
                                        <label class="radio" for="product-status">Status</label>
                                        <div class="radio">
                                            <label><input type="radio" value="true" name="product-status">Active</label>
                                        </div>
                                        <div class="radio">
                                            <label><input type="radio" value="false" name="product-status">Suspended</label>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="product-description">Description</label>
                                        <textarea class="form-control" rows="5" name="product-description" placeholder="Description" id="product-description-input"></textarea>
                                    </div>

                                    <div class="form-group">
                                        <label>Regional prices</label>
                                        <a class="btn btn-default" onclick="addRegionalPrice()">
                                            <span class="glyphicon glyphicon-plus"></span>Add
                                        </a>
                                    </div>

                                </div>
                            </div>
                            <div id="product-characteristics"  class="tab-pane fade">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12" style="margin-top: 10px;">
                        <div class="col-xs-12 text-center">
                            <div class="form-group">
                                <a class="btn btn-success" onclick="saveSelected()">
                                    <span class="glyphicon glyphicon-floppy-disk"></span>Save
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
