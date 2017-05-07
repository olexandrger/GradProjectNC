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
    <script src="/js/instance.js"></script>

</head>
<body>
<div class="container">

<#include "../resources/navbar.ftl"/>
    <div class="row">
        <div class="col-sm-10 col-sm-offset-1">
            <div class="row">
                <div class="col-sm-9">
                    <h3>Product name</h3>
                    <div class="tab">
                        <p>Product type: Some type</p>
                        <address>Address: <p class="tab">Street<br/>City</p></address>
                        <p>Price: 142</p>
                    </div>
                </div>
                <div class="col-sm-3 pull-down" style="margin-bottom: 5px">
                    <button class="btn btn-block">Create complain</button>
                    <button class="btn btn-block">Suspend</button>
                    <button class="btn btn-block">Deactivate</button>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12">
                    <h3 class="text-center">Orders</h3>
                    <table class="table table-striped">
                        <tr><th>Action</th><th>Status</th><th>Open date</th><th>Close date</th><th></th></tr>
                        <tr><td>Dosos</td><td>Closed</td><td>1.1.1111</td><td>2.1.1111</td><td>
                            <button class="btn pull-right">Cancel</button></td></tr>
                        <tr><td>Create</td><td>Closed</td><td>1.1.1111</td><td>2.1.1111</td><td></td></tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
