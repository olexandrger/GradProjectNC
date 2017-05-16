<!DOCTYPE html>
<html lang="en">
<head>
    <title>Complains</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>


    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


    <link rel="stylesheet" href="/css/main.css">
    <script src="/js/complain.js"></script>
    <script src="https://momentjs.com/downloads/moment.js"></script>

</head>

<body>

<div class="container">
<#include "../resources/navbar.ftl"/>

<div class="row">
    <div class="col-sm-4">
        <div class="panel panel-default">
            <div class="panel-heading" id="complain-list-panel-heading">Complains</div>
            <div class="panel-body" id="complain-list-panel-body">
                <ul class="list-group">
                    <a class="list-group-item" href = "#">Complain 1</a>
                    <a class="list-group-item" href = "#">Complain 2 </a>
                    <a class="list-group-item" href = "#">Complain 3</a>
                </ul>
            </div>
            <div class="panel-footer" id="complain-list-panel-footer">
                <button type="button" class="btn"> &larr; Previous</button>
                <button type="button" class="btn"> Next &rarr;</button>
                <button type="button" class="btn btn-success">New</button>
            </div>
        </div>
    </div>
    <div class="col-sm-8">
        <div class="panel panel-default">
            <div class="panel-heading" id="complain-content-panel-heading">Complain content:</div>
            <div class="panel-body" id="complain-content-panel-body">
                <div class="alert alert-info" id="no-complain-selected-info">
                    <strong>Info!</strong> Select a complaint to view content....
                </div>
            </div>
            <div class="panel-footer" id="complain-content-panel-footer">Buttons</div>
        </div>
    </div>
</div>

</div>

</body>