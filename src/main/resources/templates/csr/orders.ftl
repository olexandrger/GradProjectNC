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
        <div class="col-sm-5">

            <div class="list-group">
                <a href="#" class="list-group-item">First order</a>
                <a href="#" class="list-group-item">Second order</a>
                <a href="#" class="list-group-item">Third order</a>
            </div>

            <ul class="pager">
                <li class="previous"><a href="#">Previous</a></li>
                <li class="next"><a href="#">Next</a></li>
            </ul>
        </div>
        <div class="col-sm-7">

            <div class="form-group">
                <label for="order-user-name">User</label>
                <input type="text" class="form-control" name="order-user-name" id="order-user-name" value="Some User" readonly>
            </div>

            <div class="form-group">
                <label for="order-domain">Domain</label>
                <select class="form-control" name="order-domain" id="order-domain">
                    <option>Domain 1</option>
                    <option selected>Domain 2</option>
                    <option>Domain 3</option>
                </select>
            </div>

            <div class="form-group">
                <label for="order-domain">Product</label>
                <select class="form-control" name="order-product" id="order-product">
                    <option>Product 1</option>
                    <option selected>Product 2</option>
                    <option>Product 3</option>
                </select>
            </div>

            <div class="form-group">
                <label for="order-user-name">Aim</label>
                <input type="text" class="form-control" name="order-user-name" id="order-user-name" value="Suspend" readonly>
            </div>

            <div class="form-group">
                <label for="order-user-name">Status</label>
                <input type="text" class="form-control" name="order-user-name" id="order-user-name" value="In progress" readonly>
            </div>

            <div class="form-group">
                <label for="order-user-name">Start date</label>
                <input type="text" class="form-control" name="order-user-name" id="order-user-name" value="11.11.1111" readonly>
            </div>

            <div class="form-group">
                <label for="order-user-name">End date</label>
                <input type="text" class="form-control" name="order-user-name" id="order-user-name" value="" readonly>
            </div>

            <div class="form-group text-center">
                <button class="btn">Update</button>
                <button class="btn">Cancel</button>
                <button class="btn">Complete</button>
            </div>

        </div>
    </div>

</div>
</body>