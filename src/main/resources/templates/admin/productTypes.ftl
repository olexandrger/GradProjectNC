<!DOCTYPE html>
<html lang="en">
<head>
    <title>Product types</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>


    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


    <link rel="stylesheet" href="/css/main.css">
    <script src="/js/productTypes.js"></script>

</head>
<body>

<div class="container">

<#include "../resources/navbar.ftl"/>

<div class="row">

    <div class="row">
        <div class="col-sm-10 col-sm-offset-1">
            <div id="new-product-type-alert-place">
            </div>
        </div>
    </div>
    <div class="row">
        <div class=" col-sm-3 col-sm-offset-1">
            <div class="list-group" id="product-types-list"></div>
            <#--<span class="input-group-btn">-->
                <#--<button type="button" onclick="this.style.visibility='hidden'; loadAllProductTypes();" class="btn btn-info btn-block"> show all</button>-->
                    <#--</span>-->
            <div class="input-group">
                <input type="text" class="form-control" placeholder="New type" id="new-product-type-name">
                <span class="input-group-btn">
                <button type="button" onclick="addProductType()" class="btn btn-default"><span class="glyphicon glyphicon-plus"></span>Add type</button>
            </span>
            </div>
        </div>
        <div class="col-sm-7 hidden" id="product-type-editor">

            <div class="row">
                <div class="col-sm-12" id="product-type-values">
                    <div class="form-group">
                        <label for="product-type-name">Name</label>
                        <input type="text" class="form-control" name="product-type-name" placeholder="Name" id="product-type-name-input">
                    </div>

                    <div class="form-group">
                        <label for="product-type-description">Description</label>
                        <textarea class="form-control" rows="5" name="product-type-description" placeholder="Description" id="product-type-description-input"></textarea>
                    </div>
                    <div class="form-group">
                        <label class="radio" for="product-type-status">Status</label>
                        <div class="radio">
                            <label><input type="radio" value="true" name="product-type-status">Active</label>
                        </div>
                        <div class="radio">
                            <label><input type="radio" value="false" name="product-type-status">Suspended</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>Characteristics</label>
                        <a class="btn btn-default" onclick="addProductCharacteristic()">
                            <span class="glyphicon glyphicon-plus"></span>Add
                        </a>
                    </div>
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
</div>
</div>
</body>
