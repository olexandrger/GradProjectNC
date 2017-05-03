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


    <script src="/js/catalog.js"></script>
</head>
<body>

<div class="container">


<#include "resources/navbar.ftl"/>
<#include "resources/catalogBar.ftl"/>

<div class="row" style="margin-top: 10px;">

    <div class="col-sm-4">
        <div class="list-group" id="catalog-products-list">
        </div>
    </div>

    <div class="col-sm-8 hidden" id="catalog-main-info">
        <div class="row">
            <div class="col-sm-12">
                <h2 id="catalog-product-name">Product name here</h2>
                <p id="catalog-product-description">Product description here</p>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <table class="table table-bordered" id="catalog-table-details">
                    <tr><th colspan="2">Details</th></tr>
                </table>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <a class="btn btn-primary active">Order</a>
            </div>
        </div>
    </div>

</div>

</div>
</body>
</html>