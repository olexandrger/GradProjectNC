<!DOCTYPE html>
<html lang="en">
<head>
    <title>Orders</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>


    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


    <link rel="stylesheet" href="/css/main.css">
    <script src="/js/order.js"></script>
    <script src="https://momentjs.com/downloads/moment.js"></script>

</head>
<body>
<div class="container">

<#include "../resources/navbar.ftl"/>

    <div class="col-sm-2">
        <div class="col-sm-12">
            <a class="btn btn-default" href="/csr/orders/">❮ All orders</a>
        </div>
    </div>

    <div class="col-sm-8">
        <div class="row">
            <div id="csr-order-alert-place">
            </div>
                <h2 id="order-header"></h2>
                <div class="hidden" id="order-main">

                    <div class="form-group">
                        <label for="order-user-name">User</label>
                        <input type="text" class="form-control" name="order-user-name" id="order-user-name" readonly>
                    </div>

                    <div class="form-group">
                        <label for="order-domain">Domain</label>
                        <select class="form-control" name="order-domain" id="order-domain">
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="order-product">Product</label>
                        <select class="form-control" name="order-product" id="order-product">
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="order-aim">Aim</label>
                        <input type="text" class="form-control" name="order-aim" id="order-aim" readonly>
                    </div>

                    <div class="form-group">
                        <label for="order-status">Status</label>
                        <input type="text" class="form-control" name="order-status" id="order-status" readonly>
                    </div>

                    <div class="form-group">
                        <label for="order-start-date">Start date</label>
                        <input type="text" class="form-control" name="order-start-date" id="order-start-date" readonly>
                    </div>

                    <div class="form-group">
                        <label for="order-end-date">End date</label>
                        <input type="text" class="form-control" name="order-end-date" id="order-end-date" readonly>
                    </div>

                    <div class="form-group text-center">
                        <button class="btn btn-default order-button hidden" id="order-button-update"
                                onclick="updateSelectedOrder()">Update
                        </button>
                        <button class="btn btn-default order-button hidden" id="order-button-cancel"
                                onclick="cancelSelectedOrder()">Cancel
                        </button>
                        <button class="btn btn-default order-button hidden" id="order-button-start"
                                onclick="startSelectedOrder()">
                            Start
                        </button>
                        <button class="btn btn-default order-button hidden" id="order-button-complete"
                                onclick="completeSelectedOrder()">Complete
                        </button>
                    </div>

                </div>

        </div>
    </div>
</div>

</body>
