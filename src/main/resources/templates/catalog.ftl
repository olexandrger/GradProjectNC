<!DOCTYPE html>
<html lang="en">
<head>
    <title>Catalog</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet" href="/css/catalog.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://momentjs.com/downloads/moment.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twbs-pagination/1.4.1/jquery.twbsPagination.min.js"></script>
    <script src="/js/catalogBar.js"></script>
    <script src="/js/catalog.js"></script>
</head>

<body>
<div class="container">
<#include "resources/navbar.ftl"/>

    <div class="row">
        <div class="col-sm-12">
            <ul class="nav nav-pills nav-justified" id="catalog-product-types-list"></ul>
        </div>
    </div>

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
                        <tr>
                            <th colspan="3">Details</th>
                        </tr>
                    </table>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12">
                    <a class="btn btn-primary active hidden" onclick="catalogCreateOrder()"
                       id="catalog-new-order-button">Order</a>
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

    <div class="modal fade" id="new-product-order-modal" tabindex="-1" role="dialog" aria-labelledby="Order modal"
         aria-hidden="true" style="display: none;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header" align="center">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                    </button>
                    <h1>Create order</h1>
                    <div id="catalog-order-alert"></div>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="catalog-domain-selector" class="radio">Select domain
                            <select class="form-control" id="catalog-domain-selector"
                                    onchange="catalogChangeDomain(this.options[this.selectedIndex].value)">
                            </select>
                        </label>
                    </div>
                    <div>
                        <div class="form-group">
                            <label for="catalog-price-field" class="radio">Price</label>
                            <input type="text" class="form-control" name="price" id="catalog-price-field" readonly>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" id="new-product-order-modal-submit" class="btn btn-primary btn-lg btn-block"
                            onclick="catalogSubmitOrder()">Create
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>